package web.application.IdeaManagement.model.response;

import lombok.Data;

@Data
public class FileResponse {
    private String fileType;
    private String thumbnail;
    private String original;
    private String downloadUrl;
    private String fileName;
}
