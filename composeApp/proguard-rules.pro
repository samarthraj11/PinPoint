# PinPoint ProGuard Rules

# General Android rules
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep application class
-keep class com.pinpoint.PinPointApplication { *; }

# Keep data classes used with Firebase Realtime Database
-keepclassmembers class com.pinpoint.domain.model.** { *; }
-keepclassmembers class com.pinpoint.feature.map.MapScreenState { *; }
-keepclassmembers class com.pinpoint.feature.map.MemberLocation { *; }
