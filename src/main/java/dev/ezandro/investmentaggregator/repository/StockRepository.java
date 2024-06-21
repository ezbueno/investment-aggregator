package dev.ezandro.investmentaggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountRepository, UUID> {
}