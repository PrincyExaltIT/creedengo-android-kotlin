/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin

import com.sonarsource.plugins.kotlin.api.KotlinPluginExtensionsProvider
import io.creedengo.android.kotlin.checks.ClearCacheCheck
import io.creedengo.android.kotlin.checks.environment.BrightnessOverrideRule
import io.creedengo.android.kotlin.checks.environment.DurableWakeLockRule
import io.creedengo.android.kotlin.checks.environment.RigidAlarmRule
import io.creedengo.android.kotlin.checks.environment.ThriftyBluetoothLowEnergyRequestConnectionPriorityRule
import io.creedengo.android.kotlin.checks.environment.TorchFreeRule
import io.creedengo.android.kotlin.checks.environment.ContinuousRenderingRule
import io.creedengo.android.kotlin.checks.environment.ThriftyMotionSensorRule
import io.creedengo.android.kotlin.checks.environment.VibrationFreeRule
import io.creedengo.android.kotlin.checks.social.TrackingIdRule
import io.creedengo.android.kotlin.checks.environment.ThriftyBluetoothLowEnergySetAdvertiseModeRule
import io.creedengo.android.kotlin.checks.environment.ThriftyNotificationRule
import io.creedengo.android.kotlin.checks.environment.KeepCpuOnRule
import io.creedengo.android.kotlin.checks.environment.KeepScreenOnAddFlagsRule
import io.creedengo.android.kotlin.checks.environment.KeepScreenOnSetFlagsRule
import org.sonar.api.scanner.ScannerSide

/**
 * Registers Creedengo rule classes with the sonar-kotlin analysis engine.
 *
 * Without this provider, the rule classes are never instantiated by the scanner,
 * so their visitor methods never run and no issues are reported. The provider
 * also tells sonar-kotlin to create the `creedengo-android-kotlin` repository
 * and auto-load rule metadata from `org/sonar/l10n/kotlin/rules/kotlin/`.
 *
 * The third argument to `registerRule(...)` controls inclusion in the default
 * "Sonar way" profile — we set it to false because our rules live in the
 * dedicated `creedengo way` profile.
 */
@ScannerSide
class CreedengoKotlinExtensionsProvider : KotlinPluginExtensionsProvider {

    override fun registerKotlinPluginExtensions(extensions: KotlinPluginExtensionsProvider.Extensions) {
        extensions.registerRepository(
            AndroidKotlinRulesDefinition.REPOSITORY_KEY,
            AndroidKotlinRulesDefinition.REPOSITORY_NAME,
        )

        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, ClearCacheCheck::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, BrightnessOverrideRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, KeepScreenOnAddFlagsRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, KeepScreenOnSetFlagsRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, KeepCpuOnRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, DurableWakeLockRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, RigidAlarmRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, ThriftyNotificationRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, ThriftyBluetoothLowEnergySetAdvertiseModeRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, ThriftyBluetoothLowEnergyRequestConnectionPriorityRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, TorchFreeRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, ContinuousRenderingRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, ThriftyMotionSensorRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, VibrationFreeRule::class.java, false)
        extensions.registerRule(AndroidKotlinRulesDefinition.REPOSITORY_KEY, TrackingIdRule::class.java, false)
    }
}
