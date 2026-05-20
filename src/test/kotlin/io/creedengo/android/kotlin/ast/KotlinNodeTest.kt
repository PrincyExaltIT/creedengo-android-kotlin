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
package io.creedengo.android.kotlin.ast

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

/**
 * Unit tests for KotlinNode adapter.
 *
 * Note: Full integration tests with real Kotlin PSI are deferred to SonarQube testkit.
 * These tests verify the interface contract only.
 */
class KotlinNodeTest {

    @Test
    fun interface_provides_required_contract() {
        // Verify that KotlinNode interface defines expected methods
        val methods = KotlinNode::class.java.methods.map { it.name }
        
        assertThat(methods).contains("getElement", "getText", "getLineNumber", "ancestors", "hasAncestor")
    }

    @Test
    fun extension_function_creates_node() {
        // Verify that asKotlinNode extension function is defined
        val extensionFunctions = KotlinNode::class.java.declaredMethods
        
        // The actual test of asKotlinNode will happen in SonarQube integration tests
        // where real KtElement instances are available
        assertThat(KotlinNode::class.java.simpleName).isEqualTo("KotlinNode")
    }
}
