package com.lostAndFind.project.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.lostAndFind.project.Utils.FileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.synth.Region;
import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    // 设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "LndfGvXBOrPkpRTOET6ieGO7qDR6C27Db_k_L_-Y";
    String SECRET_KEY = "DLFIpzHQ0_6FWWbueeZ4Ki7be2vt34cVlZVE6Dts";
    // 要上传的空间
    String bucketname = "swzlpt";

    // 密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
    Configuration cfg = new Configuration(Zone.zone2());
    // ...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);

    private static String QINIU_IMAGE_DOMAIN = "http://cdn.djkyyds.fun/";

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            // 判断是否是合法的文件后缀
            if (!FileUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            // 调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
    //删除文件
    public boolean delete(String url){
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //此时url为全路径，需要将外链域名去掉，七牛云云端删除时只需要提供文件名即可
        //https://qiniuyun.xzr.blue/9cb077ef572f49948f0dda60ed850a9d.jpg
        // 截取最后一个指定字符串(此处为"/")之后的字符串。 此处fileName="9cb077ef572f49948f0dda60ed850a9d.jpg"
        String fileName = StringUtils.substringAfterLast(url, "/");
        try {
            bucketManager.delete(bucketname, fileName);
            return true;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return false;
    }
}
