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

@Rule(key = "GCI530")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC530")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB013")
class TorchFreeRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Flashlight is one of the most energy-intensive component. Don't programmatically turn it on."
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.hardware.camera2.CameraManager",
            name = "setTorchMode"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        // setTorchMode(cameraId, enabled) — flag only when enabled (arg index 1) is true
        val args = resolvedCall.argumentMapping.keys.toList()
        val enabledArg = args.getOrNull(1) ?: return
        if (isTrueLiteral(enabledArg)) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }

    private fun isTrueLiteral(expression: KtExpression): Boolean =
        expression is KtConstantExpression && expression.text.trim() == "true"
}
