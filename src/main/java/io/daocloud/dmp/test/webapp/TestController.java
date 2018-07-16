package io.daocloud.dmp.test.webapp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/echo")
    public String echo(@RequestBody String input) {
        return input;
    }

    @RequestMapping("/add")
    public Long add(@RequestParam("a") Long a, @RequestParam("b") Long b) {
        return a + b;
    }

    @RequestMapping("/upload")
    public HttpEntity<byte[]> upload(@RequestParam("file") MultipartFile file) throws Exception {

        byte[] documentBody = org.apache.commons.io.IOUtils.toByteArray(file.getInputStream());


        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType(file.getContentType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + file.getOriginalFilename().replace(" ", "_"));
        header.setContentLength(documentBody.length);

        return new HttpEntity<byte[]>(documentBody, header);
    }
}
