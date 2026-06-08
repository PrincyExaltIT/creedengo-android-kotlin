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

@Rule(key = "GCI513")
class LocationLeakRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Failing to call android.location.LocationManager#removeUpdates() can drain the battery in just a few hours."
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val calls = PsiTreeUtil.collectElementsOfType(file, KtCallExpression::class.java)
        val requestCalls = calls.filter { it.calleeExpression?.text == "requestLocationUpdates" }
        if (requestCalls.isEmpty()) return
        val hasRemove = calls.any { it.calleeExpression?.text == "removeUpdates" }
        if (hasRemove) return
        requestCalls.forEach { data.reportIssue(it.calleeExpression!!, MESSAGE) }
    }
}
