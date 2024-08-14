package com.example.Bank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    private String fileName;
    private String fileType;

    @Lob  // This annotation is used for large binary objects
    private byte[] data;

    private LocalDateTime uploadDateTime;

    @ManyToOne
    @JoinColumn(name = "accNo", nullable = false)
    private Details accNo;
}
