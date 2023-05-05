package com.lostAndFind.project.controller;

import com.lostAndFind.project.Utils.QiniuUtil;
import com.lostAndFind.project.common.ErrorCode;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.exception.BusinessException;
import com.lostAndFind.project.qiniu.QiniuService;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DJK
 */
@RestController
@RequestMapping("/qiniu")
@Slf4j
public class QiniuController {

    @Resource
    private QiniuService qiniuService;

    @PostMapping("/uploadImg")
    @PermitAll
    public ResponseResult uploadImg(MultipartFile file) {

        if(file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        try {
            String fileUrl=qiniuService.saveImage(file);
            return new ResponseResult(200,"文件上传成功",fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }

    @RequestMapping(value = "/getToken")
    public @ResponseBody Map<String, String> getToken() throws IOException {
        String accessKey = "LndfGvXBOrPkpRTOET6ieGO7qDR6C27Db_k_L_-Y";
        String secretKey = "DLFIpzHQ0_6FWWbueeZ4Ki7be2vt34cVlZVE6Dts";
        String bucket = "swzlpt";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        Map<String, String> map = new HashMap<String, String>();
        map.put("uptoken", upToken);
        return map;
    }

    @PostMapping("/delete")
    public ResponseResult articleImgDel(@RequestHeader("Img-Delete")String url){
        if(url.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        qiniuService.delete(url);
        //删除云服务器文件
        return new ResponseResult(200,"文件删除成功");
    }
}


