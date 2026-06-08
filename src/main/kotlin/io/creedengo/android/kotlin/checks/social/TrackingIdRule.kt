/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.social

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI534")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC534")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "SPRI004")
class TrackingIdRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Avoid using TelephonyManager#getDeviceId() due to privacy concerns."
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.telephony.TelephonyManager",
            name = "getDeviceId"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
    }
}
