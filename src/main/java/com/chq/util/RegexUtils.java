package com.chq.util;

import java.util.regex.Pattern;

public class RegexUtils {
   private static final Pattern p = Pattern.compile("^1[39]\\d{9}$");
   private static final Pattern e =Pattern.compile("^[123456789]\\d{5,12}@qq.com$");



    public final static boolean phoneRegex(CharSequence s) {
        return p.matcher(s).matches();

    }
    public final static boolean mailRegex(CharSequence s) {
        return e.matcher(s).matches();
    }



}

