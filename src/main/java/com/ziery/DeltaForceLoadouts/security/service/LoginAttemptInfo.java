package com.ziery.DeltaForceLoadouts.security.service;

import java.time.Instant;

public class LoginAttemptInfo {
    private int failedAttempts;
    private Instant blockedUntil;

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Instant getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(Instant blockedUntil) {
        this.blockedUntil = blockedUntil;
    }
}
