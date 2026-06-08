/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.ThriftyGeolocationMinTimeRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ThriftyGeolocationMinTimeRuleTest {

    @Test
    fun `is annotated with rule key GCI523`() {
        val annotation = ThriftyGeolocationMinTimeRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI523")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(ThriftyGeolocationMinTimeRule::class.java)
    }

    @Test
    fun `message mentions time greater than 0`() {
        assertThat(ThriftyGeolocationMinTimeRule.MESSAGE).contains("time").contains("0")
    }

    @Test
    fun `visits one function matcher for requestLocationUpdates`() {
        assertThat(ThriftyGeolocationMinTimeRule().functionsToVisit).hasSize(1)
    }
}
