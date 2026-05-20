package samples

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager

/**
 * Sample code used to validate that the GCI522 rule (BrightnessOverrideRule)
 * fires on real-world Android assignments.
 *
 * Lines marked `// Noncompliant` MUST be flagged by SonarQube once the
 * Creedengo Android Kotlin plugin is installed. Lines marked `// Compliant`
 * MUST stay silent.
 */
class BrightnessOverrideSample : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ---- Cases that MUST trigger GCI522 ----

        val params = window.attributes
        params.screenBrightness = 1.0f                              // Noncompliant — float literal full brightness
        window.attributes.screenBrightness = 1f                     // Noncompliant — short float literal
        window.attributes.screenBrightness = 1.0                    // Noncompliant — double literal
        window.attributes.screenBrightness = 1                      // Noncompliant — int literal coerced

        val layoutParams: WindowManager.LayoutParams = window.attributes
        layoutParams.screenBrightness = 1.0F                        // Noncompliant — uppercase F suffix

        // ---- Cases that MUST stay silent ----

        window.attributes.screenBrightness = 0.5f                   // Compliant — half brightness
        window.attributes.screenBrightness = 0f                     // Compliant — minimum
        window.attributes.screenBrightness = -1f                    // Compliant — "use system default" sentinel
        window.attributes.screenBrightness = computeBrightness()    // Compliant — non-literal, unknown at compile time

        // Assignment to a *different* member with value 1 — must not trigger
        layoutParams.alpha = 1.0f                                   // Compliant — not screenBrightness

        // Apply the changes back so the sample is realistic
        window.attributes = params
    }

    private fun computeBrightness(): Float = 0.7f
}
