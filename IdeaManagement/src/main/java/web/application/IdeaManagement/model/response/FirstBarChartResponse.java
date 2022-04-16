package web.application.IdeaManagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstBarChartResponse {
    private List<String> listDept;
    private List<Long> listIdea;
    private List<Long> listComment;
}
