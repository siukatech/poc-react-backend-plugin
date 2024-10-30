package com.siukatech.poc.react.backend.plugin

import com.siukatech.poc.react.backend.plugin.handler.PluginHandler
import com.siukatech.poc.react.backend.plugin.handler.PluginHandlerManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.openapitools.generator.gradle.plugin.OpenApiGeneratorPlugin
import org.springframework.boot.gradle.plugin.DependencyManagementPluginAction
import org.springframework.boot.gradle.plugin.SpringBootPlugin

/**
 * Reference:
 * https://tutorialpoint.com/gradle/gradle_plugins.html
 * https://www.youtube.com/watch?v=F3DF6bQo6jk
 */
class ReactBackendPlugin implements Plugin<Project> {

    public static String PLUGIN_GROUP = "react-backend-plugin"

    public static List<String> PLUGIN_SIMPLE_NAMES = List.of(
            JavaPlugin.simpleName
            , GroovyPlugin.simpleName
            , MavenPublishPlugin.simpleName
            , SpringBootPlugin.simpleName
            , OpenApiGeneratorPlugin.simpleName
            , "MyPlugin"
    )

    public static String SPRING_BOOT_VERSION = ReactBackendUtil.extractSpringBootVersion()
    public static String SPRING_SECURITY_VERSION = ReactBackendUtil.extractSpringSecurityVersion()
    public static String OPENAPI_GENERATOR_VERSION = ReactBackendUtil.extractOpenapiGeneratorVersion()

    private PluginHandlerManager pluginHandlerManager;

