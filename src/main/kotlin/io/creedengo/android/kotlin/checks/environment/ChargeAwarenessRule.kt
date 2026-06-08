/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI519")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC519")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EPOW004")
class ChargeAwarenessRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Monitoring power changes and customizing behavior depending on battery level is a good practice."

        val POWER_ACTION_STRINGS = setOf(
            "android.intent.action.ACTION_POWER_CONNECTED",
            "android.intent.action.ACTION_POWER_DISCONNECTED",
            "android.intent.action.BATTERY_LOW",
            "android.intent.action.BATTERY_OKAY"
        )

        // The Intent class constant names that resolve to the power action strings above
        val POWER_ACTION_CONSTANTS = setOf(
            "ACTION_POWER_CONNECTED",
            "ACTION_POWER_DISCONNECTED",
            "ACTION_BATTERY_LOW",
            "ACTION_BATTERY_OKAY"
        )
    }

    override fun visitCallExpression(expression: KtCallExpression, data: KotlinFileContext) {
        val callee = expression.calleeExpression?.text ?: return
        when {
            callee == "IntentFilter" -> checkFirstArg(expression, data)
            callee == "addAction" -> checkFirstArg(expression, data)
            callee == "create" -> {
                // Only flag IntentFilter.create(...) — check the receiver is "IntentFilter"
                val parent = expression.parent
                if (parent is KtDotQualifiedExpression &&
                    parent.receiverExpression.text == "IntentFilter"
                ) {
                    checkFirstArg(expression, data)
                }
            }
        }
    }

    private fun checkFirstArg(expression: KtCallExpression, data: KotlinFileContext) {
        val firstArg = expression.valueArguments.firstOrNull()?.getArgumentExpression() ?: return
        if (isPowerAction(firstArg)) {
            data.reportIssue(firstArg, MESSAGE)
        }
    }

    private fun isPowerAction(expression: KtExpression): Boolean {
        return when (expression) {
            is KtStringTemplateExpression -> {
                val text = expression.text.removeSurrounding("\"")
                text in POWER_ACTION_STRINGS
            }
            else -> {
                val text = expression.text.trim()
                POWER_ACTION_CONSTANTS.any { text == it || text.endsWith(".$it") }
            }
        }
    }
}
