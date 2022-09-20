package com.dhnns.navermaptest;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Documents {

    @Expose
    private List<Document> documents;
    @Expose
    private Meta meta;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
