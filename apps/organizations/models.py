from typing import TYPE_CHECKING
from autoslug import AutoSlugField
from django.db import models
from apps.core.models import SocialAccount


if TYPE_CHECKING:
    from django.db.models.fields.related_descriptors import RelatedManager
    from apps.teams.models import Team

class Organization(models.Model):
    id = models.BigAutoField(primary_key=True)

    creator = models.ForeignKey('accounts.User', on_delete=models.SET_NULL, null=True, related_name='created_organizations')
    owner = models.ForeignKey('accounts.User', on_delete=models.PROTECT, related_name='owned_organizations')
    name = models.CharField(max_length=255, unique=True)
    slug = AutoSlugField(populate_with='name', unique=True)
    email = models.EmailField(max_length=255, unique=True)
    creator_snapshot = models.JSONField(default=dict)
    joined_members = models.ManyToManyField('accounts.User', through='OrganizationMembership', related_name='joined_organizations')

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    # Annotations
    organizationmembership_set: 'RelatedManager[OrganizationMembership]'
    team_set: 'RelatedManager[Team]'
    profile: 'OrganizationProfile'
    creator_id: int | None
    owner_id: int

    @property
    def socialaccount_set(self):
        return SocialAccount.from_organization(self.id)

    class Meta:
        db_table = 'organizations'

class OrganizationProfile(models.Model):
    id = models.BigAutoField(primary_key=True)

    organization = models.OneToOneField('Organization', on_delete=models.CASCADE, related_name='profile')
    display_name = models.CharField(max_length=255, null=True, default=None)
    public = models.BooleanField(default=True)
    profile_picture = models.ImageField()
    public_email = models.EmailField(max_length=255, null=True, default=None)
    description = models.TextField(null=True, default=None)
    public_url = models.URLField()

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'organization_profiles'

class OrganizationMembership(models.Model):
    pk = models.CompositePrimaryKey('user_id', 'organization_id')

    user = models.ForeignKey('accounts.User', on_delete=models.CASCADE)
    organization = models.ForeignKey('Organization', on_delete=models.CASCADE)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'organization_memberships'