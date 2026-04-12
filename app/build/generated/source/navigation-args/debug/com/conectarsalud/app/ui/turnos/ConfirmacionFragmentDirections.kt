package com.conectarsalud.app.ui.turnos

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.conectarsalud.app.R

public class ConfirmacionFragmentDirections private constructor() {
  public companion object {
    public fun actionConfirmacionToHome(): NavDirections =
        ActionOnlyNavDirections(R.id.action_confirmacion_to_home)
  }
}
