package com.xieqing.codeutils.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
    public static String[] match(String text,String statement){
        Matcher mr = Pattern.compile(statement, 40).matcher(text);
        List<String> list = new ArrayList();
        while (mr.find()) {
            list.add(mr.group());
        }
        return (String[]) list.toArray(new String[list.size()]);

    }
}
