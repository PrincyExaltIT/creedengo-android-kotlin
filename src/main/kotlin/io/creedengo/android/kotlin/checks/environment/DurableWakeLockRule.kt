package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI508")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC508")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EIDL006")
class DurableWakeLockRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Prefer setting a timeout when acquiring a wake lock to avoid running down the device's battery excessively."
    }

    override val functionsToVisit = listOf(
        FunMatcher(definingSupertype = "android.os.PowerManager.WakeLock", name = "acquire")
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        // Only flag acquire() with no arguments — acquire(timeoutMillis) is compliant
        if (resolvedCall.argumentMapping.isEmpty()) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }
}
