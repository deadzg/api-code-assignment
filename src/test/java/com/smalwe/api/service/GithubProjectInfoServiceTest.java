package com.smalwe.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalwe.api.bean.ProjectListApiResponse;
import com.smalwe.api.security.ApiKeyAuthManager;
import com.smalwe.api.security.RestAuthenticationEntryPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class GithubProjectInfoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GithubProjectInfoService githubProjectInfoService;

    private static final String GITHUB_VALID_TEST_RESPONSE = "{\"total_count\":12236181,\"incomplete_results\":true,\"items\":[{\"id\":132464395,\"node_id\":\"MDEwOlJlcG9zaXRvcnkxMzI0NjQzOTU=\",\"name\":\"JavaGuide\",\"full_name\":\"Snailclimb/JavaGuide\",\"private\":false,\"owner\":{\"login\":\"Snailclimb\",\"id\":29880145,\"node_id\":\"MDQ6VXNlcjI5ODgwMTQ1\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/29880145?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/Snailclimb\",\"html_url\":\"https://github.com/Snailclimb\",\"followers_url\":\"https://api.github.com/users/Snailclimb/followers\",\"following_url\":\"https://api.github.com/users/Snailclimb/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/Snailclimb/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/Snailclimb/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/Snailclimb/subscriptions\",\"organizations_url\":\"https://api.github.com/users/Snailclimb/orgs\",\"repos_url\":\"https://api.github.com/users/Snailclimb/repos\",\"events_url\":\"https://api.github.com/users/Snailclimb/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/Snailclimb/received_events\",\"type\":\"User\",\"site_admin\":false},\"html_url\":\"https://github.com/Snailclimb/JavaGuide\",\"description\":\"「Java学习+面试指南」一份涵盖大部分Java程序员所需要掌握的核心知识。准备Java面试，首选JavaGuide！\",\"fork\":false,\"url\":\"https://api.github.com/repos/Snailclimb/JavaGuide\",\"forks_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/forks\",\"keys_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/keys{/key_id}\",\"collaborators_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/collaborators{/collaborator}\",\"teams_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/teams\",\"hooks_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/hooks\",\"issue_events_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/issues/events{/number}\",\"events_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/events\",\"assignees_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/assignees{/user}\",\"branches_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/branches{/branch}\",\"tags_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/tags\",\"blobs_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/blobs{/sha}\",\"git_tags_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/tags{/sha}\",\"git_refs_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/refs{/sha}\",\"trees_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/trees{/sha}\",\"statuses_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/statuses/{sha}\",\"languages_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/languages\",\"stargazers_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/stargazers\",\"contributors_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/contributors\",\"subscribers_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/subscribers\",\"subscription_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/subscription\",\"commits_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/commits{/sha}\",\"git_commits_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/commits{/sha}\",\"comments_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/comments{/number}\",\"issue_comment_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/issues/comments{/number}\",\"contents_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/contents/{+path}\",\"compare_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/compare/{base}...{head}\",\"merges_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/merges\",\"archive_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/{archive_format}{/ref}\",\"downloads_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/downloads\",\"issues_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/issues{/number}\",\"pulls_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/pulls{/number}\",\"milestones_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/milestones{/number}\",\"notifications_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/notifications{?since,all,participating}\",\"labels_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/labels{/name}\",\"releases_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/releases{/id}\",\"deployments_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/deployments\",\"created_at\":\"2018-05-07T13:27:00Z\",\"updated_at\":\"2022-11-26T05:09:37Z\",\"pushed_at\":\"2022-11-22T14:35:49Z\",\"git_url\":\"git://github.com/Snailclimb/JavaGuide.git\",\"ssh_url\":\"git@github.com:Snailclimb/JavaGuide.git\",\"clone_url\":\"https://github.com/Snailclimb/JavaGuide.git\",\"svn_url\":\"https://github.com/Snailclimb/JavaGuide\",\"homepage\":\"https://javaguide.cn\",\"size\":157692,\"stargazers_count\":128934,\"watchers_count\":128934,\"language\":\"Java\",\"has_issues\":true,\"has_projects\":true,\"has_downloads\":true,\"has_wiki\":true,\"has_pages\":false,\"has_discussions\":true,\"forks_count\":42925,\"mirror_url\":null,\"archived\":false,\"disabled\":false,\"open_issues_count\":63,\"license\":null,\"allow_forking\":true,\"is_template\":false,\"web_commit_signoff_required\":false,\"topics\":[\"algorithms\",\"interview\",\"java\",\"jvm\",\"mysql\",\"redis\",\"spring\",\"system\",\"system-design\",\"zookeeper\"],\"visibility\":\"public\",\"forks\":42925,\"open_issues\":63,\"watchers\":128934,\"default_branch\":\"main\",\"score\":1.0}]}";

    private static final String GITHUB_INVALID_RESPONSE_WITH_NO_ARRAY = "{\"total_count\":12236181,\"incomplete_results\":true,\"items\":{\"id\":132464395,\"node_id\":\"MDEwOlJlcG9zaXRvcnkxMzI0NjQzOTU=\",\"name\":\"JavaGuide\",\"full_name\":\"Snailclimb/JavaGuide\",\"private\":false,\"owner\":{\"login\":\"Snailclimb\",\"id\":29880145,\"node_id\":\"MDQ6VXNlcjI5ODgwMTQ1\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/29880145?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/Snailclimb\",\"html_url\":\"https://github.com/Snailclimb\",\"followers_url\":\"https://api.github.com/users/Snailclimb/followers\",\"following_url\":\"https://api.github.com/users/Snailclimb/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/Snailclimb/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/Snailclimb/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/Snailclimb/subscriptions\",\"organizations_url\":\"https://api.github.com/users/Snailclimb/orgs\",\"repos_url\":\"https://api.github.com/users/Snailclimb/repos\",\"events_url\":\"https://api.github.com/users/Snailclimb/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/Snailclimb/received_events\",\"type\":\"User\",\"site_admin\":false},\"html_url\":\"https://github.com/Snailclimb/JavaGuide\",\"description\":\"「Java学习+面试指南」一份涵盖大部分Java程序员所需要掌握的核心知识。准备Java面试，首选JavaGuide！\",\"fork\":false,\"url\":\"https://api.github.com/repos/Snailclimb/JavaGuide\",\"forks_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/forks\",\"keys_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/keys{/key_id}\",\"collaborators_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/collaborators{/collaborator}\",\"teams_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/teams\",\"hooks_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/hooks\",\"issue_events_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/issues/events{/number}\",\"events_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/events\",\"assignees_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/assignees{/user}\",\"branches_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/branches{/branch}\",\"tags_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/tags\",\"blobs_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/blobs{/sha}\",\"git_tags_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/tags{/sha}\",\"git_refs_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/refs{/sha}\",\"trees_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/trees{/sha}\",\"statuses_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/statuses/{sha}\",\"languages_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/languages\",\"stargazers_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/stargazers\",\"contributors_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/contributors\",\"subscribers_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/subscribers\",\"subscription_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/subscription\",\"commits_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/commits{/sha}\",\"git_commits_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/git/commits{/sha}\",\"comments_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/comments{/number}\",\"issue_comment_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/issues/comments{/number}\",\"contents_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/contents/{+path}\",\"compare_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/compare/{base}...{head}\",\"merges_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/merges\",\"archive_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/{archive_format}{/ref}\",\"downloads_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/downloads\",\"issues_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/issues{/number}\",\"pulls_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/pulls{/number}\",\"milestones_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/milestones{/number}\",\"notifications_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/notifications{?since,all,participating}\",\"labels_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/labels{/name}\",\"releases_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/releases{/id}\",\"deployments_url\":\"https://api.github.com/repos/Snailclimb/JavaGuide/deployments\",\"created_at\":\"2018-05-07T13:27:00Z\",\"updated_at\":\"2022-11-26T05:09:37Z\",\"pushed_at\":\"2022-11-22T14:35:49Z\",\"git_url\":\"git://github.com/Snailclimb/JavaGuide.git\",\"ssh_url\":\"git@github.com:Snailclimb/JavaGuide.git\",\"clone_url\":\"https://github.com/Snailclimb/JavaGuide.git\",\"svn_url\":\"https://github.com/Snailclimb/JavaGuide\",\"homepage\":\"https://javaguide.cn\",\"size\":157692,\"stargazers_count\":128934,\"watchers_count\":128934,\"language\":\"Java\",\"has_issues\":true,\"has_projects\":true,\"has_downloads\":true,\"has_wiki\":true,\"has_pages\":false,\"has_discussions\":true,\"forks_count\":42925,\"mirror_url\":null,\"archived\":false,\"disabled\":false,\"open_issues_count\":63,\"license\":null,\"allow_forking\":true,\"is_template\":false,\"web_commit_signoff_required\":false,\"topics\":[\"algorithms\",\"interview\",\"java\",\"jvm\",\"mysql\",\"redis\",\"spring\",\"system\",\"system-design\",\"zookeeper\"],\"visibility\":\"public\",\"forks\":42925,\"open_issues\":63,\"watchers\":128934,\"default_branch\":\"main\",\"score\":1.0}}";
    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGetProjectInfoList_Valid() throws JsonProcessingException {
        ObjectMapper tobj = new ObjectMapper();
        JsonNode testGithubRespObj = tobj.readTree(GITHUB_VALID_TEST_RESPONSE);
        Mockito.when(restTemplate.getForObject("https://api.github.com/search/repositories?q=language:java&per_page=1&page=1", String.class)).thenReturn(GITHUB_VALID_TEST_RESPONSE);
        Mockito.when(objectMapper.readTree(ArgumentMatchers.anyString())).thenReturn(testGithubRespObj);
        ProjectListApiResponse projectListApiResponse = githubProjectInfoService.getProjectList("java", 1, 1);
        assertEquals(projectListApiResponse.getTotalCount(), 12236181);
    }

    @Test
    public void testGetProjectInfoList_RTException() throws JsonProcessingException {

        Mockito.when(restTemplate.getForObject("https://api.github.com/search/repositories?q=language:java&per_page=1&page=1", String.class)).thenReturn(GITHUB_VALID_TEST_RESPONSE);
        Mockito.when(objectMapper.readTree(ArgumentMatchers.anyString())).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> githubProjectInfoService.getProjectList("java", 1, 1));
    }

    @Test
    public void testGetProjectInfoList_NotArray() throws JsonProcessingException {
        ObjectMapper tobj = new ObjectMapper();
        JsonNode testGithubRespObj = tobj.readTree(GITHUB_INVALID_RESPONSE_WITH_NO_ARRAY);
        Mockito.when(restTemplate.getForObject("https://api.github.com/search/repositories?q=language:java&per_page=1&page=1", String.class)).thenReturn(GITHUB_VALID_TEST_RESPONSE);
        Mockito.when(objectMapper.readTree(ArgumentMatchers.anyString())).thenReturn(testGithubRespObj);
        Assertions.assertThrows(RuntimeException.class, () -> githubProjectInfoService.getProjectList("java", 1, 1));
    }
}
