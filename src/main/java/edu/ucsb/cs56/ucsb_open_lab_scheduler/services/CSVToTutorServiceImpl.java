package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.List;

@Service
public class CSVToTutorServiceImpl implements CSVToTutorService {
    private static Logger log = LoggerFactory.getLogger(CSVToTutorServiceImpl.class);

    @Override
    public List<Tutor> parse(Reader csv) {
        return new CsvToBeanBuilder<Tutor>(csv).withSkipLines(1).withType(Tutor.class).build().parse();
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
