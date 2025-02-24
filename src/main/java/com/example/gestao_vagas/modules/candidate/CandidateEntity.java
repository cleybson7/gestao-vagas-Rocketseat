package com.example.gestao_vagas.modules.candidate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "o campo(name) não pode ser vazio")
    private String name;

    @NotBlank(message = "o campo(username) não pode ser vazio")
    @Pattern(regexp = "^[\\S]*$", message = "o campo(username) não pode conter espaços em branco")
    private String username;

    @Email(message = "o campo(email) deve conter um email válido")
    private String email;

    @Length(min = 6, max = 100, message = "o campo(password) deve conter entre 6 e 100 caracteres")
    private String password;
    private String description;
    private String curriculum;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
