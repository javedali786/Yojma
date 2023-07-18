package com.tv.beanModel.responseModels.detailPlayer;

import java.io.Serializable;

public class Series implements Serializable {
    private Object vodCount;
    private Object seasonCount;
    private Object name;
    private Object description;
    private Object id;
    private Object picture;
    private Object status;

    public Object getVodCount() {
        return vodCount;
    }

    public void setVodCount(Object vodCount) {
        this.vodCount = vodCount;
    }

    public Object getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(Object seasonCount) {
        this.seasonCount = seasonCount;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "Series{" +
                        "vodCount = '" + vodCount + '\'' +
                        ",seasonCount = '" + seasonCount + '\'' +
                        ",name = '" + name + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
