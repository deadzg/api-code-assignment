package com.smalwe.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalwe.api.bean.ProjectInfo;
import com.smalwe.api.bean.ProjectListApiResponse;
import com.smalwe.api.controller.GithubProjectInfoController;
import io.swagger.v3.core.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubProjectInfoService {

    Logger logger = LoggerFactory.getLogger(GithubProjectInfoService.class);

    private static final String GITHUB_SEARCH_REPO_URL = "https://api.github.com/search/repositories";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public ProjectListApiResponse getProjectList (String language, Integer pageSize, Integer pageNum){

        List<ProjectInfo> projectList = new ArrayList<>();
        String githubProjectsObj = restTemplate.getForObject(constructFullUrl(language, pageSize, pageNum), String.class);
        JsonNode actualObj;
        try {
            actualObj = objectMapper.readTree(githubProjectsObj);
        } catch (JsonProcessingException jpe) {
            logger.error("Unable to process the github result", jpe.getMessage());
            throw new RuntimeException("Unable to fetch the results");
        }

        JsonNode githubProjects = actualObj.get("items");
        Integer totalCount = actualObj.get("total_count").asInt();
        if(githubProjects.isArray()) {
            logger.debug("Total Projects: {}", githubProjects.size());
            for(JsonNode project : githubProjects) {
                ProjectInfo projectInfo = new ProjectInfo(
                            project.get("id").asInt(),
                            project.get("name").asText("No data"),
                            project.get("html_url").asText("No data"),
                            project.get("owner").get("login").asText("No data")
                        );
                //TODO: Add exception
                projectList.add(projectInfo);
            }
        } else {
            throw new RuntimeException("Unexpected results from github APIs");
        }
        return new ProjectListApiResponse(projectList, totalCount, pageSize, pageNum);
    }

    private String constructFullUrl(String language, Integer pageSize, Integer pageNum) {
        return UriComponentsBuilder
                .fromUriString(GITHUB_SEARCH_REPO_URL)
                .queryParam("q", "language:"+language)
                .queryParam("per_page", pageSize)
                .queryParam("page", pageNum)
                .build().toString();
    }
}
