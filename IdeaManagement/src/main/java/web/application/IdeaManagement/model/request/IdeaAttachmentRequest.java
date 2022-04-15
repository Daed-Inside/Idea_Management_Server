package web.application.IdeaManagement.model.request;

import lombok.Data;

@Data
public class IdeaAttachmentRequest {
    private String fileName;
    private String downloadUrl;
    private String filePath;
}
