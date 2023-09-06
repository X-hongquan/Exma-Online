package com.chq.common;

import lombok.Data;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

@Data
public class R<T> implements Serializable {

    private Integer code;
    private T data;
    private String msg;
    private Long total;

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.code=1;
        r.data=data;
        return r;
    }

    public static <T> R<T> ok(T data,Long total) {
        R<T> r = new R<>();
        r.code=1;
        r.data=data;
        r.total=total;
        return r;
    }
    public static  R ok() {
        R r = new R<>();
        r.code=1;
        return r;
    }

    public static R fail(String msg) {
        R<Object> r = new R<>();
        r.code=0;
        r.msg=msg;
        return r;
    }


}
