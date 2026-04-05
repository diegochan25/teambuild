package org.typecrafters.teambuild.document;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.typecrafters.teambuild.domain.enums.OwnerType;
import org.typecrafters.teambuild.domain.enums.ProjectStatus;

@Document
public class Project {
    @Id
    private String id;
    private String name;
    private String description;
    @Indexed(unique = true)
    private String slug;
    private OwnerType ownerType;
    private Map<String, String> details;
    private String createdBy;
    private String ownerId;
    private ProjectStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Project() { }

    public Project(
        String name, 
        String description, 
        String slug, 
        OwnerType ownerType, 
        Map<String, String> details,
        String createdBy, 
        String ownerId, 
        ProjectStatus status, 
        Instant createdAt, 
        Instant updatedAt,
        Instant deletedAt
    ) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.ownerType = ownerType;
        this.details = details;
        this.createdBy = createdBy;
        this.ownerId = ownerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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
