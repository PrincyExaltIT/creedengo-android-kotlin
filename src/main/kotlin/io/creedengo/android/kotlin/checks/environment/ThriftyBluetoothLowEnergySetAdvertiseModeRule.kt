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

@Rule(key = "GCI525")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC525")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB007")
class ThriftyBluetoothLowEnergySetAdvertiseModeRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "You should call AdvertiseSettings.Builder.setAdvertiseMode(ADVERTISE_MODE_LOW_POWER) to optimize battery usage."

        private const val ADVERTISE_MODE_LOW_POWER = 0

        // Maps known AdvertiseSettings constant names to their integer values
        private val NAMED_CONSTANTS = mapOf(
            "ADVERTISE_MODE_LOW_POWER" to 0,
            "ADVERTISE_MODE_BALANCED" to 1,
            "ADVERTISE_MODE_LOW_LATENCY" to 2,
        )
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.bluetooth.le.AdvertiseSettings.Builder",
            name = "setAdvertiseMode"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val arg = resolvedCall.argumentMapping.keys.firstOrNull() ?: return
        val value = resolveIntConstant(arg) ?: return
        if (value != ADVERTISE_MODE_LOW_POWER) {
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
