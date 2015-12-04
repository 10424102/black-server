package com.example.services.extensions;

import com.example.config.json.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class PostExtensionData {
    private String id;

    private Object data;

    public PostExtensionData(String id, Object data) {
        this.id = id;
        this.data = data;
    }

    @JsonView(Views.Post.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonView(Views.Post.class)
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
