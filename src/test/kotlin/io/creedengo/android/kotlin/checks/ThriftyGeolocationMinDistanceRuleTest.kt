/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ThriftyGeolocationMinDistanceRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ThriftyGeolocationMinDistanceRuleTest {

    @Test
    fun `is annotated with rule key GCI527`() {
        val annotation = ThriftyGeolocationMinDistanceRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI527")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(ThriftyGeolocationMinDistanceRule::class.java)
    }

    @Test
    fun `message mentions distance greater than 0`() {
        assertThat(ThriftyGeolocationMinDistanceRule.MESSAGE).contains("distance").contains("0")
    }

    @Test
    fun `visits one function matcher for requestLocationUpdates`() {
        assertThat(ThriftyGeolocationMinDistanceRule().functionsToVisit).hasSize(1)
    }
}
