package com.example.Bank.service;

import com.example.Bank.dto.Bankdto;
import com.example.Bank.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;


public interface BankService {
    Bankdto createBankDetails(Bankdto bankdto);
    Bankdto getDetilsByaccNo(Long accNo);
    List<Bankdto> getAllDetails();
    Bankdto updateDetails(Long accNo,Bankdto updateDetails);
    void deleteDetails(Long accNo);


    FileEntity getFile(Long fileId);

    void saveFile(MultipartFile file, Long recordId) throws Exception;



    ByteArrayInputStream generatePdf();

    ByteArrayInputStream generateExcel();
}
