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

@Rule(key = "GCI553")
class BatteryConstrainedWorkRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Use WorkManager with battery constraints (setRequiresBatteryNotLow or setRequiresCharging) to avoid running background work when the battery is low."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        val workBuilderCalls = calls.filter {
            val callee = it.calleeExpression?.text
            callee == "OneTimeWorkRequestBuilder" || callee == "PeriodicWorkRequestBuilder"
        }
        if (workBuilderCalls.isEmpty()) return

        val hasBatteryConstraint = calls.any {
            val callee = it.calleeExpression?.text
            callee == "setRequiresBatteryNotLow" || callee == "setRequiresCharging"
        }
        if (hasBatteryConstraint) return

        workBuilderCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
