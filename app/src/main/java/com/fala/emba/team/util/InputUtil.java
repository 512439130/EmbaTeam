package com.fala.emba.team.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SoMustYY on 2017/10/2.
 * 检查输入工具类
 */

public class InputUtil {
    /**
     * 判断是否为邮箱格式
     * @param email
     * @return
     */
    public boolean isEmail(String email){
        Pattern pattern = Pattern.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            return false;
        }
        return true;
    }
}
