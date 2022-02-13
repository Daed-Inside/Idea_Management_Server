package web.application.IdeaManagement.model.request;

import lombok.Data;

@Data
public class MailRequest {
    public String sender;
    public String password;
    public String receiver;
    public String subject;
    public String content;
}
