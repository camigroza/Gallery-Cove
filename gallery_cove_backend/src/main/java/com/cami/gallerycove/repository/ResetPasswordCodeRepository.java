package com.cami.gallerycove.repository;

import com.cami.gallerycove.domain.model.ResetPasswordCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordCode, String> {
}
