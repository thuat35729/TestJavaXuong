package com.example.xuong.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "department_facility")
public class departmentFacility {
    @Id
    @GeneratedValue
    private UUID id;
    private String idDepartMent;
    private String idFacility;
    private String staff;

}
