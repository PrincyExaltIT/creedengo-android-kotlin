package io.creedengo.android.kotlin.checks.environment

import android.view.WindowManager

// POSITIVE CASES - Should trigger GCI522

fun testBrightnessFullValueInt() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = 1 // Noncompliant {{Forcing brightness to max value may cause useless energy consumption.}}
}

fun testBrightnessFullValueFloat() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = 1f // Noncompliant {{Forcing brightness to max value may cause useless energy consumption.}}
}

fun testBrightnessFullValueFloatWithZero() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = 1.0f // Noncompliant {{Forcing brightness to max value may cause useless energy consumption.}}
}

fun testBrightnessFullValueDouble() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = 1.0 // Noncompliant {{Forcing brightness to max value may cause useless energy consumption.}}
}

fun testNestedBrightnessFullValue() {
    val windowManager = WindowManager()
    val params = windowManager.layoutParams
    params.screenBrightness = 1 // Noncompliant {{Forcing brightness to max value may cause useless energy consumption.}}
}

fun testBrightnessWithConstant() {
    val params = WindowManager.LayoutParams()
    val fullBrightness = 1
    params.screenBrightness = fullBrightness // OK - constant reference, not literal
}

// NEGATIVE CASES - Should NOT trigger GCI522

fun testBrightnessPartialValueFloat() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = 0.5f // OK - partial brightness
}

fun testBrightnessZero() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = 0 // OK - zero value
}

fun testBrightnessOtherValue() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = 2 // OK - other value
}

fun testBrightnessNegativeValue() {
    val params = WindowManager.LayoutParams()
    params.screenBrightness = -1 // OK - negative value
}

fun testDifferentMember() {
    val params = WindowManager.LayoutParams()
    params.alpha = 1 // OK - different member
}

fun testNoAssignment() {
    val params = WindowManager.LayoutParams()
    // Just reading the value should be fine
    val brightness = params.screenBrightness
}
