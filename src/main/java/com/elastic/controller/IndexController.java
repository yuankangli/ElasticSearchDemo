package com.elastic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : liyk
 * @version 1.0
 * @date : 2020/6/19 8:49
 */
@RestController
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "ElasticSearch Demo Start Success";
    }
}
