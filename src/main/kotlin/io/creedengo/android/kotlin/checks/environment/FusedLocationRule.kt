/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.psi.KtFile
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI517")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC517")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EOPT001")
class FusedLocationRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Use com.google.android.gms.location instead of android.location to maximize battery life."
        const val WRONG_IMPORT = "android.location"
        const val GOOD_IMPORT = "com.google.android.gms.location"
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val imports = file.importDirectives
        val badImports = imports.filter {
            it.importedFqName?.asString()?.startsWith(WRONG_IMPORT) == true
        }
        if (badImports.isEmpty()) return
        val hasGoodImport = imports.any {
            it.importedFqName?.asString()?.startsWith(GOOD_IMPORT) == true
        }
        if (!hasGoodImport) {
            badImports.forEach { data.reportIssue(it, MESSAGE) }
        }
    }
}
