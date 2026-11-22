from django.db import models


class SocialAccountQuerySet(models.QuerySet['SocialAccount']):
    def from_organizations(self):
        return self.filter(owner_type=self.model.Owners.ORGANIZATION)

    def from_organization(self, organization_id: int):
        return self.from_organizations().filter(owner_id=organization_id)

    def from_users(self):
        return self.filter(owner_type=self.model.Owners.USER)

    def from_user(self, user_id: int):
        return self.from_users().filter(owner_id=user_id)

    def from_platform(self, platform: int):
        return self.filter(platform=platform)

class SocialAccount(models.Model):
    class Platforms(models.IntegerChoices):
        FACEBOOK = (0, 'Facebook')
        TWITTER = (1, 'X')
        INSTAGRAM = (2, 'Instagram')
        GITHUB = (3, 'GitHub')
        LINKEDIN = (4, 'LinkedIn')

    class Owners(models.IntegerChoices):
        ORGANIZATION = (0, 'Organization')
        USER = (1, 'User')

    id = models.BigAutoField(primary_key=True)

    owner_type = models.PositiveSmallIntegerField(choices=Owners.choices, db_index=True)
    owner_id = models.BigIntegerField(db_index=True)
    platform = models.PositiveSmallIntegerField(choices=Platforms.choices)
    url = models.URLField()

    class Meta:
        db_table = 'social_accounts'
        constraints = [models.UniqueConstraint(fields=['owner_type', 'owner_id', 'platform'], name='uniq_platform_per_owner')]
    