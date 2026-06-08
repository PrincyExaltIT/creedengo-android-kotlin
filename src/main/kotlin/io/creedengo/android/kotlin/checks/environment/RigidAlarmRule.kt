package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI509")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC509")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EIDL007")
class RigidAlarmRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Using exact alarms unnecessarily reduces the OS's ability to minimize battery use (i.e. Doze Mode)."
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.app.AlarmManager",
            name = "setExact"
        ),
        FunMatcher(
            definingSupertype = "android.app.AlarmManager",
            name = "setExactAndAllowWhileIdle"
        ),
        FunMatcher(
            definingSupertype = "android.app.AlarmManager",
            name = "setRepeating"
        ),
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
    }
}
