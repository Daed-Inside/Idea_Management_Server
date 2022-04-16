package web.application.IdeaManagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondBarChartResponse {
    List<String> listDept;
    List<Long> userCount;
}
