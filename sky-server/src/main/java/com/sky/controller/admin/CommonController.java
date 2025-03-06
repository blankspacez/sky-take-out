package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    private static final String PREFIX = "http://localhost/images/";

    @Value("${nginx.url}")
    private String NGINX_URL;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传:{}",file);
        try {
            String originalFilename = file.getOriginalFilename();
            //生成唯一文件名
            String fileName = UUID.randomUUID().toString().replace("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));
            //转存文件至nginx
            file.transferTo(new File(NGINX_URL+fileName));

            String url = PREFIX + fileName;
            log.info("文件上传成功:{}",url);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败:{}",e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}
