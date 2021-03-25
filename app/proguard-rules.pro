-dontskipnonpubliclibraryclasses
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontpreverify
-verbose
-keepattributes *Annotation*
-dontoptimize

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-keep public class * extends android.app.Activity      
-keep public class * extends android.app.Application   
-keep public class * extends android.app.Service       
-keep public class * extends android.content.BroadcastReceiver  
-keep public class * extends android.content.ContentProvider    
-keep public class * extends android.app.backup.BackupAgentHelper 
-keep public class * extends android.preference.Preference        
-keep public class com.android.vending.licensing.ILicensingService    


-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

-keep public class * extends android.support.**
-keep class com.xxx.**{*;}
-dontwarn com.xxx**
-keepattributes Signature
-keepnames class * implements java.io.Serializable

-keepclassmembers class **.R$* {
　　public static <fields>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();    
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}