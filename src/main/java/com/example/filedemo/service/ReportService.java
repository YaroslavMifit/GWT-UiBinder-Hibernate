package com.example.filedemo.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.IOException;

public interface ReportService {
    byte[] getReportXlsx(final JasperPrint jp) throws JRException, IOException;
}
