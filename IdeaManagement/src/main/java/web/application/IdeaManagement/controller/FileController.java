package web.application.IdeaManagement.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.manager.CSVManager;
import web.application.IdeaManagement.manager.IdeaManager;
import web.application.IdeaManagement.model.response.ExcelExportResponse;
import web.application.IdeaManagement.utils.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Controller
@CrossOrigin("*")
@RequestMapping("/file")
public class FileController {
    @Autowired
    private CSVManager csvManager;
    @Autowired
    private IdeaManager ideaManager;

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

    @GetMapping("/report")
    public void downloadTest(@RequestParam("year") String year,
                             @RequestParam(value = "semester", required = false) String semester,
                             @RequestParam(value = "department", required = false) Long department,
                             @RequestParam(value = "topic", required = false) Long topic,
                             HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; file=customers.csv");
        try {
            List<ExcelExportResponse> listData = ideaManager.getExportData(year, semester, department, topic);
            csvManager.writeObjectToCSV(response.getWriter(), listData);
        } catch (Exception e) {

        }
    }

}
