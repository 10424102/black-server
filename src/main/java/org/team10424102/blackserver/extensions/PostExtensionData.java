package org.team10424102.blackserver.extensions;

import com.fasterxml.jackson.annotation.JsonView;
import org.team10424102.blackserver.config.json.Views;

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
