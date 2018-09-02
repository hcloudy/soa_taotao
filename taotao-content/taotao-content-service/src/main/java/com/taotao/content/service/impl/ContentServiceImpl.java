package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Override
    public EasyUIDataGrid getContentList(Long categoryId,Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbContentExample example = new TbContentExample();
        List<TbContent> list = new ArrayList<>();
        if(categoryId == 0 || categoryId == null) {
            list = tbContentMapper.selectByExample(example);
        }else {
            TbContentExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            list = tbContentMapper.selectByExample(example);
        }
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUIDataGrid easyUIDataGrid = new EasyUIDataGrid();
        easyUIDataGrid.setTotal((int) pageInfo.getTotal());
        easyUIDataGrid.setRows(list);
        return easyUIDataGrid;
    }

    @Override
    public TaotaoResult createContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.insertSelective(tbContent);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContent(String ids) {
        String[] split = ids.split(",");
        for(String id : split) {
            tbContentMapper.deleteByPrimaryKey(Long.valueOf(id));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setUpdated(date);
        tbContent.setCreated(date);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(tbContent.getId());
        tbContentMapper.updateByExampleSelective(tbContent,example);
        return TaotaoResult.ok();
    }
}
