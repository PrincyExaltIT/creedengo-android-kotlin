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

@Rule(key = "GCI510")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC510")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EIDL008")
class ContinuousRenderingRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Using RENDERMODE_WHEN_DIRTY instead of RENDERMODE_CONTINUOUSLY can improve battery life."

        private const val RENDERMODE_CONTINUOUSLY = 1

        private val NAMED_CONSTANTS = mapOf(
            "RENDERMODE_CONTINUOUSLY" to 1,
            "RENDERMODE_WHEN_DIRTY" to 0,
        )
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.opengl.GLSurfaceView",
            name = "setRenderMode"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val arg = resolvedCall.argumentMapping.keys.firstOrNull() ?: return
        val value = resolveIntConstant(arg) ?: return
        if (value == RENDERMODE_CONTINUOUSLY) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }

    private fun resolveIntConstant(expression: KtExpression): Int? = when (expression) {
        is KtConstantExpression -> expression.text.toLongOrNull()?.toInt()
        is KtNameReferenceExpression -> NAMED_CONSTANTS[expression.getReferencedName()]
        is KtDotQualifiedExpression -> {
            val selector = expression.selectorExpression as? KtNameReferenceExpression
            selector?.let { NAMED_CONSTANTS[it.getReferencedName()] }
        }
        else -> null
    }
}
