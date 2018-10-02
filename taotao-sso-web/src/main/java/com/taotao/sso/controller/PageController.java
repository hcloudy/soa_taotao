package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 显示页面
 */
@Controller
public class PageController {

    @RequestMapping("/page/{page}")
    public String showPage(@PathVariable String page) {
        return page;
    }
}
