package org.projw.blackserver.extensions.poll;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.StringUtils;
import org.projw.blackserver.config.json.Views;

public class PollResult {
    private Long id;
    private String content;
    private String result;

    public PollResult(Poll poll) {
        id = poll.getId();
        content = poll.getContent();
        int optionCount = StringUtils.countMatches(content, ",") + 1;
        int[] r = new int[optionCount];
        for (Vote v:poll.getVotes()) {
            r[v.getChoice()] ++;
        }
        result= "" + r[0];
        for (int i=1;i<r.length;i++) {
            result += "," + r[i];
        }
    }

    @JsonView(Views.Post.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Post.class)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonView(Views.Post.class)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
