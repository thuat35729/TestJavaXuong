package com.example.xuong.Model;

import com.example.xuong.Validator.NoWhitespace;
import com.example.xuong.Validator.ValidFE;
import com.example.xuong.Validator.ValidFPT;
import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "Tên không được bỏ trống")
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String name;

    @Column(name = "account_fe")
    @NotBlank(message = "Email FE không được bỏ trống")
    @Email(message = "Email FE không hợp lệ")
    @Size(max = 100, message = "Email FE không được vượt quá 100 ký tự")
    @NoWhitespace
    @ValidFE
    private String accountFE;

    @Column(name = "account_fpt")
    @NotBlank(message = "Email FPT không được bỏ trống")
    @Email(message = "Email FPT không hợp lệ")
    @Size(max = 100, message = "Email FPT không được vượt quá 100 ký tự")
    @NoWhitespace
    @ValidFPT
    private String accountFPT;

    @Column(name = "staff_code")
    @NotBlank(message = "Mã không được bỏ trống")
    @Size(max = 15, message = "Mã không được vượt quá 15 ký tự")
    private String staffCode;

    @Column(name = "status")
    private int status;
}
