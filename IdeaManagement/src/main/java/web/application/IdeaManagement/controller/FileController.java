package web.application.IdeaManagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import web.application.IdeaManagement.utils.FileUtils;

import java.net.MalformedURLException;
import java.nio.file.Path;

@Slf4j
@Controller
@CrossOrigin("*")
@RequestMapping("/file")
public class FileController {
    @GetMapping("/download/{type}/{fileName:.+}")
    public ResponseEntity downloadFileFromDevice(@PathVariable String type,
                                                 @PathVariable String fileName) {
        Path path = FileUtils.getAttachmentFilePath(type, fileName);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "contract; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
