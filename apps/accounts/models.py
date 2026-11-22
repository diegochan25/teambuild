from datetime import datetime, timedelta
import hashlib
import secrets
from typing import TYPE_CHECKING
from django.contrib.auth.base_user import AbstractBaseUser, BaseUserManager
from django.contrib.auth.models import PermissionsMixin
from django.db import models
from django.utils import timezone
from apps.core.models import SocialAccount


ISSUER = 'teambuild.typecrafters.dev'
EMAIL_VERIFICATION_TTL = timedelta(days=1)
PASSWORD_RESET_TTL = timedelta(hours=1)


if TYPE_CHECKING:
    from django.db.models.fields.related_descriptors import RelatedManager, ManyRelatedManager
    from apps.organizations.models import Organization, OrganizationMembership
    from apps.teams.models import Team, TeamMembership

class UserManager(BaseUserManager):
    use_in_migrations = True

    def _create_user(self, email, password, **kwargs):
        if not email:
            raise ValueError('The email must be set')
        email = self.normalize_email(email)
        user = self.model(email=email, **kwargs)
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_user(self, email, password=None, **kwargs):
        kwargs.setdefault('is_superuser', False)
        return self._create_user(email, password, **kwargs)

    def create_superuser(self, email, password=None, **kwargs):
        kwargs['is_superuser'] = True
        kwargs.setdefault('is_active', True)
        return self._create_user(email, password, **kwargs)


class User(AbstractBaseUser, PermissionsMixin):
    id = models.BigAutoField(primary_key=True)

    email = models.EmailField(max_length=255, unique=True)
    password = models.CharField(max_length=255)
    is_active = models.BooleanField(default=False)
    is_superuser = models.BooleanField(default=False)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    # Annotations
    profile: 'UserProfile'
    organizationmembership_set: 'RelatedManager[OrganizationMembership]'
    joined_organizations: 'ManyRelatedManager[Organization]'
    created_organizations: 'RelatedManager[Organization]'
    owned_organizations: 'RelatedManager[Organization]'
    teammembership_set: 'RelatedManager[TeamMembership]'
    joined_teams: 'ManyRelatedManager[Team]'
    created_teams: 'RelatedManager[Team]'
    led_teams: 'RelatedManager[Team]'


    objects = UserManager()

    USERNAME_FIELD = 'email'
    EMAIL_FIELD = 'email'

    @property
    def socialaccount_set(self):
        return SocialAccount.from_user(self.id)

    @property
    def display_name(self):
        return self.profile.display_name

    class Meta:
        db_table = 'users'


class UserProfile(models.Model):
    id = models.BigAutoField(primary_key=True)

    user = models.OneToOneField('User', on_delete=models.CASCADE, related_name='profile')
    full_name = models.CharField(max_length=255, null=True, default=None)
    display_name = models.CharField(max_length=32)
    public = models.BooleanField(default=True)
    profile_picture = models.ImageField()
    public_email = models.EmailField(max_length=255, null=True, default=None)
    bio = models.TextField(null=True, default=None)
    address = models.TextField(null=True, default=None)
    public_url = models.URLField(null=True, default=None)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    # Annotations
    user_id: int

    class Meta:
        db_table = 'user_profiles'


class Session(models.Model):
    user = models.ForeignKey('User', on_delete=models.CASCADE, null=True, default=None)
    ip_address = models.GenericIPAddressField(null=True)
    user_agent = models.CharField(max_length=255, null=True)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    # Annorations
    user_id: int | None
    session_key: str | None
    expire_date: datetime

    @property
    def id(self) -> str | None:
        return self.session_key

    @property
    def created_at(self) -> datetime:
        return self.expire_date

    class Meta:
        db_table = 'sessions'


class AccountTokenQuerySet(models.QuerySet['AccountToken']):
    def email_verification(self):
        return self.filter(type=self.model.Types.EMAIL_VERIFICATION)

    def password_reset(self):
        return self.filter(type=self.model.Types.PASSWORD_RESET)

    def active(self):
        return self.filter(expires_at__gt=timezone.now())

    def expired(self):
        return self.filter(expires_at__lte=timezone.now())

    def issued_by_teambuild(self):
        return self.filter(issuer=ISSUER)


class AccountToken(models.Model):
    class Types(models.IntegerChoices):
        EMAIL_VERIFICATION = (0, 'email_verification')
        PASSWORD_RESET = (1, 'password_reset')

    id = models.BigAutoField(primary_key=True)

    subject = models.ForeignKey('User', on_delete=models.CASCADE)
    digest = models.CharField(max_length=255, unique=True)
    type = models.PositiveSmallIntegerField(choices=Types.choices)
    issuer = models.CharField(max_length=255, default=ISSUER)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    expires_at = models.DateTimeField()

    objects = AccountTokenQuerySet.as_manager()

    # Annotations 
    subject_id: int

    @classmethod
    def for_email_verification(cls, subject: 'User'):
        return cls(
            subject=subject,
            type=AccountToken.Types.EMAIL_VERIFICATION,
            expires_at=timezone.now() + EMAIL_VERIFICATION_TTL
        )

    @classmethod
    def for_password_reset(cls, subject: 'User'):
        return cls(
            subject=subject,
            type=AccountToken.Types.PASSWORD_RESET,
            expires_at=timezone.now() + PASSWORD_RESET_TTL
        )

    def set_digest(self, raw: str) -> str:
        digest = hashlib.sha256(raw.encode('utf-8')).hexdigest()
        self.digest = digest
        return raw

    def create_and_set_digest(self) -> str:
        return self.set_digest(secrets.token_urlsafe(24))
    
    class Meta:
        db_table = 'account_tokens'
