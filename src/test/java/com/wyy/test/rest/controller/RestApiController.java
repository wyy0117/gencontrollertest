package com.wyy.test.rest.controller;

import com.wyy.test.rest.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
@RestController
public class RestApiController {

//    @GetMapping("hello")
//    public String hello(@RequestParam String a, @RequestParam String b) {
//        return "hello";
//    }
//
//    @DeleteMapping(value = "hello1/{id}")
//    public String hello1(@PathVariable("id") long id) {
//        return "11";
//    }
//
//    @PostMapping(value = "hello2")
//    public void hello2(@RequestBody UserDTO dto) {
//
//    }
//
//    @PostMapping("hello3")
//    public void hello3(@ModelAttribute UserDTO dto, MultipartFile file) {
//
//    }

    @PostMapping(value = "hello4")
    public void hello4(@RequestBody List<UserDTO> dto) {

    }

}
