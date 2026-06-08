/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * Licensed under the GNU General Public License v3 — see LICENCE.md for details.
 */
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.checks.environment.FusedLocationRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class FusedLocationRuleTest {

    @Test
    fun `is annotated with rule key GCI517`() {
        val annotation = FusedLocationRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI517")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(FusedLocationRule::class.java)
    }

    @Test
    fun `MESSAGE references gms location package`() {
        assertThat(FusedLocationRule.MESSAGE).contains("com.google.android.gms.location")
    }

    @Test
    fun `WRONG_IMPORT is android dot location`() {
        assertThat(FusedLocationRule.WRONG_IMPORT).isEqualTo("android.location")
    }

    @Test
    fun `GOOD_IMPORT is gms location`() {
        assertThat(FusedLocationRule.GOOD_IMPORT).isEqualTo("com.google.android.gms.location")
    }
}
