from typing import TYPE_CHECKING
from django.db import models


if TYPE_CHECKING:
    from django.db.models.fields.related_descriptors import RelatedManager

class Team(models.Model):
    id = models.BigAutoField(primary_key=True)

    creator = models.ForeignKey('accounts.User', on_delete=models.SET_NULL, null=True, related_name='created_teams')
    leader = models.ForeignKey('accounts.User', on_delete=models.PROTECT, related_name='led_teams')
    organization = models.ForeignKey('organizations.Organization', on_delete=models.CASCADE)
    name = models.CharField(max_length=255)
    joined_members = models.ManyToManyField('accounts.User', through='TeamMembership', related_name='joined_teams')
    creator_snapshot = models.JSONField(default=dict)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    # Annotations
    teammembership_set: 'RelatedManager[TeamMembership]'
    organization_id: int
    creator_id: int | None
    leader_id: int

    class Meta:
        db_table ='teams'
        constraints = [models.UniqueConstraint(fields=['organization', 'name'], name='uniq_team_name_per_org')]

class TeamProfile(models.Model):
    id = models.BigAutoField(primary_key=True)

    team = models.OneToOneField('Team', on_delete=models.CASCADE, related_name='profile')
    profile_picture = models.ImageField()
    description = models.TextField(null=True, default=None)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    # Annotations
    team_id: int

    class Meta:
        db_table ='team_profiles'

class TeamMembership(models.Model):
    pk = models.CompositePrimaryKey('user_id', 'team_id')

    user = models.ForeignKey('accounts.User', on_delete=models.CASCADE)
    team = models.ForeignKey('Team', on_delete=models.CASCADE)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    # Annotations
    user_id: int
    team_id: int

    class Meta:
        db_table = 'team_memberships'