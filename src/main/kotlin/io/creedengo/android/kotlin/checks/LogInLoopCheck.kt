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
import org.jetbrains.kotlin.psi.KtForExpression
import org.jetbrains.kotlin.psi.KtWhileExpression
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

/**
 * GCI602 — Logging in loops.
 *
 * Flags log calls (Log.d, Log.e, Timber, etc.) occurring inside loop bodies
 * (for, while, do-while).
 *
 * Logging per iteration degrades performance, wastes CPU and battery, and can
 * overwhelm log buffers. Move logs outside loops or batch them.
 *
 * Implementation: syntactic check only (pattern matching for loop ancestors).
 */
@Rule(key = "GCI602")
class LogInLoopCheck : AbstractCheck() {

    override fun visitCallExpression(expression: KtCallExpression, data: KotlinFileContext) {
        // Skeleton scope: only run on Android sources
        if (!data.isInAndroid()) return

        val callName = expression.calleeExpression?.text ?: return
        
        // Match Log.d, Log.e, Log.i, Log.v, Log.w, Timber.d, Timber.w, etc.
        if (!isLogCall(callName)) return

        // Check if inside a for or while loop
        val node = expression.asKotlinNode()
        if (isInsideLoop(node)) {
            data.reportIssue(expression, MESSAGE)
        }
    }

    /**
     * Identifies log call patterns: Log.*, Timber.*, android.util.Log.*
     */
    private fun isLogCall(callName: String): Boolean {
        // Match patterns like "Log.d", "Timber.e", "android.util.Log.v", etc.
        val patterns = listOf(
            Regex("(Log|Timber|\\w+\\.Log)\\.(d|e|i|v|w|wtf)"),  // static methods
            Regex("\\w+\\.(d|e|i|v|w)\\("),                       // instance methods
        )
        return patterns.any { it.containsMatchIn(callName) }
    }

    /**
     * Checks if the node has a loop ancestor (for, while, do-while).
     */
    private fun isInsideLoop(node: io.creedengo.android.kotlin.ast.KotlinNode): Boolean {
        return node.hasAncestor { parent ->
            parent.element is KtForExpression || parent.element is KtWhileExpression
        }
    }

    companion object {
        const val MESSAGE =
            "Avoid logging inside loops — it degrades performance, wastes battery, and overwhelms logs. Move logs outside loops or batch them."
    }
}
