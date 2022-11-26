package com.smalwe.api;

import com.smalwe.api.controller.GithubProjectInfoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GithubProjectInfoApplicationTest {

    @Autowired
    GithubProjectInfoController githubProjectInfoController;

    @Test
    public void contextLoads() {

    }
}
