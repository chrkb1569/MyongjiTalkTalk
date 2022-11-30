package com.example.demo.controller.crawling;

import com.example.demo.response.Response;
import com.example.demo.service.crawling.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CrawlingController {

    private final CrawlingService crawlingService;

    @GetMapping("/notice")
    public Response getNotice() {
        return Response.success(crawlingService.getNotice());
    }

    @GetMapping("/schedule")
    public Response getSchedule() {
        return Response.success(crawlingService.getSchedule());
    }
}
