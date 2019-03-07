package ch.pascal.eventapp.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String id;
    private byte[] avatar;

    private String username;

    private String email;

    private String name;

    private String surname;

    private Instant dateOfBirth;
    private String address;
    private String city;
    private String zipCode;
    private String phone;
    private String mobile;
    private String country;
    private List<Role> roles = new ArrayList<>();

    private List<PasswordHistory> passwordHistories;
    private String refreshToken;
    private String accessToken;
    private Boolean twoFAEnabled;
    private String twoFASecret;
    private Instant lastLoginTime;
    private Instant expirationDate;

    public User(String id, byte[] avatar, String username, String email, String name, String surname, Instant dateOfBirth,
                String address, String city, String zipCode, String phone, String mobile, String country, List<Role> roles,
                List<PasswordHistory> passwordHistories, String refreshToken, String accessToken, Boolean twoFAEnabled,
                String twoFASecret, Instant lastLoginTime, Instant expirationDate) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.phone = phone;
        this.mobile = mobile;
        this.country = country;
        this.roles = roles;
        this.passwordHistories = passwordHistories;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.twoFAEnabled = twoFAEnabled;
        this.twoFASecret = twoFASecret;
        this.lastLoginTime = lastLoginTime;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<PasswordHistory> getPasswordHistories() {
        return passwordHistories;
    }

    public void setPasswordHistories(List<PasswordHistory> passwordHistories) {
        this.passwordHistories = passwordHistories;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Boolean getTwoFAEnabled() {
        return twoFAEnabled;
    }

    public void setTwoFAEnabled(Boolean twoFAEnabled) {
        this.twoFAEnabled = twoFAEnabled;
    }

    public String getTwoFASecret() {
        return twoFASecret;
    }

    public void setTwoFASecret(String twoFASecret) {
        this.twoFASecret = twoFASecret;
    }

    public Instant getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Instant lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
}

