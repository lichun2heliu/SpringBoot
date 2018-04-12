package com.kotei.allweather.Test;

import com.kotei.core.utils.http.HttpCommon;
import org.springframework.util.StringUtils;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/9
 * 修改历史：
 * 1. [2018/4/9]创建文件
 *
 * @author chunl
 */
public class TestHttpCommon {

    public static void main(String[] args) {
        String url = "http://127.0.0.1:8200/Sys/Auth/Test";

        HttpCommon httpCommon = new HttpCommon() {
            @Override
            public void callBack(String responseString) {
                if (StringUtils.hasLength(responseString)){
                    System.out.println("transfer success");
                }
            }
        };

        httpCommon.get(url, null);

    }
}
