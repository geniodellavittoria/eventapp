package ch.pascal.eventapp.model;

import java.io.Serializable;
import java.time.Instant;

public class PasswordHistory implements Serializable {

    private Instant timestamp;

    private String passwordHash;

    public PasswordHistory(Instant timestamp, String passwordHash) {
        this.timestamp = timestamp;
        this.passwordHash = passwordHash;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
