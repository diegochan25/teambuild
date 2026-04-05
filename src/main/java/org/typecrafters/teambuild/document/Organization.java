package org.typecrafters.teambuild.document;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.typecrafters.teambuild.domain.enums.OrganizationStatus;

@Document
public class Organization {
    @Id
    private String id;
    private String name;
    private String description;
    @Indexed(unique = true)
    private String slug;
    private String createdBy;
    @Indexed
    private String ownerId;
    private String logoUrl;
    private Map<String, String> details;
    private OrganizationStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Organization() { }

    public Organization(
        String name, 
        String description, 
        String slug, 
        String createdBy, 
        String ownerId, 
        String logoUrl,
        Map<String, String> details, 
        OrganizationStatus status,
        Instant createdAt, 
        Instant updatedAt,
        Instant deletedAt
    ) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.createdBy = createdBy;
        this.ownerId = ownerId;
        this.logoUrl = logoUrl;
        this.details = details;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public OrganizationStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizationStatus status) {
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
