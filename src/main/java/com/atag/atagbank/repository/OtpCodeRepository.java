package com.atag.atagbank.repository;

import com.atag.atagbank.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode,Long> {
    Optional<OtpCode>  findByEmail(String email);
    @Transactional
    public void deleteByEmail(String email);
}
