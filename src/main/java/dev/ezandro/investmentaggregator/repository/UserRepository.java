package dev.ezandro.investmentaggregator.repository;

import dev.ezandro.investmentaggregator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}