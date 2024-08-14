package com.example.Bank.service.Impl;

import com.example.Bank.dto.Bankdto;
import com.example.Bank.entity.Details;
import com.example.Bank.entity.FileEntity;
import com.example.Bank.exception.AccNotFoundException;
import com.example.Bank.mapper.BankMapper;
import com.example.Bank.repository.BankRepository;
import com.example.Bank.repository.FileRepository;
import com.example.Bank.service.BankService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {
    private BankRepository bankRepository;
    private FileRepository fileRepository;

    @Override
    public Bankdto createBankDetails(Bankdto bankdto) {

        Details details = BankMapper.mapToDetails(bankdto);
        Details savedDetails = bankRepository.save(details);
        return BankMapper.mapToBankdto(savedDetails);

    }

    @Override
    public Bankdto getDetilsByaccNo(Long accNo) {
        Details details = bankRepository.findById(accNo)
                .orElseThrow(() ->
                        new AccNotFoundException("Acc number is not registered : " + accNo));
        return BankMapper.mapToBankdto(details);
    }

    @Override
    public List<Bankdto> getAllDetails() {
        List<Details> details = bankRepository.findAll();
        return details.stream().map((detail) -> BankMapper.mapToBankdto(detail))
                .collect(Collectors.toList());
    }

    @Override
    public Bankdto updateDetails(Long accNo, Bankdto updateDetails) {
        Details details = bankRepository.findById(accNo).orElseThrow(
                () -> new AccNotFoundException("Acc details not found " + accNo)
        );
        details.setName(updateDetails.getName());
        details.setAcc_type(updateDetails.getAcc_type());
        details.setBalance(updateDetails.getBalance());
        Details updateDetailsobj = bankRepository.save(details);
        return BankMapper.mapToBankdto(updateDetailsobj);
    }

    @Override
    public void deleteDetails(Long accNo) {
        Details details = bankRepository.findById(accNo).orElseThrow(
                () -> new AccNotFoundException("Acc details not found " + accNo)
        );
        bankRepository.deleteById(accNo);

    }


    private String getFileExtension(String filename) {

        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Override
    public FileEntity getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));
    }

    @Override
    public void saveFile(MultipartFile file, Long recordId) throws Exception {
        try {
            Details userId = bankRepository.findById(recordId)
                    .orElseThrow(() -> new Exception("Record not found"));

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileType(getFileExtension(file.getOriginalFilename()));
            fileEntity.setData(file.getBytes());
            fileEntity.setUploadDateTime(LocalDateTime.now());
            fileEntity.setAccNo(userId);


            System.out.println("Saving file entity: " + fileEntity);

            fileRepository.save(fileEntity);
        } catch (Exception e) {
            System.err.println("Error saving file: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ByteArrayInputStream generatePdf() {
        List<Details> users = bankRepository.findAll();

        Document document = new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph title = new Paragraph("User Details");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4); // Adjust column count as needed
            table.setWidthPercentage(100);

            addTableHeader(table, "ID");
            addTableHeader(table, "Name");
            addTableHeader(table, "Account Type");
            addTableHeader(table, "Balance");

            for (Details user : users) {
                table.addCell(user.getAccNo() != null ? user.getAccNo().toString() : "N/A");
                table.addCell(user.getName() != null ? user.getName() : "N/A");
                table.addCell(user.getAcc_type() != null ? user.getAcc_type() : "N/A");
                table.addCell(user.getBalance() != null ? user.getBalance().toString() : "N/A");
            }

            document.add(table);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (DocumentException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("IO Error: " + e.getMessage());
            return null;
        }
    }


    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setPhrase(new Paragraph(headerTitle));
        table.addCell(header);
    }


    // method for downloading a users data into excel;
    @Override
    public ByteArrayInputStream generateExcel() {
        List<Details> users = bankRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("User Details");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Account Type", "Balance"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowIndex = 1;
            for (Details user : users) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(user.getAccNo() != null ? user.getAccNo() : 0);
                row.createCell(1).setCellValue(user.getName() != null ? user.getName() : "N/A");
                row.createCell(2).setCellValue(user.getAcc_type() != null ? user.getAcc_type() : "N/A");
                row.createCell(3).setCellValue(user.getBalance() != null ? user.getBalance() : 0.0);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            return null;
        }
    }


    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerCellStyle.setFont(font);
        return headerCellStyle;
    }
}

