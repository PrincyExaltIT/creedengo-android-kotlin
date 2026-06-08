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

@Rule(key = "GCI523")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC523")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB005")
class ThriftyGeolocationMinTimeRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE = "Location updates should be done with a time greater than 0."
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.location.LocationManager",
            name = "requestLocationUpdates"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val args = resolvedCall.argumentMapping.keys.toList()
        val firstArg = args.firstOrNull() ?: return
        // Provider-based overloads: first arg is String (or null) → minTime at index 1
        // Criteria-based overloads: first arg is a numeric literal → minTime at index 0
        val minTimeIdx = if (isNumericLiteral(firstArg)) 0 else 1
        val minTimeArg = args.getOrNull(minTimeIdx) ?: return
        if (isZeroLiteral(minTimeArg)) {
            kotlinFileContext.reportIssue(minTimeArg, MESSAGE)
        }
    }
}

internal fun isNumericLiteral(expression: KtExpression): Boolean {
    if (expression !is KtConstantExpression) return false
    val text = expression.text.trim()
    return text != "null" && text != "true" && text != "false" && !text.startsWith("'")
}

internal fun isZeroLiteral(expression: KtExpression): Boolean {
    if (expression !is KtConstantExpression) return false
    val text = expression.text.trim().lowercase().replace("_", "")
    val value = when {
        text.endsWith("l") -> text.dropLast(1).toLongOrNull()
        text.endsWith("f") -> text.dropLast(1).toFloatOrNull()?.let { if (it == 0f) 0L else 1L }
        text.endsWith("d") -> text.dropLast(1).toDoubleOrNull()?.let { if (it == 0.0) 0L else 1L }
        else -> text.toLongOrNull()
    }
    return value == 0L
}
