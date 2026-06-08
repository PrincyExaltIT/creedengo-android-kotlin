/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.social

import org.jetbrains.kotlin.psi.KtFile
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI532")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC532")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "SPRI002")
class GoogleTrackerRule : AbstractCheck() {

    companion object {
        const val GOOGLE_ANALYTICS_MESSAGE =
            "Using com.google.android.gms.analytics.* is a potential threat for privacy."
        const val FIREBASE_ANALYTICS_MESSAGE =
            "Using com.google.firebase.analytics.* is a potential threat for privacy."

        private const val GOOGLE_ANALYTICS_IMPORT = "com.google.android.gms.analytics"
        private const val FIREBASE_ANALYTICS_IMPORT = "com.google.firebase.analytics"
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        file.importDirectives.forEach { importDirective ->
            val importName = importDirective.importedFqName?.asString() ?: return@forEach
            when {
                importName.startsWith(GOOGLE_ANALYTICS_IMPORT) ->
                    data.reportIssue(importDirective, GOOGLE_ANALYTICS_MESSAGE)
                importName.startsWith(FIREBASE_ANALYTICS_IMPORT) ->
                    data.reportIssue(importDirective, FIREBASE_ANALYTICS_MESSAGE)
            }
        }
    }
}
