package com.dailytasktracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference  // Prevent recursion during serialization of Task -> Account
    private Account account;

    @CreationTimestamp
    //@JsonFormat(pattern = "yyyy:MM:dd hh a")  // 'hh' for 12-hour format, 'a' for AM/PM
    private LocalDateTime dateCreated;


    public Task(String description, Account account) {
        this.description = description;
        this.account = account;
    }
}
