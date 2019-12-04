package io.daocloud.dmp.test.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Value("${system.pause}")
    private Integer pauseSec;

    private final AtomicLong counter = new AtomicLong(0);

    @PostMapping("/echo")
    public String echo(@RequestBody String input) {
        return input;
    }

    @GetMapping("/add")
    public Long add(@RequestParam("a") Long a,
                    @RequestParam("b") Long b) throws InterruptedException {

        LOGGER.info("calls {}", counter.incrementAndGet());
        Thread.sleep(TimeUnit.SECONDS.toMillis(pauseSec));

        return a + b;
    }


    @PostMapping("/upload")
    public HttpEntity<byte[]> upload(@RequestParam("file") MultipartFile file) throws Exception {

        byte[] documentBody = org.apache.commons.io.IOUtils.toByteArray(file.getInputStream());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType(file.getContentType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + URLEncoder.encode(file.getOriginalFilename().replace(" ", "_"), "UTF-8"));
        header.setContentLength(documentBody.length);

        return new HttpEntity<>(documentBody, header);
    }

    @PostMapping("/custom-code")
    public ResponseEntity upload(@RequestParam("code") Integer code) throws Exception {
        return ResponseEntity.status(code).build();
    }


    @PostMapping("/upload_without_echo")
    public void upload_without_echo(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] documentBody = org.apache.commons.io.IOUtils.toByteArray(file.getInputStream());
        LOGGER.info("upload {} MB", documentBody.length / 1024 / 1024);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downPhotoByStudentId(@Value("classpath:一个文件.txt") Resource res) throws Exception {
        String fileName = "一个文件.txt";
        fileName = URLEncoder.encode(fileName, "UTF-8");

        byte[] data = IOUtils.toByteArray(res.getInputStream());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .header("Content-Length", "" + data.length)
                .contentType(MediaType.valueOf("application/octet-stream;charset=UTF-8"))
                .body(data);
    }

    @GetMapping("/exception")
    public void exception() {
        throw new RuntimeException();
    }

    @RequestMapping(value = "/describe", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> describe(@RequestHeader HttpHeaders httpHeaders,
                                        @RequestBody(required = false) String body,
                                        @RequestParam Map<String, String> allRequestParams) {
        Map<String, Object> map = new HashMap<>();
        map.put("headers", httpHeaders);
        map.put("query", allRequestParams);
        map.put("body", body);
        return map;
    }

    @RequestMapping("/size")
    public byte[] size(@RequestParam("size") Integer size) throws InterruptedException {
        byte[] s = new byte[1024 * size];
        Arrays.fill(s, (byte) 10);
        return s;
    }

    @RequestMapping("/block")
    public void block(@RequestParam("sec") Integer size) throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(size));
    }

    @PostMapping("/check-token")
    public void check(@RequestHeader HttpHeaders httpHeaders) {
        if ("123456".equals(httpHeaders.getFirst("x_token"))) {
            return;
        }

        throw new RuntimeException("illegae token");
    }

}
