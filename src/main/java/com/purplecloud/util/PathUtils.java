package com.purplecloud.util;

import javax.servlet.ServletException;
import java.util.regex.Pattern;
/**
 * 用于将注解中的/？？？？？进行匹配
 * @author 15517
 */
public class PathUtils {
    public static Pattern compile(String path) throws ServletException {
        String regPath = path.replaceAll("\\{([a-zA-Z][a-zA-Z0-9]*)\\}", "(?<$1>[^/]*)");
        //将{}中的名字分类匹配时能通过group获得值
        if (regPath.indexOf('{') >= 0 || regPath.indexOf('}') >= 0) {
            throw new ServletException("Invalid path: " + path);
        }
        return Pattern.compile("^" + regPath + "$");
    }
}
