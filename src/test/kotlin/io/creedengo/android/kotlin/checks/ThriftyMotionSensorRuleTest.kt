/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ThriftyMotionSensorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ThriftyMotionSensorRuleTest {

    @Test
    fun `is annotated with rule key GCI521`() {
        val annotation = ThriftyMotionSensorRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI521")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(ThriftyMotionSensorRule::class.java)
    }

    @Test
    fun `message mentions TYPE_GEOMAGNETIC_ROTATION_VECTOR`() {
        assertThat(ThriftyMotionSensorRule.MESSAGE).contains("TYPE_GEOMAGNETIC_ROTATION_VECTOR")
    }

    @Test
    fun `visits one function matcher for getDefaultSensor`() {
        assertThat(ThriftyMotionSensorRule().functionsToVisit).hasSize(1)
    }
}
