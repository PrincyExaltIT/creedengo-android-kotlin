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

@Rule(key = "GCI514")
class SensorManagerLeakRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Failing to call android.hardware.SensorManager#unregisterListener() can drain the battery in just a few hours."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        val registerCalls = calls.filter { it.calleeExpression?.text == "registerListener" }
        if (registerCalls.isEmpty()) return
        val hasUnregister = calls.any { it.calleeExpression?.text == "unregisterListener" }
        if (hasUnregister) return
        registerCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
