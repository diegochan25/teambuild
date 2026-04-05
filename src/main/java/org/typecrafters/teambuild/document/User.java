package org.typecrafters.teambuild.document;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.typecrafters.teambuild.domain.enums.UserStatus;

@Document
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String userName;
    private String displayName;
    @Indexed(unique = true)
    private String email;
    private String passwordHash;
    private String profilePictureUrl;
    private String bio;
    private Map<String, String> details;
    private UserStatus status;
    private Instant createdAt;
    private Instant lastLoginAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public User() { }

    public User(
        String firstName, 
        String lastName, 
        String userName, 
        String displayName, 
        String email,
        String passwordHash, 
        String profilePictureUrl, 
        String bio, 
        Map<String, String> details, 
        UserStatus status,
        Instant createdAt, 
        Instant lastLoginAt, 
        Instant updatedAt, 
        Instant deletedAt
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.details = details;
        this.status = status;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
