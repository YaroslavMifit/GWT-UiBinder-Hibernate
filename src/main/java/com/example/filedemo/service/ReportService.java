package com.example.filedemo.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReportService {

    public byte[] getReportXlsx(final JasperPrint jp) throws JRException, IOException {
        JRXlsxExporter xlsxExporter = new JRXlsxExporter();
        final byte[] rawBytes;

        try(ByteArrayOutputStream xlsReport = new ByteArrayOutputStream()){
            xlsxExporter.setExporterInput(new SimpleExporterInput(jp));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
            xlsxExporter.exportReport();

            rawBytes = xlsReport.toByteArray();
        }

        return rawBytes;
    }
}
