package com.conectarsalud.app.ui.auth

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.conectarsalud.app.R

public class RegisterFragmentDirections private constructor() {
  public companion object {
    public fun actionRegisterToHome(): NavDirections =
        ActionOnlyNavDirections(R.id.action_register_to_home)

    public fun actionRegisterToLogin(): NavDirections =
        ActionOnlyNavDirections(R.id.action_register_to_login)
  }
}
