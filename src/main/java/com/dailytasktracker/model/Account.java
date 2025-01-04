package com.dailytasktracker.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Account-related fields
    @NotBlank(message = "Account name is required")
    private String accountName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;

    private boolean active = true; // To mark whether the account is active or not

    @Enumerated(EnumType.STRING)
    private AccountType accountType; // To specify account type (e.g., USER, ADMIN)

    @NotNull(message = "Phone number is required")
    private String phoneNumber; // Now mandatory field

    @NotNull(message = "First name is required")
    private String firstName; // Now mandatory field

    @NotNull(message = "Last name is required")
    private String lastName; // Now mandatory field

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference  // Handle the serialization of Account -> Task
    private List<Task> taskList;

    @Override
    public String toString() {
        return String.format("Account{id=%d, accountName='%s', email='%s', active=%b, accountType=%s, phoneNumber='%s', firstName='%s', lastName='%s'}",
                id, accountName, email, active, accountType, phoneNumber, firstName, lastName);
    }

    public enum AccountType {
        USER, ADMIN, GUEST;
    }

}
