package com.todoforge.account.service.listener;

import com.todoforge.account.dto.LastLoginDTO;
import com.todoforge.account.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class AccountListener {

    AccountService accountService;

    @KafkaListener(groupId = "lastLoginGroup", topics = "last-login")
    public void lastLoginListener(@Payload LastLoginDTO lastLogin) {
        if (Objects.isNull(lastLogin)) return;
        log.info("Received: {}", lastLogin);
        accountService.updateLastLogin(lastLogin);
    }
}
