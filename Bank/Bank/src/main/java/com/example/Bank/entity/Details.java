package com.example.Bank.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="Acc_holder")
public class Details {
    @Id
    @GeneratedValue
    private Long accNo;
    private String name;
    private String acc_type;
    private Double balance;

    @OneToMany(mappedBy = "accNo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files = new ArrayList<>();
    public Details(Long accNo, String name, String acc_type,Double balance){

        this.accNo=accNo;
        this.name=name;
        this.acc_type=acc_type;
        this.balance=balance;
    }
}
