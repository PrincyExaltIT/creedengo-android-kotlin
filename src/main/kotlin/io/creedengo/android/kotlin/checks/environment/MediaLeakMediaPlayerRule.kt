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

@Rule(key = "GCI516")
class MediaLeakMediaPlayerRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Failing to call release() on a MediaPlayer may lead to continuous battery consumption."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        val constructorCalls = calls.filter { it.calleeExpression?.text == "MediaPlayer" }
        if (constructorCalls.isEmpty()) return
        val hasRelease = calls.any { it.calleeExpression?.text == "release" && it.valueArguments.isEmpty() }
        if (hasRelease) return
        constructorCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
