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
<<<<<<<< HEAD:src/test/kotlin/io/creedengo/android/kotlin/checks/ClearCacheCheckTest.kt
package io.creedengo.android.kotlin.checks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck

class ClearCacheCheckTest {

    @Test
    fun `is annotated with rule key GCI600`() {
        val annotation = ClearCacheCheck::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI600")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(ClearCacheCheck::class.java)
    }

    @Test
    fun `MESSAGE mentions the cache anti-pattern`() {
        assertThat(ClearCacheCheck.MESSAGE)
            .containsIgnoringCase("cache")
            .contains("rebuild")
    }
========
package io.creedengo.android.kotlin

import io.creedengo.android.kotlin.checks.ClearCacheCheck
import io.creedengo.android.kotlin.checks.LogInLoopCheck
import io.creedengo.android.kotlin.checks.ThreadSleepCheck
import org.sonarsource.kotlin.api.checks.KotlinCheck

object AndroidKotlinCheckList {

    fun checks(): List<Class<out KotlinCheck>> = listOf(
        ClearCacheCheck::class.java,
        ThreadSleepCheck::class.java,
        LogInLoopCheck::class.java
    )
>>>>>>>> 697e0e0 (feat: Readapt kotlin provider and build):src/main/kotlin/io/creedengo/android/kotlin/AndroidKotlinCheckList.kt
}
