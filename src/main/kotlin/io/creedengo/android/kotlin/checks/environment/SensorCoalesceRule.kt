/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI500")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC500")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EBAT002")
class SensorCoalesceRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Prefer using a reported latency on your SensorManager to reduce the power consumption of the app."
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.hardware.SensorManager",
            name = "registerListener"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val args = resolvedCall.argumentMapping.keys.toList()
        // Flag unless the 4th argument (maxReportLatencyUs) is a positive literal integer
        if (args.size < 4 || !isPositiveLiteralInt(args[3])) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }

    // Returns true only when the expression is a literal integer > 0.
    // Non-literal expressions (variables, Handler references) return false,
    // matching the Java rule's conservative behaviour for non-constant values.
    private fun isPositiveLiteralInt(expression: KtExpression): Boolean {
        if (expression !is KtConstantExpression) return false
        val text = expression.text.trim().replace("_", "").lowercase()
        val value = if (text.endsWith("l")) text.dropLast(1).toLongOrNull()
                    else text.toLongOrNull()
        return value != null && value > 0
    }
}
