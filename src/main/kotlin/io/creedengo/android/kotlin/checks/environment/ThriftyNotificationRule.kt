package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI529")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC529")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB012")
class ThriftyNotificationRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Avoid using vibration or sound when notifying the users to use less energy."
    }

    override val functionsToVisit = listOf(
        FunMatcher(definingSupertype = "android.app.NotificationChannel", name = "setVibrationPattern"),
        FunMatcher(definingSupertype = "android.app.NotificationChannel", name = "setSound"),
        FunMatcher(definingSupertype = "android.app.Notification.Builder", name = "setVibrate"),
        FunMatcher(definingSupertype = "android.app.Notification.Builder", name = "setSound"),
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        // Only report when at least one argument is not a null literal.
        // Calling setSound(null) or setVibrationPattern(null) removes the effect — that is compliant.
        val hasNonNullArg = resolvedCall.argumentMapping.keys.any { arg ->
            arg.text.trim() != "null"
        }
        if (hasNonNullArg) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }
}
