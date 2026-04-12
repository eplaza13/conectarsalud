package com.conectarsalud.app.ui.home

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.conectarsalud.app.R
import kotlin.Int
import kotlin.String

public class HomeFragmentDirections private constructor() {
  private data class ActionHomeToEspecialidad(
    public val especialidad: String = "",
  ) : NavDirections {
    public override val actionId: Int = R.id.action_home_to_especialidad

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("especialidad", this.especialidad)
        return result
      }
  }

  public companion object {
    public fun actionHomeToEspecialidad(especialidad: String = ""): NavDirections =
        ActionHomeToEspecialidad(especialidad)

    public fun actionHomeLogout(): NavDirections = ActionOnlyNavDirections(R.id.action_home_logout)
  }
}
