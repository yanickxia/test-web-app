package io.daocloud.dmp.test.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/echo")
    @ApiOperation(value = "echo input body", response = String.class)
    public String echo(@RequestBody String input) {
        return input;
    }

    @GetMapping("/add")
    @ApiOperation(value = "calc a simple add", response = Long.class)
    public Long add(@ApiParam(value = "number a", required = true) @RequestParam("a") Long a,
                    @ApiParam(value = "number b", required = true) @RequestParam("b") Long b) {
        return a + b;
    }


    @PostMapping("/upload")
    @ApiOperation(value = "echo upload file", notes = "test chinese encoding file body")
    public HttpEntity<byte[]> upload(@ApiParam(value = "file", required = true) @RequestParam("file") MultipartFile file) throws Exception {

        byte[] documentBody = org.apache.commons.io.IOUtils.toByteArray(file.getInputStream());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType(file.getContentType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + URLEncoder.encode(file.getOriginalFilename().replace(" ", "_"), "UTF-8"));
        header.setContentLength(documentBody.length);

        return new HttpEntity<>(documentBody, header);
    }

    @GetMapping("/download")
    @ApiOperation(value = "download a file", notes = "test chinese encoding file body")
    public void downPhotoByStudentId(final HttpServletResponse response,
                                     @Value("classpath:一个文件.txt") Resource res) throws Exception {
        String fileName = "一个文件.txt";
        fileName = URLEncoder.encode(fileName, "UTF-8");

        byte[] data = IOUtils.toByteArray(res.getInputStream());

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @GetMapping("/exception")
    @ApiOperation(value = "just return a exception")
    public void exception() {
        throw new RuntimeException();
    }

    @RequestMapping(value = "/describe", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "describe your http request", notes = "这个接口没办法用Swagger解释，这个接口接受任何参数，请随意使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "any header value", value = "any header value", paramType = "header")
    })
    public Map<String, Object> describe(@RequestHeader HttpHeaders httpHeaders,
                                        @RequestBody(required = false) String body,
                                        @RequestParam Map<String, String> allRequestParams) {
        Map<String, Object> map = new HashMap<>();
        map.put("headers", httpHeaders);
        map.put("query", allRequestParams);
        map.put("body", body);
        return map;
    }

    @GetMapping("/status_code")
    @ApiOperation(value = "return a assign status code")
    public void statusCode(final HttpServletResponse response,
                           @ApiParam(value = "http code", required = true) @RequestParam("code") Integer code) {
        response.setStatus(code);
    }

}
