buildscript {
    repositories {
        mavenCentral()
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'
        classpath 'org.robovm:robovm-gradle-plugin:1.12.0'
    }
}

allprojects {
    apply plugin: "eclipse"
    apply plugin: "idea"

    version = '1.0'
    ext {
        appName = "GameRuntime"
        gdxVersion = '1.8.0'
        roboVMVersion = '1.12.0'
    }

    repositories {
        mavenCentral()
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
        maven { url "https://oss.sonatype.org/content/repositories/releases/" }
    }
}

project(":desktop") {
    apply plugin: "java"


    dependencies {
        compile project(":core")
        compile "com.badlogicgames.gdx:gdx-backend-lwjgl:$gdxVersion"
        compile "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop"
        compile "com.badlogicgames.gdx:gdx-tools:$gdxVersion"
        compile fileTree(dir: 'libs', include: '*.jar')
    }
}

project(":android") {
    apply plugin: "android"

    configurations { natives }

    dependencies {
        compile project(":core")
        compile "com.badlogicgames.gdx:gdx-backend-android:$gdxVersion"
        natives "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi"
        natives "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a"
        natives "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86"
        compile fileTree(dir: '../core/libs', include: '*.jar')
        compile fileTree(dir: 'libs', include: '*.so')
    }
}

project(":ios") {
    apply plugin: "java"
    apply plugin: "robovm"


    dependencies {
        compile project(":core")
        compile "org.robovm:robovm-rt:$roboVMVersion"
        compile "org.robovm:robovm-cocoatouch:$roboVMVersion"
        compile "com.badlogicgames.gdx:gdx-backend-robovm:$gdxVersion"
        compile "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-ios"
    }
}

project(":core") {
    apply plugin: "java"


    dependencies {
        compile "com.badlogicgames.gdx:gdx:$gdxVersion"
        compile fileTree(dir: 'libs', include: '*.jar')
    }
}

tasks.eclipse.doLast {
    delete ".project"
}