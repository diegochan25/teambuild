from django import forms


class Signup(forms.Form):
    email = forms.EmailField()
    display_name = forms.EmailField()
    password = forms.CharField()
    confirm_password = forms.CharField()