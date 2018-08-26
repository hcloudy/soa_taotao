package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public EasyUIDataGrid getItemList(Integer page, Integer rows) {
        if (null == page || 0 == page) {
            page = 1;
        }
        if (null == rows || 0 == rows) {
            rows = 30;
        }
        //PageHelper设置分页参数
        PageHelper.startPage(page, rows);
        //使用mapper查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);

        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDataGrid result = new EasyUIDataGrid();
        result.setTotal((int) pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }
}
