package com.maxifier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RequestController {

    @Autowired
    private RequestExecutor executor;

    @GetMapping(path = "/similarity", produces = "application/json")
    public double getSimilarity(
            @RequestParam int page1, @RequestParam int page2,
            @RequestParam long from, @RequestParam long to) {
        return executor.getSimilarity(page1, page2, from, to);
    }

    @GetMapping(path = "/pages", produces = "application/json")
    public List<String> getPages() {
        return executor.getPages();
    }
}
