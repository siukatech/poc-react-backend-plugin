package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
import com.siukatech.poc.react.backend.plugin.ReactBackendUtil
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.openapitools.generator.gradle.plugin.OpenApiGeneratorPlugin
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

class OpenApiGeneratorPluginHandler extends AbstractPluginHandler {
    @Override
    void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig) {
//        println("handle - OpenApiGeneratorPluginHandler")

        // Use withType to associate with OpenApiGeneratorPlugin
        // Update the config of openApiGenerate when OpenApiGeneratorPlugin's GenerateTask is applied
        // withId or withType
        //   - withId is using the plugin id
        //   - withType is using the plugin class (required the import of plugin package)
        //
//        project.plugins.withId("org.openapi.generator") {
        project.plugins.withType(OpenApiGeneratorPlugin) {
            String generatedDir = "$buildDir/generated"

            project.tasks.named("openApiGenerate", GenerateTask) {
                // Resolve the task "clean" and set is as a dependency of this GenerateTask
                Task cleanTask = project.tasks.findByName("clean")
                dependsOn(cleanTask)
//                doFirst {
//                    ReactBackendExtension reactBackendConfig = project.extensions.findByName(ReactBackendExtension.EXTENSION_NAME)
                //
                //
//                generatorName.set("kotlin")
//                generatorName.set("java")
                //
                // Reference:
                // https://openapi-generator.tech/docs/generators/spring/#metadata
                generatorName.set("spring")
                library.set("spring-boot")
                //
////                inputSpec.set("$rootDir/specs/petstore-v3.0.yaml")
//                inputSpec.set("$rootDir/specs/pocreact-v0.0.1.yaml")
//                inputSpec.set("/Users/siuka/dev-workspace/openapi-spec/openapi-generator-example/petstore-v3.0.yaml")
                if (ReactBackendUtil.isLocalSpecUri(reactBackendConfig.specUri)) {
                    inputSpec.set(reactBackendConfig.specUri)
                } else {
//                    remoteInputSpec.set("https://raw.githubusercontent.com/siukatech/openapi-spec/main/openapi-generator-example/pocreact-v0.0.1.yaml")
                    remoteInputSpec.set(reactBackendConfig.specUri)
                }
                //
                outputDir.set("${generatedDir}")
                //
//                apiPackage.set("org.openapi.example.api")
//                invokerPackage.set("org.openapi.example.invoker")
//                modelPackage.set("org.openapi.example.model")
                apiPackage.set(reactBackendConfig.packageRoot + ".api")
                invokerPackage.set(reactBackendConfig.packageRoot + ".invoker")
                modelPackage.set(reactBackendConfig.packageRoot + ".model")
                // Reference:
                // https://github.com/gradle/develocity-api-samples/blob/main/build.gradle.kts
                openapiNormalizer.set(["REF_AS_PARENT_IN_ALLOF": "true"])
                // Reference:
                // https://stackoverflow.com/q/73384038
                configOptions.set([
                    dateLibrary          : "java8"

                    // interfaceOnly=true will generate the interface class only, no implementation
                    , interfaceOnly      : "true"

                    // useTags=true will generate the method operationId to specified tag(s), not by uri
                    , useTags            : "true"

//                    , delegatePattern: "true"
                    // Reference:
                    // https://openapi-generator.tech/docs/generators/spring/#metadata
                    , serializableModel  : "true"
                    , useBeanValidation  : "true"
                    , useResponseEntity  : "true"
                    , useSpringBoot3     : "true"
                    , useSpringController: "true"
                    , useSwaggerUI       : "true"
                ])

                // Append custom schemaMappings from ReactBackendPlugin
                Map<String, String> schemaMappingsMap = [
                    Pageable: "org.springframework.data.domain.Pageable"
                    ,
                    Page: "org.springframework.data.domain.Page"
                ]
                schemaMappingsMap.putAll(reactBackendConfig.schemaMappingsMap)
                schemaMappings.set(schemaMappingsMap)
//                schemaMappings.set([
//                        Pageable: "org.springframework.data.domain.Pageable"
//                        ,
//                        Page: "org.springframework.data.domain.Page"
//                ])

                typeMappings.set([
                    // Reference:
                    // https://stackoverflow.com/a/77016049
//                    'string+date-time': 'LocalDateTime'
                    "string+date-time": "LocalDateTime"
//                    , "string+date": "LocalDate"
                    //
                    // Reference:
                    // https://stackoverflow.com/a/69464205
                    // https://stackoverflow.com/a/76366031
//                    , "string+time": "LocalTime"
                    , "time"          : "LocalTime"
                    , "pageable"      : "Pageable"
                    , "page"          : "Page"
                ])

                importMappings.set([
                    LocalDateTime: "java.time.LocalDateTime"
//                    , LocalDate: "java.time.LocalDate"
                    , LocalTime  : "java.time.LocalTime"
                    , Pageable   : "org.springframework.data.domain.Pageable"
                    , Page       : "org.springframework.data.domain.Page"
                ])

                // https://github.com/OpenAPITools/openapi-generator/pull/5166#issuecomment-588422333
                additionalProperties.set([
                    "removeEnumValuePrefix": "false"
//                    "removeEnumValuePrefix": "true"
                ])

//                }
            }

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
        }


    }
}
