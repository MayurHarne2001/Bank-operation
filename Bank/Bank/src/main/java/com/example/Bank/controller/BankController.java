package com.example.Bank.controller;

import com.example.Bank.dto.Bankdto;
import com.example.Bank.entity.FileEntity;
import com.example.Bank.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/details")
public class BankController {

    private BankService bankService;

    //Add account
    @PostMapping
    public ResponseEntity<Bankdto> createDetails(@RequestBody Bankdto bankdto) {
        Bankdto savedDetails = bankService.createBankDetails(bankdto);
        return new ResponseEntity<>(savedDetails, HttpStatus.CREATED);
    }

    //Get account details
    @GetMapping("{accNo}")
    public ResponseEntity<Bankdto> getDetailsByaccNo(@PathVariable("accNo") Long accNo) {
        Bankdto bankdto = bankService.getDetilsByaccNo(accNo);
        return ResponseEntity.ok(bankdto);
    }

    //Get all details
    @GetMapping
    public ResponseEntity<List<Bankdto>> getAllDetails() {
        List<Bankdto> details = bankService.getAllDetails();
        return ResponseEntity.ok(details);
    }

    //update details
    @PutMapping("{accNo}")
    public ResponseEntity<Bankdto> updateDetails(@PathVariable("accNo") Long accNo,
                                                 @RequestBody Bankdto updateDetails) {
        Bankdto bankdto = bankService.updateDetails(accNo, updateDetails);
        return ResponseEntity.ok(bankdto);
    }

    //Remove account
    @DeleteMapping("{accNo}")
    public ResponseEntity<String> deleteDetails(@PathVariable("accNo") Long accNo) {
        bankService.deleteDetails(accNo);
        return ResponseEntity.ok("Account details deleted successfully");
    }

    // api for uploading a file
    @PostMapping("/file/upload/{accNo}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable Long accNo) throws Exception {
        bankService.saveFile(file, accNo);
        return ResponseEntity.ok("File uploaded successfully!");
    }

    //for downloading file
    @GetMapping("/file/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        FileEntity fileEntity = bankService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .body(fileEntity.getData());
    }


    //downloading a pdf having users data in pdf
    @GetMapping("/download/pdf")
    public ResponseEntity<InputStreamResource> downloadPdf() {
        ByteArrayInputStream pdfStream = bankService.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }


    //Downloading a pdf having users data in excel
    @GetMapping("/download/excel")
    public ResponseEntity<InputStreamResource> downloadExcel() {
        ByteArrayInputStream in = bankService.generateExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));


    }
}