    @Override
    void apply(Project project) {
        project.plugins.apply(SpringBootPlugin)
        pluginHandlerManager = new PluginHandlerManager(project)

        // Register the extension (config, obsolete: convention) for this plugin
        project.extensions.create(ReactBackendExtension.EXTENSION_NAME, ReactBackendExtension)

        // Resolve the build directory path from project, project.buildDir is deprecated
        String buildDir = ReactBackendUtil.resolveBuildDirPath(project)

//        println("apply - project.springBootVersion: [${project.springBootVersion}]")
//        println("apply - project.springSecurityVersion: [${project.springSecurityVersion}]")

        ReactBackendExtension reactBackendConfig = project.extensions.findByName(ReactBackendExtension.EXTENSION_NAME)


//        print_project_ext(project)
        print_project_version(project)

////        println("apply - getPluginHandlerMapSize: [" + pluginHandlerManager.getPluginHandlerMapSize() + "]")
////        List<String> simpleNameList = project.plugins.stream()
////                .peek {
////                    boolean isInterface = it.class.interface
////                    String clazzSimpleName = isInterface ? it.class.interfaces[0].class.simpleName : it.class.simpleName
////                    String implSimpleName = it.class.simpleName
////                    PluginHandler pluginHandler = pluginHandlerManager.resolvePluginHandler(it)
////                    println("apply - isInterface: [${isInterface}"
////                            + "], clazzSimpleName: [${clazzSimpleName}"
////                            + "], implSimpleName: [${implSimpleName}"
////                            + "], pluginHandler: [${pluginHandler == null ? "NULL" : pluginHandler.name}"
////                            + "]")
////                }
////                .map(it -> it.class.simpleName)
////                .toList()
////        simpleNameList.addAll(PLUGIN_SIMPLE_NAMES);
////        project.plugins.forEach {
////            PluginHandler pluginHandler = pluginHandlerManager.resolvePluginHandler(it)
////            println("apply - plugin.class.name: [${it.class.name}], pluginHandler: [${pluginHandler == null?"NULL":pluginHandler.name}]")
////            if (pluginHandler != null) {
////                pluginHandler.handle(project, buildDir, reactBackendConfig)
////            }
////        }
        PLUGIN_SIMPLE_NAMES.forEach {
            PluginHandler pluginHandler = pluginHandlerManager.resolvePluginHandler(it)
//            println("===")
//            println("apply - forEach - it-plugin-simpleName: [${it}], pluginHandler: [${pluginHandler == null ? "NULL" : pluginHandler.name}]")
//            println("===")
//            println("===")
//            println("===")
            if (pluginHandler != null) {
                pluginHandler.handle(project, buildDir, reactBackendConfig)
            }
        }


        project.plugins.withType(JavaPlugin) {

        }
//
//        // Example of creating a custom task
//        project.tasks.create('copyCompileLibs', Copy) {
////            from project.configurations.dependencies
//////            into "${project.buildDir/myCompileClasspath}"
////            into "${project.layout.buildDirectory/myCompileClasspath}"
//        }
//
//        // Use withType to associate with SpringBootPlugin
//        // Update the config of bootJar when SpringBootPlugin's BootJar is applied
//        // withId or withType
//        //   - withId is using the plugin id
//        //   - withType is using the plugin class (required the import of plugin package)
//        //
////        project.plugins.withId("org.springframework.boot") {
//        project.plugins.withType(SpringBootPlugin) {
//            project.tasks.named("bootJar", BootJar) {
//                doLast {
//                    exclude('application*.properties', 'application*.yml')
//                }
//            }
//        }
//
//        // Use withType to associate with OpenApiGeneratorPlugin
//        // Update the config of openApiGenerate when OpenApiGeneratorPlugin's GenerateTask is applied
//        // withId or withType
//        //   - withId is using the plugin id
//        //   - withType is using the plugin class (required the import of plugin package)
//        //
////        project.plugins.withId("org.openapi.generator") {
//        project.plugins.withType(OpenApiGeneratorPlugin) {
//            String generatedDir = "$buildDir/generated"
//
//            project.tasks.named("openApiGenerate", GenerateTask) {
//                // Resolve the task "clean" and set is as a dependency of this GenerateTask
//                Task cleanTask = project.tasks.findByName("clean")
//                dependsOn(cleanTask)
////                doFirst {
////                    ReactBackendExtension reactBackendConfig = project.extensions.findByName(ReactBackendExtension.EXTENSION_NAME)
//                //
//                //
////                generatorName.set("kotlin")
////                generatorName.set("java")
//                //
//                // Reference:
//                // https://openapi-generator.tech/docs/generators/spring/#metadata
//                generatorName.set("spring")
//                library.set("spring-boot")
//                //
//////                inputSpec.set("$rootDir/specs/petstore-v3.0.yaml")
////                inputSpec.set("$rootDir/specs/pocreact-v0.0.1.yaml")
////                inputSpec.set("/Users/siuka/dev-workspace/openapi-spec/openapi-generator-example/petstore-v3.0.yaml")
//                if (ReactBackendUtil.isLocalSpecUri(reactBackendConfig.specUri)) {
//                    inputSpec.set(reactBackendConfig.specUri)
//                } else {
////                    remoteInputSpec.set("https://raw.githubusercontent.com/siukatech/openapi-spec/main/openapi-generator-example/pocreact-v0.0.1.yaml")
//                    remoteInputSpec.set(reactBackendConfig.specUri)
//                }
//                //
//                outputDir.set("${generatedDir}")
//                //
////                apiPackage.set("org.openapi.example.api")
////                invokerPackage.set("org.openapi.example.invoker")
////                modelPackage.set("org.openapi.example.model")
//                apiPackage.set(reactBackendConfig.packageRoot + ".api")
//                invokerPackage.set(reactBackendConfig.packageRoot + ".invoker")
//                modelPackage.set(reactBackendConfig.packageRoot + ".model")
//                // Reference:
//                // https://github.com/gradle/develocity-api-samples/blob/main/build.gradle.kts
//                openapiNormalizer.set(["REF_AS_PARENT_IN_ALLOF": "true"])
//                // Reference:
//                // https://stackoverflow.com/q/73384038
//                configOptions.set([
//                    dateLibrary          : "java8"
//
//                    // interfaceOnly=true will generate the interface class only, no implementation
//                    , interfaceOnly      : "true"
//
//                    // useTags=true will generate the method operationId to specified tag(s), not by uri
//                    , useTags            : "true"
//
////                    , delegatePattern: "true"
//                    // Reference:
//                    // https://openapi-generator.tech/docs/generators/spring/#metadata
//                    , serializableModel  : "true"
//                    , useBeanValidation  : "true"
//                    , useResponseEntity  : "true"
//                    , useSpringBoot3     : "true"
//                    , useSpringController: "true"
//                    , useSwaggerUI       : "true"
//                ])
//
//                // Append custom schemaMappings from ReactBackendPlugin
//                Map<String, String> schemaMappingsMap = [
//                    Pageable: "org.springframework.data.domain.Pageable"
//                    ,
//                    Page: "org.springframework.data.domain.Page"
//                ]
//                schemaMappingsMap.putAll(reactBackendConfig.schemaMappingsMap)
//                schemaMappings.set(schemaMappingsMap)
////                schemaMappings.set([
////                        Pageable: "org.springframework.data.domain.Pageable"
////                        ,
////                        Page: "org.springframework.data.domain.Page"
////                ])
//
//                typeMappings.set([
//                    // Reference:
//                    // https://stackoverflow.com/a/77016049
////                    'string+date-time': 'LocalDateTime'
//                    "string+date-time": "LocalDateTime"
////                    , "string+date": "LocalDate"
//                    //
//                    // Reference:
//                    // https://stackoverflow.com/a/69464205
//                    // https://stackoverflow.com/a/76366031
////                    , "string+time": "LocalTime"
//                    , "time"          : "LocalTime"
//                    , "pageable"      : "Pageable"
//                    , "page"          : "Page"
//                ])
//
//                importMappings.set([
//                    LocalDateTime: "java.time.LocalDateTime"
////                    , LocalDate: "java.time.LocalDate"
//                    , LocalTime  : "java.time.LocalTime"
//                    , Pageable   : "org.springframework.data.domain.Pageable"
//                    , Page       : "org.springframework.data.domain.Page"
//                ])
//
//                // https://github.com/OpenAPITools/openapi-generator/pull/5166#issuecomment-588422333
//                additionalProperties.set([
//                    "removeEnumValuePrefix": "false"
////                    "removeEnumValuePrefix": "true"
//                ])
//
////                }
//            }
//
//            // Update the sourceSets with generated path when OpenApiGeneratorPlugin is applied
//            project.plugins.withType(JavaPlugin) {
//                JavaPlugin jp = project.plugins.findPlugin(JavaPlugin)
//                JavaPluginExtension jpe = project.extensions.getByType(JavaPluginExtension)
//                jpe.sourceSets {
//                    main {
//                        java {
//                            srcDir("${generatedDir}/src/main/java")
//                        }
//                    }
//                }
//            }
//        }
//
//        // Register a custom task with group to print out debug info
//        project.tasks.register("printDebugInfo", Task) {
//            group = PLUGIN_GROUP
//            doLast {
////                ReactBackendExtension reactBackendConfig = project.extensions.findByName(ReactBackendExtension.EXTENSION_NAME)
//                println "reactBackendConfig.specUri: [${reactBackendConfig.specUri}]"
//                println "reactBackendConfig.packageRoot: [${reactBackendConfig.packageRoot}]"
//                println "reactBackendConfig.schemaMappingsMap: [${reactBackendConfig.schemaMappingsMap}]"
//            }
//        }

    }

