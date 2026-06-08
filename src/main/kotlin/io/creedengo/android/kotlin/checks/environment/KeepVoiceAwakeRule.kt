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
import org.jetbrains.kotlin.psi.KtSuperTypeCallEntry
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI511")
class KeepVoiceAwakeRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "VoiceInteractionSession.setKeepAwake(false) should be called to limit battery drain."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        val keepAwakeCalls = calls.filter {
            it.calleeExpression?.text == "setKeepAwake" && it.valueArguments.size == 1
        }

        // setKeepAwake(true) found → report the true argument
        val trueArgs = keepAwakeCalls.mapNotNull { call ->
            val arg = call.valueArguments.first().getArgumentExpression() ?: return@mapNotNull null
            if (arg.text.trim() == "true") arg else null
        }
        if (trueArgs.isNotEmpty()) {
            trueArgs.forEach { data.reportIssue(it, MESSAGE) }
            return
        }

        // setKeepAwake(false) found → compliant
        if (keepAwakeCalls.any { it.valueArguments.firstOrNull()?.getArgumentExpression()?.text?.trim() == "false" }) return

        // No setKeepAwake call at all — report if file subclasses VoiceInteractionSession
        PsiTreeUtil.collectElementsOfType(file, KtSuperTypeCallEntry::class.java)
            .filter { it.typeReference?.text?.contains("VoiceInteractionSession") == true }
            .forEach { data.reportIssue(it.typeReference!!, MESSAGE) }
    }
}
