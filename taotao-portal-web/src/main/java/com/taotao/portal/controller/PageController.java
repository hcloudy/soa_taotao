package com.taotao.portal.controller;

import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AdNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示页面
 */
@Controller
public class PageController {

    @Autowired
    private ContentService contentService;
    @Value("${REST_CID}")
    private Long REST_CID;
    @Value("${AD_HEIGHT}")
    private int AD_HEIGHT;
    @Value("${AD_WIDTH}")
    private int AD_WIDTH;
    @Value("${AD_HEIGHT2}")
    private int AD_HEIGHT2;
    @Value("${AD_WIDTH2}")
    private int AD_WIDTH2;

    @RequestMapping("/index")
    public String showIndex(Model model) {
        List<TbContent> listByCatId = contentService.getContentListByCatId(REST_CID);
        List<AdNode> nodes = new ArrayList<>();
        for (TbContent tbContent : listByCatId) {
            AdNode adNode = new AdNode();
            adNode.setHeight(AD_HEIGHT);
            adNode.setHeightB(AD_HEIGHT2);
            adNode.setWidth(AD_WIDTH);
            adNode.setWidthB(AD_WIDTH2);
            adNode.setAlt(tbContent.getSubTitle());
            adNode.setHerf(tbContent.getUrl());
            adNode.setSrc(tbContent.getPic());
            adNode.setSrcB(tbContent.getPic2());
            nodes.add(adNode);
        }
        model.addAttribute("ad1", JsonUtils.objectToJson(nodes));
        return "index";
    }
}
