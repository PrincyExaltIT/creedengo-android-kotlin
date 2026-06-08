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
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI531")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC531")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB014")
class HighFrameRateRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "To optimize content refresh and save energy, frame rate should be set at maximum 60Hz."

        private const val MAX_FRAME_RATE = 60.0f

        // Surface.FRAME_RATE_DEFAULT is the only named constant in the public Android API
        private val NAMED_CONSTANTS = mapOf("FRAME_RATE_DEFAULT" to 0.0f)
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.view.Surface",
            name = "setFrameRate"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val frameRateArg = resolvedCall.argumentMapping.keys.firstOrNull() ?: return
        val value = resolveFloatConstant(frameRateArg) ?: return
        if (value > MAX_FRAME_RATE) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }

    private fun resolveFloatConstant(expression: KtExpression): Float? = when (expression) {
        is KtConstantExpression -> {
            val text = expression.text.trim()
            if (text.endsWith("f", ignoreCase = true)) text.dropLast(1).toFloatOrNull()
            else text.toFloatOrNull()
        }
        is KtNameReferenceExpression -> NAMED_CONSTANTS[expression.getReferencedName()]
        is KtDotQualifiedExpression -> {
            val selector = expression.selectorExpression as? KtNameReferenceExpression
            selector?.let { NAMED_CONSTANTS[it.getReferencedName()] }
        }
        else -> null
    }
}
