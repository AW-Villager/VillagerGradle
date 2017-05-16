# VillagerGradle
人狼知能のGraldeプラグイン。
人狼知能の開発のサポートをします。


## プラグインを適用する

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url uri('https://aitech.ac.jp/maslab/~k14048kk/maven')
        }
    }
    dependencies {
        classpath 'awvillager:villagergradle:0.1-SNAPSHOT'
    }
}

apply plugin: 'awvillager.gradle'
```

## 使用する

VillagerGradleでは簡単に開発環境を構築できるタスクを追加します。

### 基本設定

build.gradleにプラグインの設定を書き込みます。

```groovy
aiwolf {
    version = '0.4.5-0.0.1';
}
```

### タスク一覧

setupWorkspace タスク。 開発環境を構築します。

```
$ gradle setupWorkspace 
```

