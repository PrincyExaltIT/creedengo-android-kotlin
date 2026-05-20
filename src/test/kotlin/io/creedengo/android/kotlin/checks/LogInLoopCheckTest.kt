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
package io.creedengo.android.kotlin.checks

import io.creedengo.android.kotlin.AndroidKotlinCheckList
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import java.util.Objects

/**
 * Tests for LogInLoopCheck (GCI602).
 */
class LogInLoopCheckTest {

    @Test
    fun rule_annotation_is_present() {
        val annotation = LogInLoopCheck::class.java.getAnnotation(Rule::class.java)
        Objects.requireNonNull(annotation, "LogInLoopCheck must have @Rule annotation")
        check(annotation!!.key == "GCI602") { "Expected GCI602 but got ${annotation.key}" }
    }

    @Test
    fun rule_is_registered_in_checklist() {
        val checks = AndroidKotlinCheckList.checks()
        val logInLoopCheckPresent = checks.any { it == LogInLoopCheck::class.java }
        check(logInLoopCheckPresent) { "LogInLoopCheck not registered in AndroidKotlinCheckList" }
    }
}
