package web.application.IdeaManagement.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdeaStatusCountResponse {
    private Long like;
    private Long dislike;
    private Long viewCount;
    private Integer currentStatus;
    private Long commentCount;

    public IdeaStatusCountResponse(Long like, Long dislike, Long viewCount) {
        this.like = like;
        this.dislike = dislike;
        this.viewCount = viewCount;
    }
}
