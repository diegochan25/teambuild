package org.typecrafters.teambuild.document;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.typecrafters.teambuild.domain.enums.OwnerType;

@Document
@CompoundIndex(name = "org_role_index", def = "{'ownerType': 1, 'name': 1, 'ownerId': 1}", unique = true, partialFilter = "{'deletedAt': null}")
public class Role {
    @Id
    private String id;
    private String name;
    private OwnerType ownerType;
    @Indexed
    private String ownerId;
    private String createdBy;
    private List<String> permissions;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Role() { }

    public Role(
        String name, 
        OwnerType ownerType, 
        String ownerId, 
        String createdBy, 
        List<String> permissions,
        Instant createdAt, 
        Instant updatedAt, 
        Instant deletedAt
    ) {
        this.name = name;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.createdBy = createdBy;
        this.permissions = permissions;
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

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
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
