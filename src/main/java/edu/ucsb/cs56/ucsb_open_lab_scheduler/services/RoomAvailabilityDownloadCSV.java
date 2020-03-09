package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.util.List;
import java.util.stream.Collectors;


public class RoomAvailabilityDownloadCSV {
    private static Logger logger = LoggerFactory.getLogger(RoomAvailabilityDownloadCSV.class);
    public static void writeRoomAvailability(PrintWriter writer, List<RoomAvailability> rooms) {
        String[] header = {"quarter","day","startTime","endTime", "room"};
        try (
            CSVWriter csvWriter = new CSVWriter(writer,
                          CSVWriter.DEFAULT_SEPARATOR,
                          CSVWriter.NO_QUOTE_CHARACTER,
                          CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                          CSVWriter.DEFAULT_LINE_END);
        ) {
            csvWriter.writeNext(header);

            for (RoomAvailability room : rooms) {
                String[] data = {
                    room.getQuarter(),
                    room.getDay(),
                    ((Integer)room.getStartTime()).toString(),
                    ((Integer)room.getEndTime()).toString(),
                    room.getRoom().toString()
                };

                csvWriter.writeNext(data);
            }
            csvWriter.close();
        } catch (Exception e) {
            logger.info("CSV write error");
        }
    }
}
