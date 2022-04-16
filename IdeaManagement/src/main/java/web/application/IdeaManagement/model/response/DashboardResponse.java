package web.application.IdeaManagement.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardResponse {
    private Long totalIdea;
    private Long ideaNoComment;
    private Long anonymousIdea;
    private Long anonymousComment;
    private FirstBarChartResponse firstBarChart;
    private PieChartResponse pieChart;
    private SecondBarChartResponse secondBarChar;

    public DashboardResponse(Long totalIdea, Long anonymousIdea) {
        this.totalIdea = totalIdea;
        this.anonymousIdea = anonymousIdea;
    }
}
