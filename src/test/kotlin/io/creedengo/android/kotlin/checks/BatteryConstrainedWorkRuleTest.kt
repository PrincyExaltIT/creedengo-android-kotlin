/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.BatteryConstrainedWorkRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class BatteryConstrainedWorkRuleTest {

    @Test
    fun `is annotated with rule key GCI553`() {
        val annotation = BatteryConstrainedWorkRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI553")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(BatteryConstrainedWorkRule::class.java)
    }

    @Test
    fun `MESSAGE mentions battery constraint`() {
        assertThat(BatteryConstrainedWorkRule.MESSAGE)
            .contains("battery")
            .contains("WorkManager")
    }
}
