package web.application.IdeaManagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieChartResponse {
    List<String> listDept;
    List<Double> listValue;
}
