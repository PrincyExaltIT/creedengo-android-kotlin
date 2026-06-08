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

@Rule(key = "GCI533")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC533")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "SGDP001")
class GoogleAndroidAdsConsentRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Good Practice: User Consent — using the Google Consent SDK meets EU GDPR requirements."

        private const val ADS_CONSENT_IMPORT = "com.google.android.ads.consent"
        private const val UMP_IMPORT = "com.google.android.ump"
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        file.importDirectives.forEach { importDirective ->
            val importName = importDirective.importedFqName?.asString() ?: return@forEach
            if (importName.startsWith(ADS_CONSENT_IMPORT) || importName.startsWith(UMP_IMPORT)) {
                data.reportIssue(importDirective, MESSAGE)
            }
        }
    }
}
