package com.taotao.controller;

import com.taotao.common.pojo.PictureResult;
import com.taotao.common.util.FastDFSClient;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PictureController {

    /*@Autowired
    private PictureService pictureService;*/

    @Value("${IMAGE_SERVER_BASE_USL}")
    private String IMAGE_SERVER_BASE_USL;

    @RequestMapping(value = "/pic/upload",method = RequestMethod.POST)
    @ResponseBody
    public String uploadPic(MultipartFile uploadFile) {
        PictureResult pictureResult = new PictureResult();
        if(uploadFile.isEmpty()) {
            return JsonUtils.objectToJson(pictureResult);
        }
        try{
            //取图片扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            FastDFSClient fc = new FastDFSClient("classpath:properties/client.conf");
            String url = fc.uploadFile(uploadFile.getBytes(), extName);
            url = IMAGE_SERVER_BASE_USL + url;
            pictureResult.setError(0);
            pictureResult.setUrl(url);
        }catch (Exception e) {
            pictureResult.setError(1);
            pictureResult.setMessage("图片上传失败");
            e.printStackTrace();
        }
        return JsonUtils.objectToJson(pictureResult);
    }
}
