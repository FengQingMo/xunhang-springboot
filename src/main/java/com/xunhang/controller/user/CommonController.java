package com.xunhang.controller.user;


import com.xunhang.common.result.Result;
import com.xunhang.pojo.entity.User;
import com.xunhang.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
@Api(value = "公共接口")
@Slf4j
public class CommonController {


    @Autowired
    private AliOssUtil aliOssUtil;


    @PostMapping("/upload/{dir}")
    public Result  uploadFile(MultipartFile file,@PathVariable("dir") String dir){
        log.info("Uploading file");
        String url = aliOssUtil.upload(file,dir);
        User user = new User();
        return Result.success(url);
    }

}
