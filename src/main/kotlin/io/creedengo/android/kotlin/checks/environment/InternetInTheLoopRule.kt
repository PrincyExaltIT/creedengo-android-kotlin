/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDoWhileExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtForExpression
import org.jetbrains.kotlin.psi.KtWhileExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI502")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC502")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EBOT001")
class InternetInTheLoopRule : CallAbstractCheck() {

    companion object {
        const val MESSAGE = "Internet connection should not be opened in loops to preserve the battery."
    }

    override val functionsToVisit = listOf(
        FunMatcher(definingSupertype = "java.net.URL", name = "openConnection")
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        if (isInsideLoop(callExpression)) {
            kotlinFileContext.reportIssue(callExpression.calleeExpression!!, MESSAGE)
        }
    }

    private fun isInsideLoop(element: PsiElement): Boolean {
        var parent = element.parent
        while (parent != null && parent !is KtFile) {
            when (parent) {
                is KtForExpression, is KtWhileExpression, is KtDoWhileExpression -> return true
            }
            parent = parent.parent
        }
        return false
    }
}
