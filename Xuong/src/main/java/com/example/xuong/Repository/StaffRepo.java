package com.example.xuong.Repository;

import com.example.xuong.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StaffRepo extends JpaRepository<Staff, UUID> {
    boolean existsByAccountFE(String accountFE);

    boolean existsByAccountFPT(String accountFPT);

    boolean existsByStaffCode(String staffCode);
}
