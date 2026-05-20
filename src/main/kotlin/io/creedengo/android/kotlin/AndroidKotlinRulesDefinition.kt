/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin

/**
 * Shared identifiers for the Creedengo Android Kotlin rule set.
 *
 * The plugin no longer declares its own [org.sonar.api.server.rule.RulesDefinition]:
 * sonar-kotlin owns repository creation via [CreedengoKotlinExtensionsProvider],
 * and metadata is auto-loaded from `org/sonar/l10n/kotlin/rules/kotlin/`.
 *
 * This object only exposes the constants still referenced elsewhere
 * (notably by [AndroidKotlinProfile]).
 */
object AndroidKotlinRulesDefinition {
    const val LANGUAGE = "kotlin"
    const val REPOSITORY_KEY = "creedengo-android-kotlin"
    const val REPOSITORY_NAME = "Creedengo Android Kotlin"
}
