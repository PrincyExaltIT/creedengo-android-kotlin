/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassLiteralExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI528")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC528")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB011")
class VibrationFreeRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE = "Avoid using the device vibrator to use less energy."

        private val VIBRATOR_STRING_VALUES = setOf("vibrator", "vibrator_manager")
        private val VIBRATOR_CONSTANT_NAMES = setOf("VIBRATOR_SERVICE", "VIBRATOR_MANAGER_SERVICE")
        private val VIBRATOR_CLASS_NAMES = setOf("Vibrator", "VibratorManager")
    }

    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = "android.content.Context",
            name = "getSystemService"
        )
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val arg = resolvedCall.argumentMapping.keys.firstOrNull() ?: return
        if (isVibratorServiceArg(arg)) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }

    private fun isVibratorServiceArg(arg: KtExpression): Boolean = when (arg) {
        is KtStringTemplateExpression -> {
            // "vibrator" or "vibrator_manager" string literal
            arg.text.removeSurrounding("\"") in VIBRATOR_STRING_VALUES
        }
        is KtNameReferenceExpression -> {
            // Bare constant: VIBRATOR_SERVICE
            arg.getReferencedName() in VIBRATOR_CONSTANT_NAMES
        }
        is KtDotQualifiedExpression -> {
            val selector = arg.selectorExpression
            if (selector is KtNameReferenceExpression && selector.getReferencedName() in VIBRATOR_CONSTANT_NAMES) {
                // Context.VIBRATOR_SERVICE
                true
            } else {
                // Vibrator::class.java
                val receiver = arg.receiverExpression
                receiver is KtClassLiteralExpression &&
                    (receiver.receiverExpression as? KtNameReferenceExpression)?.getReferencedName() in VIBRATOR_CLASS_NAMES
            }
        }
        else -> false
    }
}
