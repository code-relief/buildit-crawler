package com.buildit.exercise.crawler.controller;

import com.buildit.exercise.crawler.model.BasicResponse;
import com.buildit.exercise.crawler.model.Result;
import com.buildit.exercise.crawler.service.Crawler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Api(description = "Web Crawler")
@RestController
@Validated
public class CrawlerController {

    private final Crawler internalLinksCrawlerService;

    public CrawlerController(final Crawler internalLinksCrawlerService) {
        this.internalLinksCrawlerService = internalLinksCrawlerService;
    }

    @PostMapping(value = "/crawl", produces = {"application/json"})
    @ApiOperation(value = "Web crawnel endpoint", notes = "Site map internal links builder", response = Result.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Site map", response = Result.class),
            @ApiResponse(code = 400, message = "The service doesn't understand the request.", response = BasicResponse.class),
            @ApiResponse(code = 500, message = "The service stopped request processing due to its internal problems.", response = BasicResponse.class) })
    public ResponseEntity<Result> crawlerEndpoint(
            @RequestBody
            @NotBlank
            @Pattern(regexp="^(https?)://[a-zA-Z0-9][a-zA-Z0-9\\.&\\?#/%=:\\-_]*")
            final String url) {
        Result result = internalLinksCrawlerService.crawl(url);
        result.setUrl(url);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
