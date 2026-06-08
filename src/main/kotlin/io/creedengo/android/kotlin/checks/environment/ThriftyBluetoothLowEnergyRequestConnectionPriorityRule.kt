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

@Rule(key = "GCI526")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC526")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB008")
class ThriftyBluetoothLowEnergyRequestConnectionPriorityRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE =
            "Invoking BluetoothGatt.requestConnectionPriority(CONNECTION_PRIORITY_LOW_POWER) is recommended to reduce power consumption."

        private const val CONNECTION_PRIORITY_LOW_POWER = 2

        private val NAMED_CONSTANTS = mapOf(
            "CONNECTION_PRIORITY_BALANCED"  to 0,
            "CONNECTION_PRIORITY_HIGH"      to 1,
            "CONNECTION_PRIORITY_LOW_POWER" to 2,
        )
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.bluetooth.BluetoothGatt",
            name = "requestConnectionPriority"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val arg = resolvedCall.argumentMapping.keys.firstOrNull() ?: return
        val value = resolveIntConstant(arg) ?: return
        if (value != CONNECTION_PRIORITY_LOW_POWER) {
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
