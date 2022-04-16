package web.application.IdeaManagement.manager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.model.response.ExcelExportResponse;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Service
public class CSVManager {
    public static void writeObjectToCSV(PrintWriter writer, List<ExcelExportResponse> listData) {
        try (
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("Semester", "Department", "Topic", "Tag", "Final Closure",
                                "Idea", "Author", "Email", "Idea Attachment"));
        ) {
            for (ExcelExportResponse data : listData) {
                List<String> rowData = Arrays.asList(data.getSemester(), data.getDepartment(), data.getTopic(),
                        data.getTag(), data.getFinalClosure(), data.getIdea(), data.getAuthor(), data.getEmail(), data.getIdeaAttachment());

                csvPrinter.printRecord(rowData);
            }
            csvPrinter.flush();
        } catch (Exception e) {
            System.out.println("Writing CSV error!");
            e.printStackTrace();
        }
    }
}
