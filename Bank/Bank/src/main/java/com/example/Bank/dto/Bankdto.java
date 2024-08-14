package com.example.Bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bankdto {
    private Long accNo;
    private String name;
    private String acc_type;
    private Double balance;
}
