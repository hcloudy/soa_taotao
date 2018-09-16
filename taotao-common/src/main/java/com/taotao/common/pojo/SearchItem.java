package com.taotao.common.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchItem implements Serializable{

    private String id;
    private String title;
    private Long price;
    private String sell_point;
    private String image;
    private String category_name;
    private String item_desc;

    public String getImage() {
        if(null != image && !image.equals("")) {
            String[] strings = image.split(",");
            return strings[0];
        }
        return image;
    }

}
