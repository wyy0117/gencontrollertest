package com.wyy.test.rest.controller;

import com.wyy.test.rest.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
@RestController
public class RestApiController {

    @GetMapping("return-string")
    public String returnString(@RequestParam(defaultValue = "abc") String a, @RequestParam String b, @RequestParam(defaultValue = "1") int c) {
        return "hello";
    }

    @DeleteMapping(value = "path-parameter/{id}")
    public String pathParameter(@PathVariable("id") long id) {
        return "11";
    }

    @PostMapping(value = "test-post")
    public void testPost(@RequestBody UserDTO dto) {

    }

    @PostMapping(value = "list-body")
    public void listBody(@RequestBody List<UserDTO> dto) {

    }
    @PostMapping(value = "array-body")
    public void arrayBody(@RequestBody UserDTO[] dto) {

    }

    @PostMapping("attribute")
    public void attribute(@ModelAttribute UserDTO dto) {

    }

    @PostMapping("file")
    public void file(MultipartFile file) {

    }

    @PostMapping("attribute-file")
    public void attributeFile(@ModelAttribute UserDTO dto, MultipartFile file) {

    }

    @PostMapping(value = "generic-class")
    public void genericClass(@RequestBody List<UserDTO> dto) {

    }

    @PostMapping("files")
    public List<UserDTO> files(MultipartFile[] files) {
        return new ArrayList<>();
    }

    @GetMapping("return-generic")
    public Map<String, List<UserDTO>> returnGeneric() {
        return null;
    }

    @GetMapping("request-param")
    public long requestParam(@RequestParam long a, @RequestParam double b) {
        return 1L;
    }

    @GetMapping("default-value")
    public long defaultValue(@RequestParam(defaultValue = "233") long a, @RequestParam double b) {
        return 1L;
    }

    @GetMapping("alias")
    public long alias(@RequestParam(name = "id") long a, @RequestParam double b) {
        return 1L;
    }

}
