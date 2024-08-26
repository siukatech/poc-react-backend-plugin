package com.siukatech.poc.react.backend.plugin

import org.gradle.api.Project
import org.openapitools.generator.gradle.plugin.OpenApiGeneratorPlugin
import org.springframework.boot.gradle.plugin.DependencyManagementPluginAction
import org.springframework.security.core.SpringSecurityCoreVersion

class ReactBackendUtil {
    static String resolveBuildDirPath(Project project) {
        String buildDir = project.layout.buildDirectory.get().asFile.path
        return buildDir
    }

    static boolean isLocalSpecUri(String specUri) {
        String specUri_lowerCase = specUri.toLowerCase();
        boolean isRemoteSpecUri = (specUri_lowerCase.startsWith("http://")
                || specUri_lowerCase.startsWith("https://"))
        return !isRemoteSpecUri
    }

    static String extractSpringBootVersion(String springBootDependencyStr) {
        println("extractSpringBootVersion - string - springBootDependencyStr: [${springBootDependencyStr}]")
        return springBootDependencyStr.replaceAll("org.springframework.boot:spring-boot-dependencies:", "")
    }

//    static String extractSpringBootVersion(Project project) {
//        String springBootVersion = "";
//        SpringBootPlugin springBootPlugin = project.plugins.findPlugin(SpringBootPlugin)
////        project.plugins.withType(SpringBootPlugin) {
//        println("extractSpringBootVersion - project - springBootPlugin: [${springBootPlugin}]")
//        if (springBootPlugin != null) {
//            springBootVersion = extractSpringBootVersion(springBootPlugin.BOM_COORDINATES)
//        }
//        return springBootVersion
//    }

    static String extractClazzImplVersion(Class clazz) {
        String implVersion = clazz.package.implementationVersion
        implVersion = implVersion == null ? "" : implVersion
        println("extractClazzImplVersion - clazz.package: [${clazz.package.name}], implVersion: [${implVersion}]")
        return implVersion
    }
    static String extractSpringBootVersion() {
//        String implVersion = DependencyManagementPluginAction.class.package.implementationVersion
        String implVersion = extractClazzImplVersion(DependencyManagementPluginAction.class)
//        println("extractSpringBootVersion - implVersion: [${implVersion}]")
        return implVersion
    }
    static String extractSpringSecurityVersion() {
        String implVersion = extractClazzImplVersion(SpringSecurityCoreVersion.class)
//        println("extractSpringSecurityVersion - implVersion: [${implVersion}]")
        return implVersion
    }
    static String extractOpenapiGeneratorVersion() {
        String implVersion = extractClazzImplVersion(OpenApiGeneratorPlugin.class)
        String packageName = OpenApiGeneratorPlugin.class.package.name
        String specVersion = OpenApiGeneratorPlugin.class.package.specificationVersion
//        println("extractOpenapiGeneratorVersion - packageName: [${packageName}], specVersion: [${specVersion}]")
//        println("extractOpenapiGeneratorVersion - properties: [${OpenApiGeneratorPlugin.properties}]")
//        OpenApiGeneratorPlugin.metaPropertyValues.forEach {
//            println("extractOpenapiGeneratorVersion - metaPropertyValues - it.name: [${it.name}], it.value: [${it.value}]")
//        }
        String protectionDomain = OpenApiGeneratorPlugin.properties["protectionDomain"]
//        println("extractOpenapiGeneratorVersion - properties[protectionDomain]: [${protectionDomain}]")
        // openapi-generator-gradle-plugin-7.2.0.jar
        String pluginNamePrefix = "openapi-generator-gradle-plugin-"
        String pluginNameSuffix = ".jar"
        if (protectionDomain.indexOf(pluginNamePrefix) >= 0) {
            protectionDomain = protectionDomain.substring(protectionDomain.indexOf(pluginNamePrefix) + pluginNamePrefix.length())
//            println("extractOpenapiGeneratorVersion - protectionDomain 1: [${protectionDomain}]")
            if (protectionDomain.indexOf(pluginNameSuffix) >= 0) {
                protectionDomain = protectionDomain.substring(0, protectionDomain.indexOf(pluginNameSuffix))
//                println("extractOpenapiGeneratorVersion - protectionDomain 2: [${protectionDomain}]")
                implVersion = protectionDomain
            }
        }
        println("extractOpenapiGeneratorVersion - packageName: [${packageName}], specVersion: [${specVersion}]")
        println("extractOpenapiGeneratorVersion - implVersion: [${implVersion}]")
        return implVersion
    }

}
