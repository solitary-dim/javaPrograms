package com.omdes.javaPrograms.crawler.entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/8
 * Time: 15:21
 */
public final class URLQueryCondition implements Serializable {
    private static final long serialVersionUID = -6467353653786578701L;

    // 表名
    private String tableName;
    // 备注 Page-URL；Image-src
    private String notes;
    // 待访问url所在层次
    private Long level;
    // 每次分页查询的起始
    private Integer pageStart;
    // 每次分页查询数量
    private Integer pageSize;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
