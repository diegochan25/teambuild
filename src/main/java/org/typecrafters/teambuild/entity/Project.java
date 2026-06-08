package org.typecrafters.teambuild.entity;

import java.time.Instant;
import java.util.Map;

import org.typecrafters.teambuild.domain.enums.OwnerType;
import org.typecrafters.teambuild.domain.enums.ProjectStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "projects",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_projects_slug", columnNames = "slug")
    }
)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String slug;
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
    // Polymorphic FK: refers to User, Team, or Organization depending on ownerType
    @Column(name = "owner_id")
    private Long ownerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "project_details", joinColumns = @JoinColumn(name = "project_id"))
    @MapKeyColumn(name = "detail_key")
    @Column(name = "detail_value")
    private Map<String, String> details;
    @Enumerated(EnumType.STRING)
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
        Long ownerId,
        User createdBy,
        Map<String, String> details,
        ProjectStatus status,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
    ) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.createdBy = createdBy;
        this.details = details;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
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
