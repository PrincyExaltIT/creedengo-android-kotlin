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

import org.jetbrains.kotlin.psi.KtElement

/**
 * Lightweight wrapper exposing KtElement to checks without coupling to PSI internals.
 * Façade unifiée pour les règles syntaxiques.
 *
 * Provides a stable contract:
 * - `element` : access to the underlying KtElement
 * - `text` : raw source text
 * - `lineNumber` : source location
 * - `ancestors()` : traverse parent hierarchy
 */
interface KotlinNode {
    val element: KtElement
    val text: String
    val lineNumber: Int

    fun ancestors(): Sequence<KotlinNode>
    fun hasAncestor(predicate: (KotlinNode) -> Boolean): Boolean = ancestors().any(predicate)
}

/**
 * Default implementation adapting KtElement → KotlinNode.
 */
internal class DefaultKotlinNode(override val element: KtElement) : KotlinNode {
    override val text: String
        get() = element.text

    override val lineNumber: Int
        get() {
            // Use getTextOffset() which is available on all PsiElement subclasses
            return try {
                val offset = element.textOffset
                val file = element.containingFile
                val document = file?.viewProvider?.document
                if (document != null) {
                    document.getLineNumber(offset)
                } else {
                    -1
                }
            } catch (e: Exception) {
                -1
            }
        }

    override fun ancestors(): Sequence<KotlinNode> = sequence {
        var parent = element.parent
        while (parent is KtElement) {
            yield(DefaultKotlinNode(parent))
            parent = parent.parent
        }
    }
}

/**
 * Extension function to convert a KtElement to a KotlinNode.
 */
fun KtElement.asKotlinNode(): KotlinNode = DefaultKotlinNode(this)
