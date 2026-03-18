package com.ziery.DeltaForceLoadouts.security.service;

import com.ziery.DeltaForceLoadouts.exception.LoginBlockedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MINUTES = 10;

    private final Map<String, LoginAttemptInfo> attempts = new ConcurrentHashMap<>();

    public void checkIfBlocked(String username) {
        LoginAttemptInfo info = attempts.get(username);

        if (info == null || info.getBlockedUntil() == null) {
            return;
        }

        if (Instant.now().isBefore(info.getBlockedUntil())) {
            throw new LoginBlockedException("Muitas tentativas inválidas. Tente novamente mais tarde.");
        }

        info.setBlockedUntil(null);
        info.setFailedAttempts(0);
    }

    public void registerFailedAttempt(String username) {
        LoginAttemptInfo info = attempts.computeIfAbsent(username, key -> new LoginAttemptInfo());

        int newCount = info.getFailedAttempts() + 1;
        info.setFailedAttempts(newCount);

        if (newCount >= MAX_ATTEMPTS) {
            info.setBlockedUntil(Instant.now().plus(BLOCK_DURATION_MINUTES, ChronoUnit.MINUTES));
            info.setFailedAttempts(0);
        }
    }

    public void resetAttempts(String username) {
        attempts.remove(username);
    }
}
