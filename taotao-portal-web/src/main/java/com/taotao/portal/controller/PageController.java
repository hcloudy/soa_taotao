package com.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示页面
 */
@Controller
public class PageController {

    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }
}
