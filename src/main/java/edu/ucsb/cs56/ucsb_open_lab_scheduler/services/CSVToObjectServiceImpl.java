package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.List;
import java.lang.Class;

@Service
public class CSVToObjectServiceImpl<T> implements CSVToObjectService<T> {
    private static Logger log = LoggerFactory.getLogger(CSVToObjectServiceImpl.class);
    
    // cannot get type of generic T at runtime, here is a workaround
    // NOTE: setType before parse
    private Class<T> type;
    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public List<T> parse(Reader csv) {
        return new CsvToBeanBuilder<T>(csv)
        .withSkipLines(1)
        .withType(type)
        .build()
        .parse();
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
