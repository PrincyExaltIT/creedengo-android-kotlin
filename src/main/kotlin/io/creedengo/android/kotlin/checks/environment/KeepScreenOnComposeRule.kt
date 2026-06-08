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

@Rule(key = "GCI550")
class KeepScreenOnComposeRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Keeping the screen on with Modifier.keepScreenOn() should be avoided to prevent unnecessary battery drain."
    }

    override fun visitCallExpression(expression: KtCallExpression, data: KotlinFileContext) {
        if (expression.calleeExpression?.text == "keepScreenOn") {
            data.reportIssue(expression.calleeExpression!!, MESSAGE)
        }
    }
}
