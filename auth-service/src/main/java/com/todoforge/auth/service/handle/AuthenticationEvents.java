package com.todoforge.auth.service.handle;

import com.todoforge.auth.dto.LastLoginDTO;
import com.todoforge.auth.dto.TodoUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import java.time.LocalDateTime;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class AuthenticationEvents {

    KafkaTemplate<String, Object> kafkaTemplate;
    NewTopic lastLoginTopic;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent successEvent) {
        Object principal = successEvent.getAuthentication().getPrincipal();
        if (principal instanceof TodoUser) {
            LocalDateTime lastLogin = LocalDateTime.now();
            TodoUser todoUser = (TodoUser) principal;
            String topicName = lastLoginTopic.name();
            LastLoginDTO lastLoginDTO = LastLoginDTO.builder().username(todoUser.getUsername()).localDateTime(lastLogin).build();

            ListenableFuture future = kafkaTemplate.send(topicName, lastLoginDTO);
            SuccessCallback successCallback = result -> {
                SendResult<String, Object> sendResult = (SendResult<String, Object>) result;
                log.info("{} Topic -> SUCCESS ", sendResult.getProducerRecord().topic());
            };
            FailureCallback failureCallback = ex -> { // KafkaProducerException
                log.error(topicName + " Topic -> FAILURE ", ex);
            };
            future.addCallback(successCallback, failureCallback);
        }
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent failureEvent) {
        Object principal = failureEvent.getAuthentication().getPrincipal();
        AuthenticationException exception = failureEvent.getException();
        if (exception instanceof UsernameNotFoundException) {
            // Handle the UsernameNotFoundException
            log.error(principal + " not found", failureEvent.getException());
        } else {
            // Handle other authentication failures
            log.error("Authentication failed", failureEvent.getException());
        }
    }
}
