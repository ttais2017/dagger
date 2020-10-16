package dagger.hilt.android.simpleKotlin.lib

import android.content.Context
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.simpleKotlin.deep.DeepAndroidLib
import dagger.hilt.android.simpleKotlin.deep.DeepLib

object AndroidLibraryEntryPoints {
  fun invokeEntryPoints(context: Context) {
    EntryPointAccessors.fromApplication(context, DeepAndroidLib.LibEntryPoint::class.java)
      .getDeepAndroidInstance()
    EntryPointAccessors.fromApplication(context, DeepLib.LibEntryPoint::class.java)
      .getDeepInstance()
  }
}
