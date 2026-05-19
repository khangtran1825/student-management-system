package com.studentmanagement.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.studentmanagement.entity.Score;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.ScoreRepository;
import com.studentmanagement.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class ReportService {

    @Inject
    StudentRepository studentRepository;

    @Inject
    ScoreRepository scoreRepository;

    public byte[] generateStudentTranscriptPdf(Long studentId) {
        Student student = studentRepository.findByIdOptional(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        List<Score> scores = scoreRepository.findByStudentId(studentId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("STUDENT TRANSCRIPT"));
            document.add(new Paragraph("Student Code: " + student.studentCode));
            document.add(new Paragraph("Full Name: " + student.fullName));
            document.add(new Paragraph("Class: " + student.classEntity.className));
            document.add(new Paragraph("\n"));

            float[] columnWidths = {150F, 100F, 100F, 100F};
            Table table = new Table(columnWidths);
            table.addHeaderCell("Subject");
            table.addHeaderCell("Midterm");
            table.addHeaderCell("Final");
            table.addHeaderCell("Average");

            for (Score s : scores) {
                table.addCell(s.subject.subjectName);
                table.addCell(s.midtermScore != null ? s.midtermScore.toString() : "-");
                table.addCell(s.finalScore != null ? s.finalScore.toString() : "-");
                table.addCell(s.averageScore != null ? s.averageScore.toString() : "-");
            }

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public byte[] generateStudentTranscriptExcel(Long studentId) {
        Student student = studentRepository.findByIdOptional(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        List<Score> scores = scoreRepository.findByStudentId(studentId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Transcript");

            Row headerRow1 = sheet.createRow(0);
            headerRow1.createCell(0).setCellValue("Student Code:");
            headerRow1.createCell(1).setCellValue(student.studentCode);

            Row headerRow2 = sheet.createRow(1);
            headerRow2.createCell(0).setCellValue("Full Name:");
            headerRow2.createCell(1).setCellValue(student.fullName);

            Row tableHeader = sheet.createRow(3);
            tableHeader.createCell(0).setCellValue("Subject");
            tableHeader.createCell(1).setCellValue("Midterm");
            tableHeader.createCell(2).setCellValue("Final");
            tableHeader.createCell(3).setCellValue("Average");

            int rowNum = 4;
            for (Score s : scores) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.subject.subjectName);
                row.createCell(1).setCellValue(s.midtermScore != null ? s.midtermScore.doubleValue() : 0.0);
                row.createCell(2).setCellValue(s.finalScore != null ? s.finalScore.doubleValue() : 0.0);
                row.createCell(3).setCellValue(s.averageScore != null ? s.averageScore.doubleValue() : 0.0);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }
}
