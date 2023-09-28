package com.chq.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chq.common.R;
import com.chq.common.Type;
import com.chq.mapper.ExamMapper;
import com.chq.mapper.QuestionMapper;
import com.chq.pojo.Exam;
import com.chq.pojo.Question;
import com.chq.pojo.Test;
import com.chq.mapper.TestMapper;
import com.chq.pojo.dto.QuestionDto;
import com.chq.pojo.dto.TestDto;
import com.chq.pojo.dto.UserDto;
import com.chq.service.IQuestionService;
import com.chq.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chq.util.UserHolder;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.*;

import static com.chq.cache.CachePool.COURSE_CACHE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈鸿权
 * @since 2023-07-08
 */
@Service
@Slf4j
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {


    @Autowired
    private MinioClient minioClient;

    @Resource
    private IQuestionService questionService;


    @Resource
    private ExamMapper examMapper;


    private Map<String,Integer> sortMap=new HashMap<>();



    @Value("${minio.bucket.test}")
    private String test;

    @Value("${minio.endpoint}")
    private String server;





  @PostConstruct
    public void problem() {
        for (int i = 1; i < 31; i++) {
            sortMap.put(String.valueOf(i),i);
        }
    }

    @Override
    @Transactional
    public R upload(byte[] bytes, String contentType, String originalFilename) throws IOException {
        String s = DigestUtil.md5Hex(bytes);
        String tail = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName=s+tail;
        try (ByteArrayInputStream bs = new ByteArrayInputStream(bytes)) {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().object(fileName)
                    .contentType(contentType).stream(bs, bytes.length, -1)
                    .bucket(test)
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            throw new RuntimeException("上传失败");
        }
        Integer id = add(fileName);
        parseQuestion(id,bytes);
        return R.ok();
    }

    @Override
    public R getList() {
        UserDto user = UserHolder.getUser();
        List<Test> list = lambdaQuery().eq(Test::getAuthorId, user.getId()).orderByDesc(Test::getCreateTime).list();
        return R.ok(list);
    }

    @Override
    public R del(Integer id) {
        Exam exam = examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getTestId, id));
        if (exam!=null) return R.fail("该试卷已经绑定考试");
        removeById(id);
        return R.ok();
    }

    @Override
    public R updateName(Test test) {
        Exam exam = examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getTestId, test.getId()));
        if (exam!=null) return R.fail("该试卷已经绑定考试");
        updateById(test);
        return R.ok();

    }

    @Override
    @Transactional
    public R handleAdd(TestDto testDto) {

        UserDto user = UserHolder.getUser();
        Integer userId = user.getId();
        if (getOne(new LambdaQueryWrapper<Test>().eq(Test::getName,testDto.getName()))!=null) return R.fail("名称已经存在");
        Test test = new Test();
        BeanUtils.copyProperties(testDto,test);
        test.setAuthorName(user.getName());
        test.setAuthorId(userId);
            save(test);
            List<QuestionDto> arr = testDto.getNewTest();
            List<Question> a = new ArrayList<>();
            int n = 1;
            for (QuestionDto questionDto : arr) {
                System.out.println("我的:" + questionDto.getTypeId());
                Question question = new Question();
                BeanUtils.copyProperties(questionDto, question);
                question.setCourseId(test.getCourseId());
                question.setUserId(userId);
                question.setSort(n++);
                question.setTestId(test.getId());
                a.add(question);
            }
            questionService.saveBatch(a);
            return R.ok();

    }


    public  void parseQuestion(Integer id, byte[] bytes) throws IOException {
        UserDto user = UserHolder.getUser();
        XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(bytes));
          List<XWPFParagraph> paragraphs = document.getParagraphs();
          Question question = null;
          int typeId = 0;
          int n=1;
          int courseId=2;
          for (XWPFParagraph paragraph : paragraphs) {
              String text = paragraph.getText();
              text=text.trim();
              if (text!=""||text.length() > 2) {
                  String s ;
                  if (n<10)
                      s=text.substring(0,1);
                  else
                      s=text.substring(0,2);
                  if (COURSE_CACHE.containsKey(text))
                      courseId=COURSE_CACHE.get(text);
                  if (text.contains("选择题"))
                      typeId = Type.SELECT.getKey();
                  else if (text.contains("判断题"))
                      typeId = Type.JUDGE.getKey();
                  else if(text.contains("填空题"))
                      typeId=Type.FILLING.getKey();
                  else if (text.contains("综合题"))
                      typeId=Type.COMPREHENSIVE.getKey();
                  else if (sortMap.containsKey(s)) {
                      question = new Question();
                      question.setUserId(user.getId());
                      question.setCourseId(courseId);
                      question.setTestId(id);
                      question.setSort(sortMap.get(s));
                      question.setTypeId(typeId);
                      if (typeId == Type.SELECT.getKey() ||typeId==Type.JUDGE.getKey()) {
                          int end =-1;
                          end = text.indexOf(")");
                          if (end==-1) test.indexOf("）");
                          char c = text.charAt(end - 1);
                          question.setResult(String.valueOf(c));
                          String s1=null;
                          if (n<10)  s1= text.substring(2, end - 1);
                          else s1=text.substring(3,end-1);
                          String s2 = text.substring(end);
                          String content = s1 + s2;
                          question.setContent(content);
                      } else {
                          if (typeId==Type.FILLING.getKey()) question.setGrade(Type.FILLING.getGrade());
                          else question.setGrade(Type.COMPREHENSIVE.getGrade());
                          question.setContent(text.substring(3));
                         questionService.save(question);
                          n++;
                      }
                  } else {
                      char c = text.charAt(0);
                      if (typeId == Type.SELECT.getKey()) {
                          switch (c) {
                              case 'A'-> question.setOptiona(text.substring(2));
                              case 'B'-> question.setOptionb(text.substring(2));
                              case 'C'-> question.setOptionc(text.substring(2));
                              case 'D'-> {
                                  question.setOptiond(text.substring(2));
                                  question.setGrade(Type.SELECT.getGrade());
                                  questionService.save(question);
                                  n++;
                              }
                              default -> {}
                          }
                      } else {
                          if (text.startsWith("A"))
                              question.setOptiona(text.substring(2));
                          else if (text.startsWith("B")){
                              question.setOptionb(text.substring(2));
                              question.setGrade(Type.JUDGE.getGrade());
                              questionService.save(question);
                              n++;
                          }
                      }
                  }
              }
          }
  }



    public Integer add(String fileName) {
        String s = appendUrl(fileName);
        Test test = new Test();
        String name = "测试_" + RandomUtil.randomNumbers(6);
        test.setName(name);
        UserDto user = UserHolder.getUser();
        test.setAuthorId(user.getId());
        test.setAuthorName(user.getName());
        test.setUrl(s);
        save(test);
        return test.getId();
    }


    private String appendUrl(String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(server).append("/").append(test).append("/").append(fileName);
        return builder.toString();
    }
}
