package com.conectarsalud.app.ui.turnos

import android.os.Bundle
import androidx.navigation.NavDirections
import com.conectarsalud.app.R
import kotlin.Int
import kotlin.String

public class EspecialidadFragmentDirections private constructor() {
  private data class ActionEspecialidadToFechaHora(
    public val especialidad: String,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_especialidad_to_fechaHora

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("especialidad", this.especialidad)
        return result
      }
  }

  public companion object {
    public fun actionEspecialidadToFechaHora(especialidad: String): NavDirections =
        ActionEspecialidadToFechaHora(especialidad)
  }
}
