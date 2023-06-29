package com.ppm.sgs.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;

import com.ppm.sgs.dtos.StudentReportDto;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ReportUtils {

    // .jrxml files
    private static final String BATCH_STUDENT_LIST = "Students_Batch.jrxml";

    /**
     * This method will export pdf file of the list of students in a particular
     * batch. The file will be downloaded on the client side.
     * 
     * @param response   HttpServletResponse to send file back to the client
     * @param parameters Parameters Map for the jasper report .jrxml
     * @param dtos       Collection with the fields for the jasper report
     */
    public static void reportStudentListForBatch(HttpServletResponse response, Map<String, Object> parameters,
            Collection<StudentReportDto> dtos, String type) {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dtos);

        try {
            JasperReport report = JasperCompileManager
                    .compileReport(ResourceUtils.getFile("classpath:" + BATCH_STUDENT_LIST).getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

            if (type.equals("pdf")) {
                response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename=batch_students.pdf");

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                exporter.exportReport();
            } else {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename=batch_students.xlsx");

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                exporter.exportReport();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
