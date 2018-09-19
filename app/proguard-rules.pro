# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable


# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 代码混淆压缩比，在0~7之间，默认为5
-optimizationpasses 5

# Activity用在了AndroidManifest.xml
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

-keep class org.simalliance.openmobileapi.** {*;}
-keep class Decoder.** {*;}
-keep class org.simeid.sdk.** {*;}
-keep class com.tencent.bugly.**{*;}
-keep class android.support.** {*;}
-keep class com.squareup.javawriter.** {*;}
-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep class retrofit2.** {*;}
-keep class io.reactivex.** {*;}
-keep class com.orhanobut.logger.** {*;}
-keep class com.jakewharton.retrofit2.** {*;}
-keep class com.jakewharton.rxbinding2.** {*;}
-keep class com.trycatch.** {*;}
-keep class com.daimajia.** {*;}
-keep class com.sdsmdg.** {*;}
-keep class com.appeaser.** {*;}
-keep class com.miguelcatalan.** {*;}
-keep class com.google.android.gms.** {*;}
-keep class com.google.firebase.** {*;}
-keep class com.google.gson.** {*;}
-keep class junit.framework.** {*;}
-keep class org.hamcrest.** {*;}
-keep class org.junit.** {*;}

-keep class **.R$* {*;}

#org.xmlpull.v1.XmlPullParser
-keep class org.xmlpull.v1.** {*;}
# net 用到反射
-keep class com.yiyun.lockcontroller.net.** {*;}
-keep class com.yiyun.lockcontroller.bean.** {*;}
# view用在xml
-keep class com.yiyun.lockcontroller.view.** {*;}

-dontwarn okio.**
-dontwarn org.simalliance.openmobileapi.**
-dontwarn com.tencent.bugly.**
-dontwarn retrofit2.**

#org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.**

