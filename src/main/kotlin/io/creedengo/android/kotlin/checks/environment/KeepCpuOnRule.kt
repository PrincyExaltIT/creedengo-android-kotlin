package io.creedengo.android.kotlin.checks.environment

import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext
import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression

@Rule(key = "GCI507")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC507")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EIDL004")
class KeepCpuOnRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE = "Keeping the CPU on should be avoided to avoid draining the battery."
    }

    override val functionsToVisit = listOf(
        FunMatcher(definingSupertype = "android.os.PowerManager", name = "newWakeLock")
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
    }
}
