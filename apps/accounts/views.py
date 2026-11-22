from django.http import HttpRequest
from django.views import View
from inertia import render

from apps.accounts.forms import Signup

class SignupView(View):
    def get(request: HttpRequest):
        form = Signup()
        return render(request, "Auth/Signup")

    def post(request: HttpRequest):
        form = Signup(request.POST)