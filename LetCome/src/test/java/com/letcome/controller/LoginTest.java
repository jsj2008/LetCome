package com.letcome.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rjt on 16/8/10.
 */
public class LoginTest {
    public static void main(String[] args){

        try {
//            Jsoup.connect("http://192.168.4.105:8080/user/remove").data("uid","").post();

            Map map = new HashMap<String ,String>();
            map.put("email","kinghy2003@qq.com");
            map.put("pwd","kinghy2003");
            Document doc = Jsoup.connect("http://192.168.4.105:8080/user/login").data(map).post();
            System.out.println("123123123123123123");
            System.out.println(doc);
            System.out.println(doc.data());
            Elements elements = doc.getAllElements();
            for(Element e : elements) {
                System.out.println(e.text());
            }
        }catch (Exception e){

        }
    }
}
