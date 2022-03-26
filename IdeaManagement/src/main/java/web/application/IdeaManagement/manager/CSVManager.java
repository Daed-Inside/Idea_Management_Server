package web.application.IdeaManagement.manager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Service
public class CSVManager {
    public static void writeObjectToCSV(PrintWriter writer) {
        try (
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("ID", "FirstName", "LastName"));
        ) {
                List<String> data = Arrays.asList(
                        "1",
                        "Khiem",
                        "Pham"
                );

                csvPrinter.printRecord(data);
            csvPrinter.flush();
        } catch (Exception e) {
            System.out.println("Writing CSV error!");
            e.printStackTrace();
        }
    }
}
