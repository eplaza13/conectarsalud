package com.conectarsalud.app.ui.turnos

import android.os.Bundle
import androidx.navigation.NavDirections
import com.conectarsalud.app.R
import kotlin.Int
import kotlin.String

public class FechaHoraFragmentDirections private constructor() {
  private data class ActionFechaHoraToConfirmacion(
    public val especialidad: String,
    public val clinica: String,
    public val fecha: String,
    public val hora: String,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_fechaHora_to_confirmacion

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("especialidad", this.especialidad)
        result.putString("clinica", this.clinica)
        result.putString("fecha", this.fecha)
        result.putString("hora", this.hora)
        return result
      }
  }

  public companion object {
    public fun actionFechaHoraToConfirmacion(
      especialidad: String,
      clinica: String,
      fecha: String,
      hora: String,
    ): NavDirections = ActionFechaHoraToConfirmacion(especialidad, clinica, fecha, hora)
  }
}
