package com.huarui.action;


import com.huarui.service.ICreateHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FreemarkerCtrl {


    @Autowired
    private ICreateHtmlService service;


    /**
     * 测试生成静态页面
     * @return
     */
    @RequestMapping("/createHtml")
    public String createHtml(){
       return service.createAllHtml();
    }


} 