package dagger.hilt.android.simpleKotlin.lib

import dagger.hilt.EntryPoints
import dagger.hilt.android.simpleKotlin.deep.DeepLib

object KotlinLibraryEntryPoints {
  fun invokeEntryPoints(component: Any) {
    EntryPoints.get(component, DeepLib.LibEntryPoint::class.java)
      .getDeepInstance()
  }
}
