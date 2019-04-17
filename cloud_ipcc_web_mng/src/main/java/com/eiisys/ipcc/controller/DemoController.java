package com.eiisys.ipcc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eiisys.ipcc.bean.GenericResponse;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.core.utils.fastdfs.FastDFSClient;
import com.eiisys.ipcc.service.IpccDemoService;
import com.google.common.base.Throwables;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "免费开通接口API", tags = {
                                   "后台接口：Demo接口API" }, description = "需要关联控制台审核状态")
@Slf4j
@RestController
public class DemoController {

    @Value("${fastdfs.file_access_url}")
    private String fileAccessUrl;

    @Value("${common.upload.image.ext}")
    private String imageExt;

    @Autowired
    RedisClient redisClient;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    IpccDemoService ipccDemoService;

    @ApiOperation(value = "测试controller接口--上传图片到fastDFS", notes = "测试测试测试测试测试", response = GenericResponse.class)
    @PostMapping(path = "uploadImage")
    public GenericResponse uploadImage(@RequestParam(value = "bizLicensePic") MultipartFile uploadFile)
            throws Exception {
        GenericResponse genericResponse = null;

        InputStream fileStream = null;
        String extName = getFileExtName(uploadFile.getOriginalFilename());
        if (extName == null || !imageExt.contains(extName)) {
            genericResponse = ResponseUtils.genErrorResponse(MsgConstants.MSG_IMG_FORMAT_ERR, imageExt);
        }

        if (genericResponse != null) {
            return genericResponse;
        }

        if (uploadFile != null && !uploadFile.isEmpty()) {

            try {
                fileStream = uploadFile.getInputStream();

                String fileName = System.currentTimeMillis() + "_" + uploadFile.getOriginalFilename();
                String picPath = fastDFSClient.upload(fileStream, fileName, null);

                genericResponse = ResponseUtils.genSuccessResponse(null);

                Map<String, String> mapData = new HashMap<>();
                mapData.put("picPath", picPath);
                genericResponse.setMapData(mapData);
            } finally {

                try {
                    if (fileStream != null) {
                        fileStream.close();
                    }
                } catch (IOException e) {
                    log.error("关闭文件流异常: " + Throwables.getStackTraceAsString(e));
                }
            }

        }

        return genericResponse;
    }

    @ApiOperation(value = "测试controller接口--从fastDFS下载图片", notes = "测试测试测试测试测试", response = GenericResponse.class)
    @PostMapping(path = "downloadImage")
    public GenericResponse downloadImage(@RequestParam("picPath") String picPath, HttpServletResponse response)
            throws Exception {
        GenericResponse genericResponse = null;

        OutputStream os = null;

        if (StringUtils.isNotBlank(picPath)) {

            try {
                String fileName = "bizLicense";

                byte[] picByte = fastDFSClient.download(picPath);

                if (picByte == null) {
                    genericResponse = ResponseUtils.genErrorResponse("510208");
                    return genericResponse;
                }

                // response.setContentType("image/jpeg");
                // response.addHeader("Content-Disposition", "inline; filename=\"" + fileName
                // +".jpg\"");
                //
                // os = response.getOutputStream();
                // os.write(picByte);
                // os.flush();
                // String base64Image = Base64Utils.encodeToString(picByte);

                genericResponse = ResponseUtils.genSuccessResponse(null);

                Map<String, Object> mapData = new HashMap<>();
                mapData.put("imageName", fileName + ".jpg");
                // mapData.put("imageData", base64Image);
                mapData.put("imageUrl", fileAccessUrl + picPath);

                genericResponse.setMapData(mapData);
            } finally {

            }
        }

        return genericResponse;
    }

    private String getFileExtName(String fileName) {
        String extName = null;

        if (!StringUtils.isEmpty(fileName) && fileName.matches("^.+\\.\\w+$")) {
            extName = fileName.split("\\.")[1];
        }

        return extName;
    }
}
