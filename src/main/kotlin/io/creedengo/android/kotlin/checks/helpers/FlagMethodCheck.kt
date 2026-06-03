package io.creedengo.android.kotlin.checks.helpers

import org.jetbrains.kotlin.analysis.api.calls.KaFunctionCall
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.util.getResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.checks.CallAbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher
import org.sonarsource.kotlin.api.frontend.KotlinFileContext
import org.sonarsource.kotlin.api.reporting.message

abstract class FlagOnMethodCheck(
    methodName: String,
    fullyQualifiedClassName: String,
    private val flagValue: Int,
    private val parameterIndices: List<Int>
) : CallAbstractCheck() {

    abstract fun getMessage(): String

    // FunMatcher filtre déjà sur le nom de méthode ET le type de l'appelant
    override val functionsToVisit = listOf(
        FunMatcher(
            definingSupertype = fullyQualifiedClassName,
            name = methodName,)
    )

    override fun visitFunctionCall(
        callExpression: KtCallExpression,
        resolvedCall: KaFunctionCall<*>,
        kotlinFileContext: KotlinFileContext
    ) {
        val arguments = resolvedCall.argumentMapping.keys.toList()
        val flagFound = parameterIndices.any { index ->
            val argExpression = arguments.getOrNull(index) ?: return@any false
            containsFlag(argExpression, flagValue)
        }
        if (flagFound) {
            kotlinFileContext.reportIssue(
                callExpression.calleeExpression!!,
                getMessage()
            )
        }
    }

    private fun containsFlag(expression: KtExpression, targetFlag: Int): Boolean =
        when (expression) {

            // Cas 1 : littéral entier décimal ou hex (0x00000080)
            is KtConstantExpression -> {
                val text = expression.text
                val value = when {
                    text.startsWith("0x") || text.startsWith("0X") ->
                        text.drop(2).toLongOrNull(16)?.toInt()
                    else ->
                        text.toLongOrNull()?.toInt()
                }
                value != null && (value and targetFlag) != 0
            }

            // Cas 2 : OR infix Kotlin — FLAG_A or FLAG_B
            is KtBinaryExpression -> {
                if (expression.operationToken == KtTokens.IDENTIFIER &&
                    expression.operationReference.text == "or"
                ) {
                    val left = expression.left ?: return false
                    val right = expression.right ?: return false
                    containsFlag(left, targetFlag) || containsFlag(right, targetFlag)
                } else false
            }

            // Cas 3 : référence simple — FLAG_KEEP_SCREEN_ON
            is KtNameReferenceExpression ->
                resolveIntConstant(expression)?.let { (it and targetFlag) != 0 } ?: false

            // Cas 4 : référence qualifiée — LayoutParams.FLAG_KEEP_SCREEN_ON
            is KtDotQualifiedExpression -> {
                val selector = expression.selectorExpression
                if (selector is KtNameReferenceExpression) {
                    resolveIntConstant(selector)?.let { (it and targetFlag) != 0 } ?: false
                } else false
            }

            else -> false
        }

    /**
     * Tente de résoudre la valeur entière d'une constante à la compilation.
     * Retourne null si la valeur ne peut pas être déterminée statiquement.
     */
    private fun resolveIntConstant(expression: KtNameReferenceExpression): Int? =
        expression.text
            .takeIf { it.startsWith("0x") || it.startsWith("0X") }
            ?.drop(2)
            ?.toIntOrNull(16)
    // La résolution complète de constante via K2 nécessite un analyze { } block
    // et est gérée dans les sous-classes si besoin
}