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

@Rule(key = "GCI520")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC520")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EPOW006")
class SaveModeAwarenessRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Taking into account when the device is entering or exiting the power save mode is a good practice."

        val BATTERY_ACTION_STRINGS = setOf("android.intent.action.BATTERY_CHANGED")

        // Intent class constant names that resolve to the battery action string
        val BATTERY_ACTION_CONSTANTS = setOf("ACTION_BATTERY_CHANGED")
    }

    override fun visitCallExpression(expression: KtCallExpression, data: KotlinFileContext) {
        val callee = expression.calleeExpression?.text ?: return
        when {
            callee == "isPowerSaveMode" ->
                data.reportIssue(expression.calleeExpression!!, MESSAGE)
            callee == "IntentFilter" ->
                checkFirstArg(expression, data)
            callee == "addAction" ->
                checkFirstArg(expression, data)
            callee == "create" -> {
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
        if (isBatteryAction(firstArg)) {
            data.reportIssue(firstArg, MESSAGE)
        }
    }

    private fun isBatteryAction(expression: KtExpression): Boolean = when (expression) {
        is KtStringTemplateExpression -> {
            expression.text.removeSurrounding("\"") in BATTERY_ACTION_STRINGS
        }
        else -> {
            val text = expression.text.trim()
            BATTERY_ACTION_CONSTANTS.any { text == it || text.endsWith(".$it") }
        }
    }
}
