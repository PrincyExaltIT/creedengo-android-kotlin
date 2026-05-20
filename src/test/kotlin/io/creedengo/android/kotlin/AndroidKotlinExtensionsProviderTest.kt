/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.creedengo.android.kotlin

import com.sonarsource.plugins.kotlin.api.KotlinPluginExtensionsProvider
import io.creedengo.android.kotlin.checks.ClearCacheCheck
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AndroidKotlinExtensionsProviderTest {

    @Test
    fun `provider registers repository and every declared check under it`() {
        val recorder = RecordingExtensions()

        AndroidKotlinExtensionsProvider().registerKotlinPluginExtensions(recorder)

        assertThat(recorder.repositories)
            .containsExactly(
                AndroidKotlinRulesDefinition.REPOSITORY_KEY to AndroidKotlinRulesDefinition.REPOSITORY_NAME
            )
        assertThat(recorder.rules)
            .containsExactly(Triple(AndroidKotlinRulesDefinition.REPOSITORY_KEY, ClearCacheCheck::class.java, true))
    }

    private class RecordingExtensions : KotlinPluginExtensionsProvider.Extensions {
        val repositories = mutableListOf<Pair<String, String>>()
        val rules = mutableListOf<Triple<String, Class<*>, Boolean>>()

        override fun registerRepository(repositoryKey: String, name: String) {
            repositories.add(repositoryKey to name)
        }

        override fun registerRule(repositoryKey: String, ruleClass: Class<*>, enabledInSonarWay: Boolean) {
            rules.add(Triple(repositoryKey, ruleClass, enabledInSonarWay))
        }
    }
}
