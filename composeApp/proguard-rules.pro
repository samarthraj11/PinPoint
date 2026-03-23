# PinPoint ProGuard Rules

# General Android rules
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep application class
-keep class com.pinpoint.PinPointApplication { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-dontwarn dagger.hilt.**

# Compose
-dontwarn androidx.compose.**

# Orbit MVI
-keep class org.orbitmvi.orbit.** { *; }
-dontwarn org.orbitmvi.orbit.**

# Compose Destinations
-keep class com.ramcosta.composedestinations.** { *; }
-dontwarn com.ramcosta.composedestinations.**

# Credential Manager
-keep class androidx.credentials.** { *; }
-dontwarn androidx.credentials.**

# Google Identity
-keep class com.google.android.libraries.identity.** { *; }
-dontwarn com.google.android.libraries.identity.**

# Coil
-dontwarn coil.**
-keep class coil.** { *; }

# Keep data classes used with Firebase Realtime Database
-keepclassmembers class com.pinpoint.domain.model.** { *; }
-keepclassmembers class com.pinpoint.feature.map.MapScreenState { *; }
-keepclassmembers class com.pinpoint.feature.map.MemberLocation { *; }
