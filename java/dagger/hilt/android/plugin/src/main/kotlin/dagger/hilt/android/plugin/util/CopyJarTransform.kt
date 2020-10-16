package dagger.hilt.android.plugin.util

import org.gradle.api.artifacts.transform.CacheableTransform
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Classpath

// A simple transform that copies the input jar file into the output destination.
// TODO: Improve to only copy classes that need to be visible by Hilt & Dagger.
@CacheableTransform
abstract class CopyJarTransform : TransformAction<TransformParameters.None> {
  @get:Classpath
  @get:InputArtifact
  abstract val inputArtifactProvider: Provider<FileSystemLocation>

  override fun transform(outputs: TransformOutputs) {
    val inputJar = inputArtifactProvider.get().asFile
    val outputJar = outputs.file(inputJar.name)
    inputJar.copyTo(outputJar)
  }
}
