package com.brandon.accounts.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AccountsController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}
