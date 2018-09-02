package com.taotao.content.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台内容管理
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Value("${REST_CID}")
    private Long REST_CID;
    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbContentMapper tbContentMapper;

    /**
     * 展示所有的tbContent。
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
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

    /**
     * 新增tbContent
     * @param tbContent
     * @return
     */
    @Override
    public TaotaoResult createContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.insertSelective(tbContent);
        //新增内容时，删除redis中缓存数据。
        try {
            if(tbContent.getCategoryId() == REST_CID) {
                System.out.println("删除缓存了。");
                jedisClient.hdel(CONTENT_KEY, tbContent.getCategoryId() + "");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }

    /**
     * 删除tbContent，包含多个删除
     * @param ids
     * @return
     */
    @Override
    public TaotaoResult deleteContent(String ids) {
        String[] split = ids.split(",");

        //删除内容时，删除redis中缓存数据。
        try {
            for(String id : split) {
                TbContent tbContent = tbContentMapper.selectByPrimaryKey(Long.valueOf(id));
                if(tbContent.getCategoryId() == REST_CID) {
                    System.out.println("删除缓存了。");
                    jedisClient.hdel(CONTENT_KEY, tbContent.getCategoryId() + "");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        for(String id : split) {
            tbContentMapper.deleteByPrimaryKey(Long.valueOf(id));
        }
        return TaotaoResult.ok();
    }

    /**
     * 更新tbContent
     * @param tbContent
     * @return
     */
    @Override
    public TaotaoResult updateContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setUpdated(date);
        tbContent.setCreated(date);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(tbContent.getId());
        tbContentMapper.updateByExampleSelective(tbContent,example);

        //更新内容时，删除redis中缓存数据。
        try {
            if(tbContent.getCategoryId() == REST_CID) {
                System.out.println("删除缓存了。");
                jedisClient.hdel(CONTENT_KEY, tbContent.getCategoryId() + "");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }

    /**
     * 根据categoryId查询内容
     * 主要是为了查询数据，展示前台的轮播图
     * @param categoryId
     * @return
     */
    @Override
    public List<TbContent> getContentListByCatId(Long categoryId) {
        //先从redis中查，如果没有再去数据库查
        try {
            String jsonStr = jedisClient.hget(CONTENT_KEY, categoryId + "");
            if (StringUtils.isNotBlank(jsonStr)) {
                System.out.println("有缓存！！！");
                return JsonUtils.jsonToList(jsonStr, TbContent.class);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(example);

        //返回之前将数据缓存到redis中
        try{
            System.out.println("没有缓存！！！");
            jedisClient.hset(CONTENT_KEY, categoryId+"", JsonUtils.objectToJson(list));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}
