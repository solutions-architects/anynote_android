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

-dontwarn com.luckhost.data.localStorage.cache.images.ImageCacheStorageImpl
-dontwarn com.luckhost.data.localStorage.cache.images.ImageCacheStorageInterface
-dontwarn com.luckhost.data.localStorage.keys.hashes.HashStorage
-dontwarn com.luckhost.data.localStorage.keys.hashes.SharedPrefHashesStorage
-dontwarn com.luckhost.data.localStorage.keys.tokens.SharedPrefTokensStorage
-dontwarn com.luckhost.data.localStorage.keys.tokens.TokensStorage
-dontwarn com.luckhost.data.localStorage.materials.NotesStorage
-dontwarn com.luckhost.data.localStorage.materials.sqlite.SQLiteNotesStorage
-dontwarn com.luckhost.data.network.NetworkModule
-dontwarn com.luckhost.data.network.retrofit.RetrofitModule
-dontwarn com.luckhost.data.repository.AuthTokensRepoImpl
-dontwarn com.luckhost.data.repository.MediaCacheRepoImpl
-dontwarn com.luckhost.data.repository.NetworkServiceImpl
-dontwarn com.luckhost.data.repository.NoteHashesRepoImpl
-dontwarn com.luckhost.data.repository.NotesRepositoryImpl