    void print_project_ext(Project project) {
        Logger log = project.getLogger();
        log.debug("print_project_ext - project.name: [${project.name}]")
        ExtraPropertiesExtension extraPropertiesExtension1 = project.extensions.findByType(ExtraPropertiesExtension.class)
//        ExtraPropertiesExtension extraPropertiesExtension1 = project.extensions.getExtraProperties()
////        ExtraPropertiesExtension extraPropertiesExtension1 = project.extensions.findByName("ext")
        log.debug("print_project_ext - extraPropertiesExtension1.springBootVersion: [${extraPropertiesExtension1.has("springBootVersion")}]")
        log.debug("print_project_ext - extraPropertiesExtension1.springSecurityVersion: [${extraPropertiesExtension1.has("springSecurityVersion")}]")
        log.debug("print_project_ext - extraPropertiesExtension1.platformSnapshotUri: [${extraPropertiesExtension1.has("platformSnapshotUri")}]")

        log.debug("print_project_ext - project.properties.springBootVersion: [${project.properties.hasProperty("springBootVersion")}]")
        log.debug("print_project_ext - project.properties.springSecurityVersion: [${project.properties.hasProperty("springSecurityVersion")}]")

        log.debug("print_project_ext - project.properties[springBootVersion]: [${project.properties["springBootVersion"]}]")

        log.debug("print_project_ext - project.rootProject.properties.springBootVersion: [${project.rootProject.properties.hasProperty("springBootVersion")}]")
        log.debug("print_project_ext - project.rootProject.properties.springSecurityVersion: [${project.rootProject.properties.hasProperty("springSecurityVersion")}]")

        log.debug("print_project_ext - project.rootProject.name: [${project.rootProject.name}]")
        ExtraPropertiesExtension extraPropertiesExtension2 = project.rootProject.extensions.findByType(ExtraPropertiesExtension.class)
//        ExtraPropertiesExtension extraPropertiesExtension2 = project.extensions.getExtraProperties()
////        ExtraPropertiesExtension extraPropertiesExtension2 = project.extensions.findByName("ext")
        log.debug("print_project_ext - extraPropertiesExtension2.springBootVersion: [${extraPropertiesExtension2.has("springBootVersion")}]")
        log.debug("print_project_ext - extraPropertiesExtension2.springSecurityVersion: [${extraPropertiesExtension2.has("springSecurityVersion")}]")
        log.debug("print_project_ext - extraPropertiesExtension2.platformSnapshotUri: [${extraPropertiesExtension2.has("platformSnapshotUri")}]")

        log.debug("print_project_ext - project.rootProject.properties.springBootVersion: [${project.rootProject.properties.hasProperty("springBootVersion")}]")
        log.debug("print_project_ext - project.rootProject.properties.springSecurityVersion: [${project.rootProject.properties.hasProperty("springSecurityVersion")}]")

        log.debug("print_project_ext - project.rootProject.properties[springBootVersion]: [${project.rootProject.properties["springBootVersion"]}]")

        log.debug("print_project_ext - project.rootProject.properties.springBootVersion: [${project.rootProject.properties.hasProperty("springBootVersion")}]")
        log.debug("print_project_ext - project.rootProject.properties.springSecurityVersion: [${project.rootProject.properties.hasProperty("springSecurityVersion")}]")

        Gradle gradle = project.rootProject.gradle
        if (gradle instanceof ExtensionAware) {
            ExtensionAware gradleExtensions = (ExtensionAware) gradle;
            log.debug("print_project_ext - gradleExtensions.extensions.extraProperties.springBootVersion: [${gradleExtensions.extensions.extraProperties.has("springBootVersion")}]")
            log.debug("print_project_ext - gradleExtensions.properties.springBootVersion: [${gradleExtensions.properties.hasProperty("springBootVersion")}]")
        }

        Gradle gradle1 = project.gradle
        if (gradle1 instanceof ExtensionAware) {
            ExtensionAware gradleExtensions = (ExtensionAware) gradle1;
            log.debug("print_project_ext - gradleExtensions.extensions.extraProperties.springBootVersion: [${gradleExtensions.extensions.extraProperties.has("springBootVersion")}]")
            log.debug("print_project_ext - gradleExtensions.properties.springBootVersion: [${gradleExtensions.properties.hasProperty("springBootVersion")}]")
        }

    }

    void print_project_version(Project project) {
        Logger log = project.getLogger();
        //DependencyManagementPluginAction
//        project.plugins.withType(SpringBootPlugin) {
            String implVersion = DependencyManagementPluginAction.class.package.implementationVersion
            log.debug("print_project_version - implVersion: [${implVersion}]")
//        }
    }

}
