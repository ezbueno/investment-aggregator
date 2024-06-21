package dev.ezandro.investmentaggregator.repository;

import dev.ezandro.investmentaggregator.entity.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {
}