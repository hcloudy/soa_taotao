package com.taotao.common.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureResult implements Serializable{

    private int error;
    private String url;
    private String message;
}
