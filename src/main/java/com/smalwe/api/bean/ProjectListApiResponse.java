package com.smalwe.api.bean;

import java.util.List;

public class ProjectListApiResponse {

    private Integer totalCount;
    private Integer pageSize;
    private Integer pageNum;
    private List<ProjectInfo> projectInfoList;


    public ProjectListApiResponse(
            List<ProjectInfo> projectInfoList, Integer totalCount, Integer pageSize, Integer pageNum) {
        this.projectInfoList = projectInfoList;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public List<ProjectInfo> getProjectInfoList() {
        return projectInfoList;
    }

    public void setProjectInfoList(List<ProjectInfo> projectInfoList) {
        this.projectInfoList = projectInfoList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
