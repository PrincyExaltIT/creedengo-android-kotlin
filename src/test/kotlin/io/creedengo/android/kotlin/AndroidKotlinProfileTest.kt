/*
 * creedengo - Android Kotlin language - Provides rules to reduce the environmental footprint of your Kotlin Android applications
 * Copyright © 2026 Green Code Initiative (https://green-code-initiative.org/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.creedengo.android.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition

class AndroidKotlinProfileTest {

    @Test
    fun `profile references only known rule keys`() {
        val context = BuiltInQualityProfilesDefinition.Context()
        AndroidKotlinProfile().define(context)

        val profile = context.profile(AndroidKotlinRulesDefinition.LANGUAGE, AndroidKotlinProfile.PROFILE_NAME)
        assertThat(profile).isNotNull

        val profileKeys = profile!!.rules().map { it.ruleKey() }.toSet()

        assertThat(profileKeys).isEqualTo(setOf("GCI500", "GCI501", "GCI505", "GCI506", "GCI507", "GCI508", "GCI509", "GCI510", "GCI517", "GCI521", "GCI522", "GCI523", "GCI525", "GCI526", "GCI527", "GCI528", "GCI529", "GCI530", "GCI531", "GCI534", "GCI600"))
    }
}
