package com.chq.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import com.chq.common.QuestionType;
import com.chq.common.R;
import com.chq.mapper.QuestionMapper;
import com.chq.pojo.Question;
import com.chq.pojo.Test;
import com.chq.mapper.TestMapper;
import com.chq.pojo.dto.UserDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private QuestionMapper questionMapper;


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
    public R upload(byte[] bytes, String contentType, String originalFilename) throws IOException {
        String s = DigestUtil.md5Hex(bytes);
        String tail = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName=s+tail;
        ByteArrayInputStream bs=null;
        try {
            bs = new ByteArrayInputStream(bytes);
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().object(fileName)
                    .contentType(contentType).stream(bs, bytes.length, -1)
                    .bucket(test)
                    .build();
            minioClient.putObject(putObjectArgs);
            log.info("上传成功");
        } catch (Exception e) {
            log.error("上传失败,{}", e.getMessage());
            return R.fail("上传失败");
        } finally {
            try {
                bs.close();
            } catch (IOException e) {
                log.error("流关闭错误,{}",e.getMessage());
            }
        }
        String id = add(fileName);
        parseQuestion(id,bytes);
        return R.ok(id);
    }


      public  void parseQuestion(String id, byte[] bytes) throws IOException {
          XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(bytes));
          List<XWPFParagraph> paragraphs =
                  document.getParagraphs();
          Question question = null;
          int typeId = 0;
          int n=1;
          for (XWPFParagraph paragraph : paragraphs) {
              String text = paragraph.getText();
              text=text.trim();
              if (text!= ""||text.length() > 2) {
                  String s ;
                  if (n<10)
                      s=text.substring(0,1);
                  else
                      s=text.substring(0,2);
                  if (text.contains("选择题"))
                      typeId = QuestionType.CHOICE.ordinal();
                  else if (text.contains("判断题"))
                      typeId = QuestionType.JUDGMENT.ordinal();
                  else if(text.contains("填空题"))
                      typeId=QuestionType.FILLING.ordinal();
                  else if (text.contains("综合题"))
                      typeId=QuestionType.COMPUTE.ordinal();
                  else if (sortMap.containsKey(s)) {
                      question = new Question();
                      question.setTestId(id);
                      question.setSort(sortMap.get(s));
                      question.setTypeId(typeId);
                      if (typeId == QuestionType.CHOICE.ordinal() || typeId == QuestionType.JUDGMENT.ordinal()) {
                          int end = text.indexOf(")");
                          char c = text.charAt(end - 1);
                          question.setResult(String.valueOf(c));
                          String s1 = text.substring(0, end - 1);
                          String s2 = text.substring(end);
                          String content = s1 + s2;
                          question.setContent(content);
                      } else {
                          question.setContent(text);
                          questionMapper.insert(question);
                          n++;
                      }
                  } else if (typeId == QuestionType.CHOICE.ordinal()) {
                      if (text.startsWith("A"))
                          question.setOptiona(text);
                      else if (text.startsWith("B"))
                          question.setOptionb(text);
                      else if (text.startsWith("C"))
                          question.setOptionc(text);
                      else if (text.startsWith("D")) {
                          question.setOptiond(text);
                          questionMapper.insert(question);
                          n++;
                      }
                  } else if (typeId == QuestionType.JUDGMENT.ordinal()) {
                      if (text.startsWith("A"))
                          question.setOptiona(text);
                      else if (text.startsWith("B")) {
                          question.setOptionb(text);
                          questionMapper.insert(question);
                          n++;
                      }
                  }
              }
          }
      }






    public String add(String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(server).append("/").append(test).append("/").append(fileName);
        String s = builder.toString();
        Test test = new Test();
        String uuId = UUID.randomUUID().toString(true);
        test.setId(uuId);
        UserDto user = UserHolder.getUser();
        test.setAuthorId(user.getId());
        test.setAuthorName(user.getName());
        test.setUrl(s);
        save(test);
        return uuId;
    }
}
