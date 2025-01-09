# React-Backend-Plugin
This is a `gradle custom plugin` project with `spring-boot` and `openapi-generator` integrations.  

Another project like `poc-gradle-plugin-consumer` is a consumer of this plugin.  
- https://github.com/siukatech/poc-gradle-plugin-consumer  

The `react-backend-` projects will be applied with this plugin as well later, e.g. 
- react-backend-core, https://github.com/siukatech/poc-react-backend-core
- react-backend-app, https://github.com/siukatech/poc-react-backend-app
- and etc  



**Reference:**  
https://discuss.gradle.org/t/standalone-plugin-to-apply-and-import-3rd-party-plugins/34397/4  
https://tutorialpoint.com/gradle/gradle_plugins.html  
https://www.youtube.com/watch?v=F3DF6bQo6jk  



# Set-up
## Project
### Gradle
```shell
mkdir project-dir
cd project-dir
gradle init

Select type of build to generate:
  1: Application
  2: Library
  3: Gradle plugin
  4: Basic (build structure only)
Enter selection (default: Application) [1..4] 4

Project name (default: project-dir): 

Select build script DSL:
  1: Kotlin
  2: Groovy
Enter selection (default: Kotlin) [1..2] 2

Generate build using new APIs and behavior (some features may change in the next minor release)? (default: no) [yes, no] 


> Task :init
To learn more about Gradle by exploring our Samples at https://docs.gradle.org/8.7/samples

BUILD SUCCESSFUL in 13s
1 actionable task: 1 executed
```

### Git
```shell
git init
git switch --create main
git touch README.md
git add .
git commit -m "feat: initial commit"
git push origin main
```


# Development
## build.gradle
Open `build.gradle` and indicate the following `gradle-plugin`.  
```groovy
...
plugins {
    id 'groovy'
    id 'java-gradle-plugin'
    id 'maven-publish'
}
...
```

Add `group` and `version`.  
```groovy
...
group = 'com.siukatech.poc'
version = '0.0.1-SNAPSHOT'
...
```

Add `repositories` and `dependencies`.  
```groovy
...
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation gradleApi()
    implementation localGroovy()
}
...
```

Add the `gradlePlugin` to indicate this is a `gradle custom plugin`.  
- `id` is the plugin id that will be used when applying this custom plugin.  
- `implementationClass` is the `main` class of this custom plugin.  
```groovy
...
gradlePlugin {
    plugins {
        myPlugin {
            id = 'com.siukatech.poc.react-backend-plugin'
            implementationClass = 'com.siukatech.poc.react.backend.plugin.ReactBackendPlugin'
        }
    }
}
...
```

Finally, add the `publishing` section for publishing this plugin to maven repository.  
The repository `maven` here is using for debug purpose.  
The generated plugin will be saved to `${buildDir}/repo` when gradle tasks are executed.
- publishMyPluginPluginMakerMavenPublicationToMavenRepository
- publishPluginMavenPublicationToMavenRepository
- publishPrivateMavenPublicationToMavenRepository

```groovy
...
publishing {
    repositories {
        maven {
            // buildDir -> project.layout.buildDirectory.getAsFile().get().path
            url = "${project.layout.buildDirectory.getAsFile().get().path}/repo"
        }
    }
    publications {
        privateMaven(MavenPublication) {
            def artifactIdStr = "$project.name"
            groupId "$project.group"
            version "$project.version"
            artifactId "$artifactIdStr"
//			from components.javaPlatform
            from components.java
            versionMapping {
                usage('java-api') {
                    fromResolutionOf('runtimeClasspath')
                }
                usage('java-runtime') {
                    fromResolutionResult()
                }
            }
            pom {
                name = "$artifactId"
                description = "$artifactId"
            }

        }
    }
    if (project.hasProperty("platformSnapshotUri")) {
        repositories {
            maven {
                name = 'platformSnapshot'
                allowInsecureProtocol = true
                credentials(PasswordCredentials)
                url = uri("$platformSnapshotUri")
            }
        }
    }
}
...
```


## settings.gradle
In the consumer project, update `settings.gradle` is required to let plugin manager can resolve this custom plugin from `mavenLocal`.  


## ImplementationClass
The `main` class of this custom plugin.  

**Reference:**  
https://tutorialpoint.com/gradle/gradle_plugins.html  
https://youtu.be/F3DF6bQo6jk?t=543 (09:03)  

```groovy
package com.siukatech.poc.react.backend.plugin

class ReactBackendPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // Implement custom logic here
        ...
    }
}
```


## Extension ( = Convention, obsolete)
The `config` of this custom plugin. Registration is required when applying this plugin.    
```groovy
...
// Register the extension (config, = convention, obsolete) for this plugin
project.extensions.create(ReactBackendExtension.EXTENSION_NAME, ReactBackendExtension)
...
```

In the consumer project's `build.gradle`, the extension can be defined like this.  
```groovy
...
reactBackendConfig {
    specUri = "https://raw.githubusercontent.com/siukatech/openapi-spec/main/openapi-generator-example/pocreact-v0.0.1.yaml"
    packageRoot = "com.siukatech.poc.generated"
    schemaMappingsMap = [
            "SimpleDiscPageResult": "org.springframework.data.domain.Page"
            , "ComplexDiscPageResult": "org.springframework.data.domain.Page"
    ]
}
...
```


