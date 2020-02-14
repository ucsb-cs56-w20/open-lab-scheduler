package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.*;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;


public interface CSVToObjectService<T> {
    List<T> parse(Reader csv);

    Logger getLogger();

    void setType(Class<T> type);

    default List<T> parse(MultipartFile file) {
        try {
            return parse(new InputStreamReader(file.getInputStream()));
        } catch (IOException e) {
            getLogger().error("CSV could not be parsed", e);
        }
        return null;
    }
}
