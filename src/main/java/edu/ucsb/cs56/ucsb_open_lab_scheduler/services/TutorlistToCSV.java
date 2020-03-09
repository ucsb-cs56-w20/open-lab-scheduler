package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.opencsv.CSVWriter;

import java.io.PrintWriter;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;

public class TutorlistToCSV{
    private static Logger logger = LoggerFactory.getLogger(TutorlistToCSV.class);
    public static void writeCSV(PrintWriter writer, List<TutorAssignment> tutorassignment){
    String[] header = {"Email","Last Name","First Name"};
        try{
            CSVWriter CsvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);

         
            CsvWriter.writeNext(header);

            for (TutorAssignment ta: tutorassignment){
                Tutor tutor = ta.getTutor();
                String data[] = {
                    tutor.getEmail(),
                    tutor.getLastName(),
                    tutor.getFirstName()
                };
                CsvWriter.writeNext(data);
            
            CsvWriter.close();
        }
    }
        catch (Exception e){
            logger.info("Writing CSV error");
            e.printStackTrace();
        }

    }
}
