package web.application.IdeaManagement.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class ExcelExportResponse {
    private String semester;
    private String department;
    private String topic;
    private String tag;
    private String finalClosure;
    private String idea;
    private String author;
    private String email;
    private String ideaAttachment;

    public ExcelExportResponse(String semester, String department, String topic, String tag, Date finalClosure,
                               String idea, String author, String email, String ideaAttachment) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        this.semester = semester;
        this.department = department;
        this.topic = topic;
        this.tag = tag;
        this.finalClosure = formatter.format(finalClosure);
        this.idea = idea;
        this.author = author;
        this.email = email;
        this.ideaAttachment = ideaAttachment;
    }
}
