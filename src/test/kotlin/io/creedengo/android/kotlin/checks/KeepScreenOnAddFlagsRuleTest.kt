import io.creedengo.android.kotlin.checks.ClearCacheCheck
import io.creedengo.android.kotlin.checks.environment.KeepScreenOnAddFlagsRule
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.sonar.check.Rule
import org.sonarsource.kotlin.api.checks.AbstractCheck
import org.sonarsource.kotlin.api.checks.FunMatcher

class KeepScreenOnAddFlagsRuleTest {

    private val screen = KeepScreenOnAddFlagsRule()
    @Test
    fun `is annotated with rule key GCI600`() {
        val annotation = KeepScreenOnAddFlagsRule::class.java.getAnnotation(Rule::class.java)
        assertThat(annotation).isNotNull
        assertThat(annotation.key).isEqualTo("GCI505")
    }

    @Test
    fun `extends sonar-kotlin AbstractCheck`() {
        assertThat(AbstractCheck::class.java).isAssignableFrom(KeepScreenOnAddFlagsRule::class.java)
    }

    @Test
    fun `MESSAGE mentions the cache anti-pattern`() {
        assertThat(screen.getMessage()).isEqualTo("Keeping the screen on should be avoided to avoid draining the battery.")
    }
}