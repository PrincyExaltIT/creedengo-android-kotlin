/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext
import com.intellij.psi.util.PsiTreeUtil

@Rule(key = "GCI551")
class EverlastingServiceRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Failing to call stopService() or stopSelf() on a started service may lead to continuous battery consumption."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        val startCalls = calls.filter {
            val callee = it.calleeExpression?.text
            callee == "startService" || callee == "startForegroundService"
        }
        if (startCalls.isEmpty()) return

        val hasStop = calls.any {
            val callee = it.calleeExpression?.text
            callee == "stopService" || callee == "stopSelf"
        }
        if (hasStop) return

        startCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
