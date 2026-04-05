package org.typecrafters.teambuild.document;

import java.time.Instant;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.typecrafters.teambuild.domain.enums.InvitationStatus;

@Document
@CompoundIndex(name = "org_membership_index", def = "{'organizationId': 1, 'userId': 1}")
public class OrganizationMember {
    @Id
    private String id;
    @Indexed(unique = true)
    private String organizationId;
    @Indexed(unique = true)
    private String userId;
    private String position;
    private Set<String> roleIds;
    private InvitationStatus invitationStatus;
    private Instant invitedAt;
    private Instant joinedAt;
    private Instant leftAt;

    public OrganizationMember() { }

    public OrganizationMember(
        String organizationId, 
        String userId, 
        String position, 
        Set<String> roleIds,
        InvitationStatus invitationStatus, 
        Instant invitedAt, 
        Instant joinedAt, 
        Instant leftAt
    ) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.position = position;
        this.roleIds = roleIds;
        this.invitationStatus = invitationStatus;
        this.invitedAt = invitedAt;
        this.joinedAt = joinedAt;
        this.leftAt = leftAt;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public Instant getInvitedAt() {
        return invitedAt;
    }

    public void setInvitedAt(Instant invitedAt) {
        this.invitedAt = invitedAt;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Instant getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(Instant leftAt) {
        this.leftAt = leftAt;
    }
}
