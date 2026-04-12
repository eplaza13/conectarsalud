package com.conectarsalud.app.ui.turnos

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class FechaHoraFragmentArgs(
  public val especialidad: String,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("especialidad", this.especialidad)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("especialidad", this.especialidad)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): FechaHoraFragmentArgs {
      bundle.setClassLoader(FechaHoraFragmentArgs::class.java.classLoader)
      val __especialidad : String?
      if (bundle.containsKey("especialidad")) {
        __especialidad = bundle.getString("especialidad")
        if (__especialidad == null) {
          throw IllegalArgumentException("Argument \"especialidad\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"especialidad\" is missing and does not have an android:defaultValue")
      }
      return FechaHoraFragmentArgs(__especialidad)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): FechaHoraFragmentArgs {
      val __especialidad : String?
      if (savedStateHandle.contains("especialidad")) {
        __especialidad = savedStateHandle["especialidad"]
        if (__especialidad == null) {
          throw IllegalArgumentException("Argument \"especialidad\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"especialidad\" is missing and does not have an android:defaultValue")
      }
      return FechaHoraFragmentArgs(__especialidad)
    }
  }
}
