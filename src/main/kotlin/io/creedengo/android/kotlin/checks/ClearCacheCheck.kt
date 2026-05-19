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
package io.creedengo.android.kotlin.checks

import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

/**
 * GCI600 — ClearCache.
 *
 * Flags programmatic cache wiping in Android Kotlin code:
 *  - `ActivityManager.clearApplicationUserData()`
 *  - `*.cacheDir.deleteRecursively()` and similar
 *
 * Proactively clearing the cache forces the OS or the app to rebuild it from
 * network or disk, wasting battery, CPU, and bandwidth. Let the system manage
 * cache eviction.
 *
 * Implementation note: this is a deliberately simple name-based check. A FunMatcher-
 * based version with full type resolution is welcome as a follow-up PR — see the
 * comment at the bottom of this file.
 */
@Rule(key = "GCI600")
class ClearCacheCheck : AbstractCheck() {

    override fun visitCallExpression(expression: KtCallExpression, data: KotlinFileContext) {
        // Skeleton scope: only run on Android sources. Sonar-kotlin marks files
        // belonging to Android projects via the `sonar.kotlin.android` property.
        if (!data.isInAndroid()) return

        when (expression.calleeExpression?.text) {
            "clearApplicationUserData" -> data.reportIssue(expression, MESSAGE)
            "deleteRecursively" -> {
                // narrow false positives: require the receiver chain to mention `cacheDir`.
                val text = expression.parent?.text ?: expression.text
                if (text.contains("cacheDir")) data.reportIssue(expression, MESSAGE)
            }
        }
    }

    companion object {
        const val MESSAGE =
            "Avoid clearing the cache programmatically — it forces costly rebuilds from network or disk."
    }
}
