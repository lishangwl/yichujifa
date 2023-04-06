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

-dontwarn javax.naming.**

-keep public class com.xieqing.codeutils.util.Utils { *; }


# 保留opencv下的所有类及其内部类
-keep class org.opencv.** {*;}

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在类中的方法参数是view的方法，
# 这样以来我们在写的onClick就不会被影响
-keepclassmembers class * {
    public void *(android.view.View);
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keep class org.opencv.* {}

###排除所有注解类

-keep class * extends java.lang.annotation.Annotation { *; }
-keep class esqeee.xieqing.com.eeeeee.widget.* { *; }
-keep class * extends android.os.Parcelable { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keepclassmembers class * {
    @com.lidroid.xutils.db.annotation.* <fields>;
}
-keep @esqeee.xieqing.com.eeeeee.annotation.NoReproground class **  {}

-keep class com.yicu.yichujifa.ui.colorpicker.**{*;}


-keep enum esqeee.xieqing.com.eeeeee.annotation.* { *; }
-keepclassmembers class ** {
    @esqeee.xieqing.com.eeeeee.annotation.* <fields>;
}
-keepclassmembers class ** {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class ** {
    @esqeee.xieqing.com.eeeeee.annotation.NoReproground <fields>;
    @esqeee.xieqing.com.eeeeee.annotation.NoReproground <methods>;
}
-keepclassmembers class ** {
    @esqeee.xieqing.com.eeeeee.sql.Clounm <fields>;
}



-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}

-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#友盟---------------------------------------------
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#--------腾讯错误收集
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}



-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn com.squareup.picasso.**
-dontwarn com.bumptech.glide.**


#-----------DB FLOW
# 保留所有类及其内部类
-keep class com.raizlabs.** {*;}


# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**


#知乎
-keep class com.zhihu.matisse.** {*;}

#讯飞
-keep class com.iflytek.**{*;}
-keepattributes Signature


#tencent
-keep class com.tencent.mm.sdk.** {
     *;
}

#----------以下内容可根据情况开启混淆日志记录-------
##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt

#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，
#这里可以作为禁止log打印的功能使用，另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}
