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

@Rule(key = "GCI521")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC521")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB001")
class ThriftyMotionSensorRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Prefer using TYPE_GEOMAGNETIC_ROTATION_VECTOR instead of TYPE_ROTATION_VECTOR to use less energy."

        private const val TYPE_ROTATION_VECTOR = 11

        private val NAMED_CONSTANTS = mapOf(
            "TYPE_ROTATION_VECTOR" to 11,
            "TYPE_GEOMAGNETIC_ROTATION_VECTOR" to 20,
        )
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.hardware.SensorManager",
            name = "getDefaultSensor"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val arg = resolvedCall.argumentMapping.keys.firstOrNull() ?: return
        val value = resolveIntConstant(arg) ?: return
        if (value == TYPE_ROTATION_VECTOR) {
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
