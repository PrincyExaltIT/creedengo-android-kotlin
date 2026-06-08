/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtFile
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI552")
class UncachedDataReceptionRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Install HttpResponseCache at application startup to cache HTTP responses and reduce energy consumption."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        val openCalls = calls.filter { it.calleeExpression?.text == "openConnection" }
        if (openCalls.isEmpty()) return

        val hasCacheInstall = calls.any { call ->
            call.calleeExpression?.text == "install" &&
                (call.parent as? KtDotQualifiedExpression)?.receiverExpression?.text == "HttpResponseCache"
        }
        if (hasCacheInstall) return

        openCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
