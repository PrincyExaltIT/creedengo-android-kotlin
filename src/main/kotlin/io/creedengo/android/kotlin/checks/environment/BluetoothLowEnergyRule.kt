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

@Rule(key = "GCI518")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC518")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "EOPT002")
class BluetoothLowEnergyRule : AbstractCheck() {

    companion object {
        const val ERROR_MESSAGE =
            "You are using Bluetooth. Did you take a look at the Bluetooth Low Energy API?"
        const val GOOD_PRACTICE_MESSAGE =
            "Using android.bluetooth.le.* is a good practice."

        private const val CLASSIC_BT_PREFIX = "android.bluetooth"
        private const val BLE_PREFIX = "android.bluetooth.le"
    }

    override fun visitKtFile(file: KtFile, data: KotlinFileContext) {
        val imports = file.importDirectives

        val bleImports = imports.filter {
            it.importedFqName?.asString()?.startsWith(BLE_PREFIX) == true
        }
        val classicImports = imports.filter {
            val name = it.importedFqName?.asString() ?: return@filter false
            name.startsWith(CLASSIC_BT_PREFIX) && !name.startsWith(BLE_PREFIX)
        }

        when {
            bleImports.isNotEmpty() ->
                // BLE present (alone or alongside classic) — positive signal
                bleImports.forEach { data.reportIssue(it, GOOD_PRACTICE_MESSAGE) }
            classicImports.isNotEmpty() ->
                // Only classic Bluetooth — encourage migration to BLE
                classicImports.forEach { data.reportIssue(it, ERROR_MESSAGE) }
        }
    }
}
