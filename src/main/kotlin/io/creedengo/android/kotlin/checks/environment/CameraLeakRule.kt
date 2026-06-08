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

@Rule(key = "GCI512")
class CameraLeakRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Failing to call android.hardware.Camera#release() can drain the battery in just a few hours."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        // Camera.open() is a static factory — match only calls whose receiver text is "Camera"
        val openCalls = calls.filter { call ->
            call.calleeExpression?.text == "open" &&
                (call.parent as? KtDotQualifiedExpression)?.receiverExpression?.text == "Camera"
        }
        if (openCalls.isEmpty()) return
        val hasRelease = calls.any { it.calleeExpression?.text == "release" && it.valueArguments.isEmpty() }
        if (hasRelease) return
        openCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
