package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.*;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;


public interface CSVToObjectService {
    <T extends Object> List<T> parse(Reader csv);

    Logger getLogger();

    default <T extends Object> List<T> parse(MultipartFile file) {
        try {
            return parse(new InputStreamReader(file.getInputStream()));
        } catch (IOException e) {
            getLogger().error("CSV could not be parsed", e);
        }
        return null;
    }
}
