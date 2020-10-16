package dagger.hilt.android.plugin.util

import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Find's the variant's Kotlin compile task and invokes a configure action on it.
internal fun configureKotlinCompileTask(
  project: Project,
  variant: BaseVariant,
  block: (KotlinCompile) -> Unit
) {
  @Suppress("UNCHECKED_CAST")
  val kotlinTaskProvider = project.tasks.named(
    "compile${variant.name.capitalize()}Kotlin"
  ) as TaskProvider<KotlinCompile>
  kotlinTaskProvider.configure(block)
}
