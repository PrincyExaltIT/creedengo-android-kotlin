/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ThriftyGeolocationCriteriaRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ThriftyGeolocationCriteriaRuleTest {

    @Test
    fun `is annotated with rule key GCI524`() {
        val annotation = ThriftyGeolocationCriteriaRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI524")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(ThriftyGeolocationCriteriaRule::class.java)
    }

    @Test
    fun `message constants mention battery`() {
        assertThat(ThriftyGeolocationCriteriaRule.MESSAGE_REQUEST).contains("battery")
        assertThat(ThriftyGeolocationCriteriaRule.MESSAGE_BEST_PROVIDER).contains("POWER_LOW")
        assertThat(ThriftyGeolocationCriteriaRule.MESSAGE_SET_POWER).contains("POWER_LOW")
    }
}
