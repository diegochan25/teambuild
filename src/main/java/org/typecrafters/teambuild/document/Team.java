package org.typecrafters.teambuild.document;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Team {
    @Id
    private String id;
    @Indexed
    private String organizationId;
    private String name;
    private String description;
    @Indexed(unique = true)
    private String slug;
    @Indexed
    private String createdBy;
    private String logoUrl;
    private Map<String, String> details;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Team() { }

    public Team(
        String organizationId, 
        String name, 
        String description, 
        String slug, 
        String createdBy, 
        String logoUrl,
        Map<String, String> details, 
        Instant createdAt, 
        Instant updatedAt, 
        Instant deletedAt
    ) {
        this.organizationId = organizationId;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.createdBy = createdBy;
        this.logoUrl = logoUrl;
        this.details = details;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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