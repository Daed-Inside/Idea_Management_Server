package web.application.IdeaManagement.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryReponse {
    private Long id;
    private String category;
    private String topic;

    public CategoryReponse(Long id, String category, String topic) {
        this.id = id;
        this.category = category;
        this.topic = topic;
    }
}
