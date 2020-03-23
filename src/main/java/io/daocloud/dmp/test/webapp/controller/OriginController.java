package io.daocloud.dmp.test.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class OriginController {
    @GetMapping("/simple-cors")
    public String simpleCors() {
        return "ok";
    }

    @PostMapping("/cors")
    public String cors() {
        return "ok";
    }
}
