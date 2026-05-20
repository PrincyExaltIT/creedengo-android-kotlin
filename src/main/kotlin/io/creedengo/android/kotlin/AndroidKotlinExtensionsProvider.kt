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

// Plugs Creedengo checks into sonar-kotlin's KotlinSensor — no custom sensor needed.
// registerRule's boolean adds the rule to sonar-kotlin's stock Sonar way profile too.
class AndroidKotlinExtensionsProvider : KotlinPluginExtensionsProvider {

    override fun registerKotlinPluginExtensions(extensions: KotlinPluginExtensionsProvider.Extensions) {
        extensions.registerRepository(
            AndroidKotlinRulesDefinition.REPOSITORY_KEY,
            AndroidKotlinRulesDefinition.REPOSITORY_NAME,
        )
        CHECK_CLASSES.forEach { check ->
            extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, check, true)
        }
    }

    companion object {
        val CHECK_CLASSES: List<Class<*>> = listOf(ClearCacheCheck::class.java)
    }
}
