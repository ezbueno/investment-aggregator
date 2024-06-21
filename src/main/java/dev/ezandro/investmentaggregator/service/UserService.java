package dev.ezandro.investmentaggregator.service;

import dev.ezandro.investmentaggregator.dto.AccountResponseDTO;
import dev.ezandro.investmentaggregator.dto.CreateAccountDTO;
import dev.ezandro.investmentaggregator.dto.CreateUserDTO;
import dev.ezandro.investmentaggregator.dto.UpdateUserDTO;
import dev.ezandro.investmentaggregator.entity.Account;
import dev.ezandro.investmentaggregator.entity.BillingAddress;
import dev.ezandro.investmentaggregator.entity.User;
import dev.ezandro.investmentaggregator.repository.AccountRepository;
import dev.ezandro.investmentaggregator.repository.BillingAddressRepository;
import dev.ezandro.investmentaggregator.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository,
                       AccountRepository accountRepository,
                       BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDTO createUserDTO) {
        var entity = new User(
                UUID.randomUUID(),
                createUserDTO.username(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);
        var userSaved = this.userRepository.save(entity);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return this.userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return this.userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDTO updateUserDTO) {
        var id = UUID.fromString(userId);
        var user = this.userRepository.findById(id);

        if (user.isPresent()) {
            var updateUser = user.get();

            if (Objects.nonNull(updateUserDTO.username())) {
                updateUser.setUsername(updateUserDTO.username());
            }

            if (Objects.nonNull(updateUserDTO.password())) {
                updateUser.setPassword(updateUserDTO.password());
            }

            this.userRepository.save(updateUser);
        }
    }

    public void deleteUserById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = this.userRepository.existsById(id);

        if (userExists) {
            this.userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDTO createAccountDTO) {
        var user = this.userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var account = new Account(
                UUID.randomUUID(),
                createAccountDTO.description(),
                user,
                null,
                new ArrayList<>()
        );

        var accountCreated = this.accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                createAccountDTO.street(),
                createAccountDTO.number(),
                account
        );

        this.billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDTO> listAccounts(String userId) {
        var user = this.userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return user.getAccounts()
                .stream()
                .map(account -> new AccountResponseDTO(account.getAccountId().toString(), account.getDescription()))
                .toList();
    }
}