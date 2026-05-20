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

import io.creedengo.android.kotlin.ast.asKotlinNode
import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

/**
 * GCI601 — Thread.sleep() blocks execution.
 *
 * Flags calls to `Thread.sleep(...)` in Android Kotlin code.
 *
 * Thread.sleep() blocks the current thread synchronously, wasting CPU cycles and battery.
 * On Android, blocking the main thread can freeze the UI. Even on background threads,
 * blocking is inefficient; coroutines and async patterns are preferred.
 *
 * Implementation: syntactic check only (pattern matching).
 * No type resolution yet — may have false positives if sleep() is overloaded,
 * but intent is clear and maintainable for V1.
 */
@Rule(key = "GCI601")
class ThreadSleepCheck : AbstractCheck() {

    override fun visitCallExpression(expression: KtCallExpression, data: KotlinFileContext) {
        // Skeleton scope: only run on Android sources
        if (!data.isInAndroid()) return

        // Syntactic check: match calls to .sleep(...)
        val callName = expression.calleeExpression?.text ?: return
        if (callName.endsWith("sleep") || callName.contains("sleep(")) {
            // Optional: skip if explicitly suppressed
            val node = expression.asKotlinNode()
            if (hasSuppressionAnnotation(node)) {
                return
            }
            data.reportIssue(expression, MESSAGE)
        }
    }

    /**
     * Check if the call is annotated with @Suppress("GCI601") or @SuppressLint.
     */
    private fun hasSuppressionAnnotation(node: io.creedengo.android.kotlin.ast.KotlinNode): Boolean {
        return node.text.contains("@Suppress", ignoreCase = false) ||
                node.text.contains("@SuppressLint", ignoreCase = false)
    }

    companion object {
        const val MESSAGE =
            "Avoid Thread.sleep() in production — it blocks threads, wasting CPU and battery. Use coroutines, delay(), or async patterns instead."
    }
}
