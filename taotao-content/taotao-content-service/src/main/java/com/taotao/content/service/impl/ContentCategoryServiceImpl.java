package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类Service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;
    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> lists = tbContentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for(TbContentCategory tbContentCategory : lists) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            node.setText(tbContentCategory.getName());
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public TaotaoResult createContentCategory(Long parentId, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        Date date = new Date();
        contentCategory.setName(name);
        contentCategory.setCreated(date);
        contentCategory.setCreated(date);
//        可选值:1(正常),2(删除)
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        contentCategory.setParentId(parentId);
        contentCategory.setIsParent(false);
        tbContentCategoryMapper.insertSelective(contentCategory);

        //插入完成后，判断传进来的parentId是否为父节点，如果不是需要更新为父节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!tbContentCategory.getIsParent()) {
            tbContentCategory.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        }
        return TaotaoResult.ok(contentCategory);
    }

    /**
     * 删除分类的思路：
     *  1、不允许删除父节点
     *  2、删除子节点时，需要判断该父节点下面是否还有其他子节点，如果没有则，将父节点isParent改为false
     *  3、删除分类的同时，要删除该分类下的内容。
     * @param id
     * @return
     */
    @Override
    public TaotaoResult deleteContentCategory(Long id) {
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        Long parentId = tbContentCategory.getParentId();
        //判断是否为父节点
        if(tbContentCategory.getIsParent()) {
            return TaotaoResult.build(500,"不能删除父节点");
        }
        //删除该分类和该分类下的内容
        tbContentCategoryMapper.deleteByPrimaryKey(id);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(id);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        for(TbContent tbContent : list) {
            tbContentMapper.deleteByPrimaryKey(tbContent.getId());
        }
        //判断该分类的父节点下面是否还有子节点，如果没有则将父节点改为子节点
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria1 = tbContentCategoryExample.createCriteria();
        criteria1.andParentIdEqualTo(parentId);
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        if(null == tbContentCategories || tbContentCategories.size() == 0) {
            TbContentCategory tbContentCategory1 = tbContentCategoryMapper.selectByPrimaryKey(parentId);
            tbContentCategory1.setIsParent(false);
            tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory1);
        }
        return TaotaoResult.ok();
    }
}
