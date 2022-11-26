package com.smalwe.api.bean;


//project id, name, url and the owner login
public class ProjectInfo {

    private Integer projectId;
    private String projectName;
    private String projectUrl;
    private String ownerLogin;


    public ProjectInfo(Integer projectId, String projectName, String projectUrl, String ownerLogin) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectUrl = projectUrl;
        this.ownerLogin = ownerLogin;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }
}
