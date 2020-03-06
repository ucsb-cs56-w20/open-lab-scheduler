package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

import java.util.List;

public class UsersToCSV {
    private static Logger logger = LoggerFactory.getLogger(UsersToCSV.class);
    public static void writeTutors(PrintWriter writer, List<User> users) {
        String[] CSV_HEADER = {"id", "first name", "last name", "email"};
        try (
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ) {
            csvWriter.writeNext(CSV_HEADER);

            for (User user : users) {
                String[] data = {
                        String.valueOf(user.getId()),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                };

                csvWriter.writeNext(data);
            }
            csvWriter.close();
            logger.info("Write CSV using CSVWriter successfully!");
        } catch (Exception e) {
            logger.info("Writing CSV error!");
            e.printStackTrace();
        }
    }
}