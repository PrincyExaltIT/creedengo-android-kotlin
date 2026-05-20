package io.creedengo.android.kotlin.checks.environment

import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.sonar.check.Rule
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.frontend.KotlinFileContext

@Rule(key = "GCI522")
@DeprecatedRuleKey(repositoryKey = "creedengo-android-java", ruleKey = "EC522")
@DeprecatedRuleKey(repositoryKey = "ecocode-android-java", ruleKey = "ESOB002")
class BrightnessOverrideRule : AbstractCheck() {

    companion object {
        const val MESSAGE = "Forcing brightness to max value may cause useless energy consumption."
        private const val MEMBER_NAME = "screenBrightness"
        private const val BRIGHTNESS_FULL_VALUE_FLOAT = 1.0f
        private const val BRIGHTNESS_FULL_VALUE_INT = 1
    }

    override fun visitBinaryExpression(
        expression: KtBinaryExpression,
        kotlinFileContext: KotlinFileContext
    ) {
        // On ne s'intéresse qu'aux assignations (=)
        if (expression.operationToken != KtTokens.EQ) return

        val left = expression.left ?: return
        val right = expression.right ?: return

        // Vérifie que le membre gauche est bien layoutParams.screenBrightness
        if (!isBrightnessAssignment(left)) return

        // Vérifie que la valeur assignée vaut 1
        if (isBrightnessFullValue(right)) {
            kotlinFileContext.reportIssue(right, MESSAGE)
        }
    }

    /**
     * Vérifie que l'expression gauche est une référence à
     * android.view.WindowManager.LayoutParams.screenBrightness
     */
    private fun isBrightnessAssignment(expression: KtExpression): Boolean {
        // Doit être de la forme : quelqueChose.screenBrightness
        if (expression !is KtDotQualifiedExpression) return false

        val selector = expression.selectorExpression
        if (selector !is KtNameReferenceExpression) return false

        return selector.getReferencedName() == MEMBER_NAME
    }

    /**
     * Vérifie que la valeur assignée est égale à 1 (int ou float).
     * Gère : littéraux directs (1, 1f, 1.0f) et constantes résolues.
     */
    private fun isBrightnessFullValue(expression: KtExpression): Boolean {
        if (expression !is KtConstantExpression) return false

        val text = expression.text
            .trimEnd('f', 'F')
            .trimEnd('.')

        return text.toFloatOrNull() == BRIGHTNESS_FULL_VALUE_FLOAT
                || text.toIntOrNull() == BRIGHTNESS_FULL_VALUE_INT
    }
}