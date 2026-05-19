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

import org.sonar.api.SonarRuntime
import org.sonar.api.server.rule.RulesDefinition
import org.sonarsource.analyzer.commons.RuleMetadataLoader

class AndroidKotlinRulesDefinition(private val sonarRuntime: SonarRuntime) : RulesDefinition {

    override fun define(context: RulesDefinition.Context) {
        val repository = context.createRepository(REPOSITORY_KEY, LANGUAGE).setName(REPOSITORY_NAME)
        RuleMetadataLoader(RESOURCE_BASE_PATH, sonarRuntime).addRulesByRuleKey(repository, RULE_KEYS)
        repository.done()
    }

    companion object {
        const val LANGUAGE = "kotlin"
        const val REPOSITORY_KEY = "creedengo-android-kotlin"
        const val REPOSITORY_NAME = "Creedengo Android Kotlin"
        const val RESOURCE_BASE_PATH = "io/creedengo/android/kotlin/rules"

        @JvmField
        val RULE_KEYS: List<String> = listOf("GCI600")
    }
}
