/*
 * Copyright (C) 2020 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Functional test of the plugin
 *
 * To run these tests first deploy artifacts to local maven via util/install-local-snapshot.sh.
 */
class HiltGradlePluginTest {

  @get:Rule
  val testProjectDir = TemporaryFolder()

  lateinit var gradleRunner: GradleTestRunner

  @Before
  fun setup() {
    gradleRunner = GradleTestRunner(testProjectDir)
  }

  // Verify plugin configuration fails when runtime dependency is missing but plugin is applied.
  @Test
  fun test_missingLibraryDep() {
    gradleRunner.addDependencies(
      "implementation 'androidx.appcompat:appcompat:1.1.0'"
    )

    val result = gradleRunner.buildAndFail()
    assertThat(result.getOutput()).contains(
      "The Hilt Android Gradle plugin is applied but no " +
        "com.google.dagger:hilt-android dependency was found."
    )
  }

  // Verify plugin configuration fails when compiler dependency is missing but plugin is applied.
  @Test
  fun test_missingCompilerDep() {
    gradleRunner.addDependencies(
      "implementation 'androidx.appcompat:appcompat:1.1.0'",
      "implementation 'com.google.dagger:hilt-android:LOCAL-SNAPSHOT'"
    )

    val result = gradleRunner.buildAndFail()
    assertThat(result.getOutput()).contains(
      "The Hilt Android Gradle plugin is applied but no " +
        "com.google.dagger:hilt-compiler dependency was found."
    )
  }

  // Verify 'enableClasspathAggregation' is not valid in an android library
  @Test
  fun test_InvalidClasspathAggregationInLibrary() {
    gradleRunner.addDependencies(
      "implementation 'androidx.appcompat:appcompat:1.1.0'",
      "implementation 'com.google.dagger:hilt-android:LOCAL-SNAPSHOT'",
      "annotationProcessor 'com.google.dagger:hilt-compiler:LOCAL-SNAPSHOT'",
      "implementation project(':library')",
    )

    testProjectDir.newFile("settings.gradle").apply {
      writeText(
        """
        include ':library'
        """.trimIndent()
      )
    }

    testProjectDir.newFolder("library/")
    testProjectDir.newFile("library/build.gradle").apply {
      writeText(
        """
        plugins {
          id 'com.android.library'
          id 'dagger.hilt.android.plugin'
        }
        
        android {
          compileSdkVersion 29
          buildToolsVersion "29.0.2"

          defaultConfig {
            minSdkVersion 21
            targetSdkVersion 29
            versionCode 1
            versionName "1.0"
          }

          compileOptions {
              sourceCompatibility 1.8
              targetCompatibility 1.8
          }
        }
        
        hilt {
          enableClasspathAggregation = true
        }
        """.trimIndent()
      )
    }

    val result = gradleRunner.buildAndFail()
    assertThat(result.getOutput()).contains(
      "The Hilt option 'enableClasspathAggregation' can only be enabled in projects with the " +
        "Android application plugin. (com.android.application)"
    )
  }
}
