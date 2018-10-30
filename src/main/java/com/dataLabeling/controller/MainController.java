package com.dataLabeling.controller;

import com.dataLabeling.service.impl.SpeechTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wzb on 2018/10/29.
 */

@Controller
public class MainController {

    @Autowired
    private SpeechTagService speechTagService;


    @RequestMapping("/index")
    public String init(){


        return "main";
    }


}
