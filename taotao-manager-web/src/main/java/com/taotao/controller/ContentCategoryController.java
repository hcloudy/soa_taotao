package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类Controller
 */
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;
    @RequestMapping(value = "/content/category/list",method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
        return list;
    }

    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createContentCategory(String name,Long parentId) {
        return contentCategoryService.createContentCategory(parentId,name);
    }

    @RequestMapping(value = "/content/category/delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCategory(long id) {
        return contentCategoryService.deleteContentCategory(id);
    }
}
