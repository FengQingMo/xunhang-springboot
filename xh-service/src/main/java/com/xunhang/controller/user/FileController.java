package com.xunhang.controller.user;

import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.pojo.vo.UploadImageVO;
import com.xunhang.service.FileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器层
 */
@Api("文件控制器层")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * @param file     图片
     * @param filePath 传的目录,如头像传到headImage,物品图片传到itemImage
     * @return
     */
    @PostMapping("/uploadImage/{filePath}")
    public Result<UploadImageVO> uploadImage(MultipartFile file, @PathVariable("filePath") String filePath) {
        return ResultUtils.success(fileService.uploadImage(file, filePath));
    }

}