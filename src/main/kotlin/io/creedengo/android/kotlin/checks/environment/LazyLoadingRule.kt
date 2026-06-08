/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI554")
class LazyLoadingRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Prefer Jetpack Compose lazy components (LazyColumn, LazyRow) over ListView, GridView, or RecyclerView to load items on demand and reduce memory and battery usage."

        private val EAGER_VIEWS = setOf("ListView", "GridView", "RecyclerView")
    }

    override fun visitCallExpression(expression: KtCallExpression, data: KotlinFileContext) {
        if (expression.calleeExpression?.text in EAGER_VIEWS) {
            data.reportIssue(expression.calleeExpression!!, MESSAGE)
        }
    }
}
