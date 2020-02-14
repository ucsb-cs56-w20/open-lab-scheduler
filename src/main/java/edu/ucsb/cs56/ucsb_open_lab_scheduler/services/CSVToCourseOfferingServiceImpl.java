package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.List;

@Service
public class CSVToCourseOfferingServiceImpl implements CSVToObjectService {
    private static Logger log = LoggerFactory.getLogger(CSVToCourseOfferingServiceImpl.class);

    @Override
    public List<CourseOffering> parse(Reader csv) {
        return new CsvToBeanBuilder<CourseOffering>(csv).withSkipLines(1).withType(CourseOffering.class).build().parse();
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
