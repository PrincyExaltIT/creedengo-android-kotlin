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
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI503")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC503")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EBOT002")
class WifiMulticastLockRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Failing to call WifiManager.MulticastLock#release() can cause a noticeable battery drain."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)

        // Narrow to files that explicitly create a MulticastLock to avoid false positives
        // from other acquire/release pairs (e.g. PowerManager.WakeLock, Semaphore).
        val hasMulticastLock = calls.any { it.calleeExpression?.text == "createMulticastLock" }
        if (!hasMulticastLock) return

        val acquireCalls = calls.filter { call ->
            call.calleeExpression?.text == "acquire" && call.valueArguments.isEmpty()
        }
        if (acquireCalls.isEmpty()) return

        val hasRelease = calls.any { call ->
            call.calleeExpression?.text == "release" && call.valueArguments.isEmpty()
        }
        if (hasRelease) return

        acquireCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
