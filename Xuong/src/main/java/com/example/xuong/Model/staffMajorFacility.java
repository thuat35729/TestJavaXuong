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
@Table(name = "staff_major_facility")
public class staffMajorFacility {
    @Id
    @GeneratedValue
    private UUID id;
    private int status;
    private UUID idMajorFacility;
    private UUID idStaff;
}
