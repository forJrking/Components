package com.forjrking.dep

/**
 * @description:
 * @author: forjrking
 * @date: 2020/8/29 6:46 PM
 */

object Dependencies {

    object versions {
        val appcompat = "1.1.0"
        val material = "1.1.0"
        val extensions = "2.2.0"
        val constraint = "1.1.3"
        val annotation = "1.1.0"
        val dagger2 = "2.26"
        val room = "2.2.5"
        val support = "28.0.0"
        val retrofit = "2.3.0"
        val rxjava = "2.1.9"
        val kotlin_stdlib_jdk7 = "1.3.41"
        val kts_core = "1.3.0"
        val junit = "4.12"
        val junitExt = "1.1.1"
        val espressoCore = "3.2.0"
    }

    object test {
        val junit = "junit:junit:${versions.junit}"
        val junitExt = "androidx.test.ext:junit:${versions.junitExt}"
        val espressoCore = "androidx.test.espresso:espresso-core:${versions.espressoCore}"
    }

    object androidx {

        val appcompat = "androidx.appcompat:appcompat:${versions.appcompat}"
        val material = "com.google.android.material:material:${versions.material}"
        val core_ktx = "androidx.core:core-ktx:1.3.1"
        val constraint_layout =
            "androidx.constraintlayout:constraintlayout:${versions.constraint}"
        val extensions = "androidx.lifecycle:lifecycle-extensions:${versions.extensions}"
        val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${versions.extensions}"
        val annotation = "androidx.annotation:annotation:1.1.0"
    }
}