package com.smalwe.api.controller;

import com.smalwe.api.bean.ProjectInfo;
import com.smalwe.api.bean.ProjectListApiResponse;
import com.smalwe.api.exception.UnProcessableEntityException;
import com.smalwe.api.security.ApiKeyAuthManager;
import com.smalwe.api.security.RestAuthenticationEntryPoint;
import com.smalwe.api.service.GithubProjectInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GithubProjectInfoController.class)
public class GithubProjectInfoControllerTest {

    private static final String VALID_API_KEY = "apiKey1";
    private static final String INVALID_API_KEY = "invalidapiKey1";

    @MockBean GithubProjectInfoService githubProjectInfoService;

    @Autowired MockMvc mockMvc;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGetProjectList_ValidRequest() throws Exception {

        ProjectInfo projectInfo = new ProjectInfo(12983151,
                                                "openhab1-addons",
                                                    "https://github.com/openhab",
                                        "openhab");
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(projectInfo);

        ProjectListApiResponse projectListApiResponse = new ProjectListApiResponse(projectInfoList, 1,30, 1);

        Mockito.when(githubProjectInfoService.getProjectList("Java",30,1)).thenReturn(projectListApiResponse);

        mockMvc.perform(get("/v1/projects?language=Java").
                accept(MediaType.APPLICATION_JSON).
                header("API_KEY", VALID_API_KEY)).
                andExpect(status().isOk());
    }

    @Test
    public void testGetProjectList_InValidCreds() throws Exception {

        ProjectInfo projectInfo = new ProjectInfo(12983151,
                "openhab1-addons",
                "https://github.com/openhab",
                "openhab");
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(projectInfo);

        ProjectListApiResponse projectListApiResponse = new ProjectListApiResponse(projectInfoList, 1,30, 1);

        Mockito.when(githubProjectInfoService.getProjectList("Java",30,1)).thenReturn(projectListApiResponse);

        mockMvc.perform(get("/v1/projects?language=Java").
                accept(MediaType.APPLICATION_JSON).
                header("API_KEY", INVALID_API_KEY)).
                andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetProjectList_BadRequest() throws Exception {
        ProjectInfo projectInfo = new ProjectInfo(12983151,
                "openhab1-addons",
                "https://github.com/openhab",
                "openhab");
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(projectInfo);

        ProjectListApiResponse projectListApiResponse = new ProjectListApiResponse(projectInfoList, 1,30, 1);

        Mockito.when(githubProjectInfoService.getProjectList("invalidLang",30,1)).thenThrow(new UnProcessableEntityException());

        mockMvc.perform(get("/v1/projects?language=invalidLang").
                accept(MediaType.APPLICATION_JSON).
                header("API_KEY", VALID_API_KEY)).
                andExpect(status().isBadRequest());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
            return new RestAuthenticationEntryPoint();
        }

        @Bean
        public ApiKeyAuthManager apiKeyAuthManager() {
            return new ApiKeyAuthManager(Arrays.asList(VALID_API_KEY));
        }

    }
}
