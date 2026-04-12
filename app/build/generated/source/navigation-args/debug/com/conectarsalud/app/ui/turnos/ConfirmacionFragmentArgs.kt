package com.conectarsalud.app.ui.turnos

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class ConfirmacionFragmentArgs(
  public val especialidad: String,
  public val clinica: String,
  public val fecha: String,
  public val hora: String,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("especialidad", this.especialidad)
    result.putString("clinica", this.clinica)
    result.putString("fecha", this.fecha)
    result.putString("hora", this.hora)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("especialidad", this.especialidad)
    result.set("clinica", this.clinica)
    result.set("fecha", this.fecha)
    result.set("hora", this.hora)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ConfirmacionFragmentArgs {
      bundle.setClassLoader(ConfirmacionFragmentArgs::class.java.classLoader)
      val __especialidad : String?
      if (bundle.containsKey("especialidad")) {
        __especialidad = bundle.getString("especialidad")
        if (__especialidad == null) {
          throw IllegalArgumentException("Argument \"especialidad\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"especialidad\" is missing and does not have an android:defaultValue")
      }
      val __clinica : String?
      if (bundle.containsKey("clinica")) {
        __clinica = bundle.getString("clinica")
        if (__clinica == null) {
          throw IllegalArgumentException("Argument \"clinica\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"clinica\" is missing and does not have an android:defaultValue")
      }
      val __fecha : String?
      if (bundle.containsKey("fecha")) {
        __fecha = bundle.getString("fecha")
        if (__fecha == null) {
          throw IllegalArgumentException("Argument \"fecha\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"fecha\" is missing and does not have an android:defaultValue")
      }
      val __hora : String?
      if (bundle.containsKey("hora")) {
        __hora = bundle.getString("hora")
        if (__hora == null) {
          throw IllegalArgumentException("Argument \"hora\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"hora\" is missing and does not have an android:defaultValue")
      }
      return ConfirmacionFragmentArgs(__especialidad, __clinica, __fecha, __hora)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ConfirmacionFragmentArgs {
      val __especialidad : String?
      if (savedStateHandle.contains("especialidad")) {
        __especialidad = savedStateHandle["especialidad"]
        if (__especialidad == null) {
          throw IllegalArgumentException("Argument \"especialidad\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"especialidad\" is missing and does not have an android:defaultValue")
      }
      val __clinica : String?
      if (savedStateHandle.contains("clinica")) {
        __clinica = savedStateHandle["clinica"]
        if (__clinica == null) {
          throw IllegalArgumentException("Argument \"clinica\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"clinica\" is missing and does not have an android:defaultValue")
      }
      val __fecha : String?
      if (savedStateHandle.contains("fecha")) {
        __fecha = savedStateHandle["fecha"]
        if (__fecha == null) {
          throw IllegalArgumentException("Argument \"fecha\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"fecha\" is missing and does not have an android:defaultValue")
      }
      val __hora : String?
      if (savedStateHandle.contains("hora")) {
        __hora = savedStateHandle["hora"]
        if (__hora == null) {
          throw IllegalArgumentException("Argument \"hora\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"hora\" is missing and does not have an android:defaultValue")
      }
      return ConfirmacionFragmentArgs(__especialidad, __clinica, __fecha, __hora)
    }
  }
}