## Integration
### io.spring.dependency-management
In `build.gradle`, add new dependencies as below to load the required `SpringBootPlugin` and `SpringSecurityCoreVersion` into the project.  
```groovy
...
dependencies {
    ...
    implementation "org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}"
    implementation "org.springframework.security:spring-security-test:${springSecurityVersion}"
    ...
}
...
```

The dependency version here will affect the project that applied this plugin.  
For example, this plugin used `3.3.1`, then those project(s) that adapting this plugin MUST upgrade to `3.3.1`.  
As a result, static variables or methods are provided to extract this information from this plugin.  
```groovy
...
import com.siukatech.poc.react.backend.plugin.ReactBackendPlugin
import com.siukatech.poc.react.backend.plugin.ReactBackendUtil
...
ext {
    ... // static variables from ReactBackendPlugin
    springBootVersion = "${ReactBackendPlugin.SPRING_BOOT_VERSION}"
    springSecurityVersion = "${ReactBackendPlugin.SPRING_SECURITY_VERSION}"
    openapiGeneratorVersion = "${ReactBackendPlugin.OPENAPI_GENERATOR_VERSION}"

    ... // static methods from ReactBackendUtil
    springBootVersion = "${ReactBackendUtil.extractSpringBootVersion()}"
    springSecurityVersion = "${ReactBackendUtil.extractSpringSecurityVersion()}"
    openapiGeneratorVersion = "${ReactBackendUtil.extractOpenapiGeneratorVersion()}"
}
...
```

Once the dependencies added, the plugin classes can be used to perform the `withType` action.  
When the `SpringBootPlugin` applied, this custom plugin will start to do the related logic inside in this block.  
Logics now are placed in `SpringBootPluginHandler`.  
```groovy
...
project.plugins.withType(SpringBootPlugin) {
    ...
}
...
```

**Example:**  
Exclude the application*.yml during `bootJar` task.  
```groovy
...
project.tasks.named("bootJar", BootJar) {
    doLast {
        exclude('application*.properties', 'application*.yml')
    }
}
...
```

Apply jvmArgs when `bootRun` task executed.    
```groovy
...
project.tasks.named("bootRun", BootRun) {
    println("handle - bootRun - System.getenv: [" + System.getenv("jvmArgs") + "]")
    println("handle - bootRun - System.getpropertu: [" + System.getProperty("jvmArgs") + "]")
    def jvmArgsStr = System.getProperty("jvmArgs")
    def jvmArgsList = jvmArgsStr == null ? [] : jvmArgsStr.split('\\s+') as List
    println("handle - bootRun - jvmArgsStr: [" + jvmArgsStr + "], jvmArgsList: [" + jvmArgsList + "]")
    jvmArgs(
//        ["-Duser.timezone=UTC"]
        jvmArgsList
    )
}
...
```

### org.openapi.generator
In `build.gradle`, add new dependencies as below to load the required `OpenApiGeneratorPlugin` into the project.
```groovy
dependencies {
    ...
    implementation "org.openapitools:openapi-generator-gradle-plugin:${openapiGeneratorVersion}"
    ...
}
```

Same as `SpringBootPlugin` handling, applying the setting to extension (obsolete: convention) under `withType` with `OpenApiGeneratorPlugin` in `OpenApiGeneratorPluginHandler`.  
```groovy
...
project.plugins.withType(OpenApiGeneratorPlugin) {
    project.tasks.named("openApiGenerate", GenerateTask) {
        // Resolve the task "clean" and set is as a dependency of this GenerateTask
        Task cleanTask = project.tasks.findByName("clean")
        dependsOn(cleanTask)
        ...
        generatorName.set("spring")
        ...
    }
}
...
```

Moreover, using the input from `ReactBackendExtension` can help to centralize the `openapi-generator`'s config in this custom plugin for customization.  
```groovy
package com.siukatech.poc.react.backend.plugin

class ReactBackendExtension {
    ...
    String specUri
    String packageRoot
    Map<String, String> schemaMappingsMap
    ...
}
```

In `OpenApiGeneratorPluginHandler`, only expose limited configuration.  
```groovy
...
if (ReactBackendUtil.isLocalSpecUri(reactBackendConfig.specUri)) {
    inputSpec.set(reactBackendConfig.specUri)
} else {
    remoteInputSpec.set(reactBackendConfig.specUri)
}
...
apiPackage.set(reactBackendConfig.packageRoot + ".api")
invokerPackage.set(reactBackendConfig.packageRoot + ".invoker")
modelPackage.set(reactBackendConfig.packageRoot + ".model")
...
// Append custom schemaMappings from ReactBackendPlugin
Map<String, String> schemaMappingsMap = [
    Pageable: "org.springframework.data.domain.Pageable"
    ,
    Page: "org.springframework.data.domain.Page"
]
schemaMappingsMap.putAll(reactBackendConfig.schemaMappingsMap)
schemaMappings.set(schemaMappingsMap)
...
```

Also update the `sourceSets` after code generation in `OpenApiGeneratorPluginHandler`.  
```groovy
...
String generatedDir = "$buildDir/generated"
...
// Update the sourceSets with generated path when OpenApiGeneratorPlugin is applied
project.plugins.withType(JavaPlugin) {
    JavaPlugin jp = project.plugins.findPlugin(JavaPlugin)
    JavaPluginExtension jpe = project.extensions.getByType(JavaPluginExtension)
    jpe.sourceSets {
        main {
            java {
                srcDir("${generatedDir}/src/main/java")
            }
        }
    }
}
...
```
