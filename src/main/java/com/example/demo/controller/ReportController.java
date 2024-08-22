package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ReportController {
    @GetMapping(value = "/", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generate() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File file;
        try {
            file = ResourceUtils.getFile("classpath:reports/hello.jrxml");
            /*
            To avoid compiling it every time, we can save it to a file:
            JRSaver.saveObject(jasperReport, "employeeReport.jasper");
            */
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("FIRST_NAME", "THERAMED");
            //parameters.put("minSalary", 15000.0);
            //parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        } catch (IOException | JRException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(out.toByteArray());
    }
}
