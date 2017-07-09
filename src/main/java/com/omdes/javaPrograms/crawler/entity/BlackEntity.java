package com.omdes.javaPrograms.crawler.entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/8
 * Time: 17:56
 */
public final class BlackEntity implements Serializable {
    private static final long serialVersionUID = 6656681835446113124L;

    private long id;
    private String name;
    private int count;
    private String notes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
