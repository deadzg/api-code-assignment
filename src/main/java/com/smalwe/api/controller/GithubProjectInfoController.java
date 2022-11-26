package com.smalwe.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smalwe.api.bean.ProjectInfo;
import com.smalwe.api.bean.ProjectListApiResponse;
import com.smalwe.api.service.GithubProjectInfoService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/projects")
public class GithubProjectInfoController {

    Logger logger = LoggerFactory.getLogger(GithubProjectInfoController.class);

    @Autowired
    private GithubProjectInfoService githubProjectInfoService;

    @Operation(summary = "Get list of projects in Github for a specified programming language")
    @RequestMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ProjectListApiResponse getProjectList(@RequestParam String language,
                                            @RequestParam(defaultValue="30") Integer pageSize,
                                            @RequestParam(defaultValue="1") Integer pageNum) {
        logger.info("Request GET Project List");
        return githubProjectInfoService.getProjectList(language, pageSize, pageNum);
    }
}
