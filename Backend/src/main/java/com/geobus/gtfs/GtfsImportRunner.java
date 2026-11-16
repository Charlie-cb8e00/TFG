package com.geobus.gtfs;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ConditionalOnProperty(prefix = "gtfs", name = "import-on-startup", havingValue = "true")
public class GtfsImportRunner implements ApplicationRunner {

    private final GtfsImportService gtfsImportService;

    public GtfsImportRunner(GtfsImportService gtfsImportService) {
        this.gtfsImportService = gtfsImportService;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        gtfsImportService.importar();
    }
}
