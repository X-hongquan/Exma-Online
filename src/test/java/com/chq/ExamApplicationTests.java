package com.chq;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.chq.mapper.StuClassMapper;
import com.chq.pojo.StuClass;
import com.chq.pojo.eo.StuClassEo;
import com.chq.service.IStuClassService;
import jakarta.annotation.Resource;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ExamApplicationTests {

    @Resource
    private IStuClassService service;

    @Test
    void contextLoads() throws IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream("计组测试3.docx"));

        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        int i=60;
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            if (i==60) break;
            String text = paragraph.getText();
            i--;
        }
    }

    @Test
    void write(){
        String fileName =  "xx.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        List<StuClass> list =
                service.list();
        List<StuClassEo> collect = list.stream().map(item -> {
            StuClassEo stuClassEo = new StuClassEo();
            BeanUtils.copyProperties(item, stuClassEo);
            return stuClassEo;
        }).collect(Collectors.toList());
        EasyExcel.write(fileName, StuClassEo.class).sheet("模板").doWrite(collect);

    }

}
