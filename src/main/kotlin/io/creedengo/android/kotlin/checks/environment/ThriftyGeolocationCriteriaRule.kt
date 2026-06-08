/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI524")
class ThriftyGeolocationCriteriaRule : AbstractCheck() {

    companion object {
        const val MESSAGE_REQUEST = "You should configure a location provider (LocationManager.getBestProvider(...)) to optimize battery usage."
        const val MESSAGE_BEST_PROVIDER = "You should call Criteria.setPowerRequirement(POWER_LOW) to optimize battery usage."
        const val MESSAGE_SET_POWER = "You should set the power requirement to POWER_LOW to optimize battery usage."

        private val POWER_LOW_TEXTS = setOf("1", "Criteria.POWER_LOW", "POWER_LOW")
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)

        // Case 3 (independent): setPowerRequirement called with arg != POWER_LOW
        val setPowerCalls = calls.filter { it.calleeExpression?.text == "setPowerRequirement" }
        setPowerCalls.forEach { call ->
            val argText = call.valueArguments.firstOrNull()?.getArgumentExpression()?.text?.trim() ?: ""
            if (!isPowerLow(argText)) {
                data.reportIssue(call.calleeExpression!!, MESSAGE_SET_POWER)
            }
        }

        // Cases 1 and 2 (chained on requestLocationUpdates)
        val requestCalls = calls.filter { it.calleeExpression?.text == "requestLocationUpdates" }
        if (requestCalls.isEmpty()) return

        val bestProviderCalls = calls.filter { it.calleeExpression?.text == "getBestProvider" }
        if (bestProviderCalls.isEmpty()) {
            // Case 1: no getBestProvider at all
            requestCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE_REQUEST) }
        } else if (setPowerCalls.isEmpty()) {
            // Case 2: getBestProvider present but setPowerRequirement never called
            bestProviderCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE_BEST_PROVIDER) }
        }
    }

    private fun isPowerLow(text: String): Boolean =
        POWER_LOW_TEXTS.any { text == it || text.endsWith(".$it") }
}
