package com.example.filedemo.controller;

import com.example.filedemo.service.FileStorageService;
import com.example.filedemo.service.ReportService;
import com.example.filedemo.report.UserDataReport;
import com.example.filedemo.service.UserDataService;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/userDataReport", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @ResponseBody
    public HttpEntity<byte[]> getFilterDataXlsx() throws JRException, IOException, ClassNotFoundException {
        logger.info("Начали формировать отчет");
        final UserDataReport userDataReport = new UserDataReport(userDataService.getListUserDataFilter());
        final byte[] data = reportService.getReportXlsx(userDataReport.getReport(fileStorageService.getListUserDataFilter(2)));

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=userDataReport.xlsx");
        logger.info("Успешно сформировали отчет");
        header.setContentLength(data.length);

        return new HttpEntity<byte[]>(data, header);
    }
}
