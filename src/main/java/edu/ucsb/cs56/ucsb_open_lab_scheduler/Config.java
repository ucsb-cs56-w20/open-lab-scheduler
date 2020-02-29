package edu.ucsb.cs56.ucsb_open_lab_scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class Config {

    /**
     * Code taken from lab07
     * 
     * Conversion service to allow using Lists on Value annotations for
     * application.properties values. See:
     * https://stackoverflow.com/a/29970335/6454116
     * 
     * @return a default conversion service used automatically
     */

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        return service;
    }
}