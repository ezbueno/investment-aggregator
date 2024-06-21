package dev.ezandro.investmentaggregator.repository;

import dev.ezandro.investmentaggregator.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}