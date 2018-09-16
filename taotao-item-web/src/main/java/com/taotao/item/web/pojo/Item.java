package com.taotao.item.web.pojo;

import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public class Item extends TbItem {

    public Item(TbItem tbItem) {
        BeanUtils.copyProperties(tbItem,this);
    }

    public String[] getImages() {
        if(StringUtils.isNotBlank(super.getImage())) {
            return super.getImage().split(",");
        }
        return null;
    }
}
