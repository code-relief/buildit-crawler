package com.buildit.exercise.crawler.controller;

import com.buildit.exercise.crawler.model.BasicResponse;
import com.buildit.exercise.crawler.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Web Crawler")
@RestController
public class CrawlerController {

    @GetMapping(value = "/crawl/{url}", produces = {"application/json"})
    @ApiOperation(value = "Web crawnel endpoint", notes = "Site map internal links builder", response = Result.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Site map", response = Result.class),
            @ApiResponse(code = 400, message = "The service doesn't understand the request.", response = BasicResponse.class),
            @ApiResponse(code = 500, message = "The service stopped request processing due to its internal problems.", response = BasicResponse.class) })
    public ResponseEntity<Result> importEndpoint(@PathVariable final String url) {
        Result result = new Result();
        result.setUrl(url);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
