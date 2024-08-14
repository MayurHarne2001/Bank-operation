package com.example.Bank.mapper;

import com.example.Bank.dto.Bankdto;
import com.example.Bank.entity.Details;

public class BankMapper {
    public static  Bankdto mapToBankdto(Details details){
        return new Bankdto(
                details.getAccNo(),
                details.getName(),
                details.getAcc_type(),
                details.getBalance()
        );
    }
    public static Details mapToDetails(Bankdto bankdto){
        return new Details(
                bankdto.getAccNo(),
                bankdto.getName(),
                bankdto.getAcc_type(),
                bankdto.getBalance()
        );
    }
}
