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
import org.jetbrains.kotlin.psi.KtValueArgument
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI504")
class UncompressedDataTransmissionRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Prefer using GZIPOutputStream instead of OutputStream to improve energy efficiency."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
            .filter { it.calleeExpression?.text == "getOutputStream" && it.valueArguments.isEmpty() }
            .filter { !isWrappedInGzip(it) }
            .forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }

    /**
     * Returns true when the getOutputStream() call is the direct argument of GZIPOutputStream(...).
     * Handles both connection.getOutputStream() and plain getOutputStream() forms.
     */
    private fun isWrappedInGzip(call: KtCallExpression): Boolean {
        // Step up one level: allow for an enclosing dot-qualified expression
        val exprToCheck = if (call.parent is KtDotQualifiedExpression) call.parent else call
        // The expression must be a direct value argument of some outer call
        val valueArg = exprToCheck.parent as? KtValueArgument ?: return false
        // Skip KtValueArgumentList to reach the outer KtCallExpression
        val outerCall = valueArg.parent?.parent as? KtCallExpression ?: return false
        return outerCall.calleeExpression?.text == "GZIPOutputStream"
    }
}
