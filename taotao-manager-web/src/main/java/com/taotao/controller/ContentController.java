package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/query/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGrid getContentList(Long categoryId,int page,int rows) {
        EasyUIDataGrid contentList = contentService.getContentList(categoryId,page, rows);
        return contentList;
    }

    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addContent(TbContent tbContent) {
        try{
            TaotaoResult result = contentService.createContent(tbContent);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"添加分类异常");
        }
    }

    @RequestMapping(value = "/content/delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContent(String ids) {
        try{
            TaotaoResult result = contentService.deleteContent(ids);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"删除失败");
        }
    }

    @RequestMapping(value = "/rest/content/edit",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContent(TbContent tbContent) {
        try{
            TaotaoResult result = contentService.updateContent(tbContent);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"更新失败。");
        }
    }
}
