package com.maxifier.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class RequestController {

    @GetMapping(path = "/similarity", produces = "application/json")
    public Map<String, String> getSimilarity(
            @RequestParam Map<String, String> parameters) {
        return parameters;
    }

    @GetMapping(path = "/pages", produces = "application/json")
    public List<Integer> getPages() {
        return Arrays.asList(1, 2, 3);
    }
}
