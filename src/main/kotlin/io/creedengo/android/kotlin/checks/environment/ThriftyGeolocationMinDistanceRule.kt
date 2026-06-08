/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI527")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC527")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB010")
class ThriftyGeolocationMinDistanceRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE = "Location updates should be done with a distance interval greater than 0."
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
        // Provider-based overloads: first arg is String (or null) → minDistance at index 2
        // Criteria-based overloads: first arg is a numeric literal → minDistance at index 1
        val minDistIdx = if (isNumericLiteral(firstArg)) 1 else 2
        val minDistArg = args.getOrNull(minDistIdx) ?: return
        if (isZeroLiteral(minDistArg)) {
            kotlinFileContext.reportIssue(minDistArg, MESSAGE)
        }
    }
}
