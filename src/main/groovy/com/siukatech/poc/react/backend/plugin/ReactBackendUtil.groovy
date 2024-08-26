package com.siukatech.poc.react.backend.plugin

import org.gradle.api.Project
import org.openapitools.generator.gradle.plugin.OpenApiGeneratorPlugin
import org.springframework.boot.gradle.plugin.DependencyManagementPluginAction
import org.springframework.boot.gradle.util.VersionExtractor
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

//    static String extractSpringBootVersion(String springBootDependencyStr) {
//        println("extractSpringBootVersion - string - springBootDependencyStr: [${springBootDependencyStr}]")
//        return springBootDependencyStr.replaceAll("org.springframework.boot:spring-boot-dependencies:", "")
//    }

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
        String implVersion = null;
//        implVersion = clazz.package.implementationVersion
        implVersion = VersionExtractor.forClass(clazz)
        if (implVersion == null) {
            try {
                URL codeSourceLocation = OpenApiGeneratorPlugin.class.getProtectionDomain().getCodeSource().getLocation()
                implVersion = extractFromCodeSourceLocation(codeSourceLocation)
            }
            catch (Exception ex) {
                println(ex.fillInStackTrace())
            }
        }
        implVersion = implVersion == null ? "" : implVersion
        println("extractClazzImplVersion - clazz.package: [${clazz.package.name}], implVersion: [${implVersion}]")
        return implVersion
    }

    static String extractFromCodeSourceLocation(URL codeSourceLocation) {
//        URLConnection connection = codeSourceLocation.openConnection();
//        println("extractClazzImplVersion - 1 - codeSourceLocation.toURI: [${codeSourceLocation.toURI()}]")
//        if (connection instanceof JarURLConnection) {
//            JarURLConnection jarURLConnection = (JarURLConnection) connection
////                    return getImplementationVersion(jarURLConnection.getJarFile());
//            JarFile jarFile = jarURLConnection.getJarFile()
//            println("extractClazzImplVersion - 1 - a - jarURLConnection.jarFileURL: [${jarURLConnection.jarFileURL}]")
//            println("extractClazzImplVersion - 1 - a - jarFile.getVersion: [${jarFile.getVersion()}]")
//        }
//        else {
//            try (JarFile jarFile = new JarFile(new File(codeSourceLocation.toURI()))) {
//                println("extractClazzImplVersion - 1 - b - codeSourceLocation.toURI: [${codeSourceLocation.toURI()}]")
//                println("extractClazzImplVersion - 1 - b - jarFile.getVersion: [${jarFile.getVersion()}]")
//            }
//        }
        String implVersion = null
        String fullPath = codeSourceLocation.toURI().path
        println("extractFromCodeSourceLocation - fullPath: [${fullPath}]")
        if (fullPath.lastIndexOf("/") >= 0) {
            String jarFileName = fullPath.substring(fullPath.lastIndexOf("/") + 1)
            String pluginNameWithVersion = jarFileName.substring(0, jarFileName.lastIndexOf("."))
            if (pluginNameWithVersion.lastIndexOf("-") >= 0) {
                implVersion = pluginNameWithVersion.substring(pluginNameWithVersion.lastIndexOf("-") + 1)
            }
            println("extractFromCodeSourceLocation - jarFileName: [${jarFileName}], pluginNameWithVersion: [${pluginNameWithVersion}], implVersion: [${implVersion}]")
        }
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
        String implVersion = null;
        implVersion = extractClazzImplVersion(OpenApiGeneratorPlugin.class)
//        String packageName = OpenApiGeneratorPlugin.class.package.name
//        String specVersion = OpenApiGeneratorPlugin.class.package.specificationVersion
//        println("extractOpenapiGeneratorVersion - packageName: [${packageName}], specVersion: [${specVersion}]")
//        println("extractOpenapiGeneratorVersion - properties: [${OpenApiGeneratorPlugin.properties}]")
//        OpenApiGeneratorPlugin.metaPropertyValues.forEach {
//            println("extractOpenapiGeneratorVersion - metaPropertyValues - it.name: [${it.name}], it.value: [${it.value}]")
//        }
//        println("extractOpenapiGeneratorVersion - 1 - implVersion: [${implVersion}]")
//        if ("" == implVersion) {
//            URL codeSourceLocation = OpenApiGeneratorPlugin.class.getProtectionDomain().getCodeSource().getLocation();
//            println("extractOpenapiGeneratorVersion - 1 - codeSourceLocation: [${codeSourceLocation}]")
//            try {
//                URLConnection connection = codeSourceLocation.openConnection();
//                println("extractOpenapiGeneratorVersion - 1 - connection: [${connection}]")
//                if (connection instanceof JarURLConnection) {
//                    JarURLConnection jarURLConnection = (JarURLConnection)connection
////                    return getImplementationVersion(jarURLConnection.getJarFile());
//                    JarFile jarFile = jarURLConnection.getJarFile()
//                    Manifest manifest = jarFile.getManifest()
//                    Attributes attributes = manifest.getMainAttributes()
//                    implVersion = jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
//                    println("extractOpenapiGeneratorVersion - 1 - a - jarURLConnection: [${jarURLConnection}]")
//                    println("extractOpenapiGeneratorVersion - 1 - a - jarFile: [${jarFile}]")
//                    println("extractOpenapiGeneratorVersion - 1 - a - manifest: [${manifest}]")
//                    println("extractOpenapiGeneratorVersion - 1 - a - attributes: [${attributes}]")
//                    println("extractOpenapiGeneratorVersion - 1 - a - implVersion: [${implVersion}]")
//                }
//                try (JarFile jarFile = new JarFile(new File(codeSourceLocation.toURI()))) {
////                    return getImplementationVersion(jarFile);
//                    Manifest manifest = jarFile.getManifest()
//                    Attributes attributes = manifest.getMainAttributes()
//                    implVersion = jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
//                    println("extractOpenapiGeneratorVersion - 1 - b - jarFile: [${jarFile}]")
//                    println("extractOpenapiGeneratorVersion - 1 - b - manifest: [${manifest}]")
//                    println("extractOpenapiGeneratorVersion - 1 - b - attributes: [${attributes}]")
//                    println("extractOpenapiGeneratorVersion - 1 - b - implVersion: [${implVersion}]")
//                }
//            }
//            catch (Exception ex) {
//                return null;
//            }
//            //
//            //
//            String protectionDomain = OpenApiGeneratorPlugin.properties["protectionDomain"]
////            println("extractOpenapiGeneratorVersion - properties[protectionDomain]: [${protectionDomain}]")
//            // openapi-generator-gradle-plugin-7.2.0.jar
//            String pluginNamePrefix = "openapi-generator-gradle-plugin-"
//            String pluginNameSuffix = ".jar"
//            if (protectionDomain.indexOf(pluginNamePrefix) >= 0) {
//                protectionDomain = protectionDomain.substring(protectionDomain.indexOf(pluginNamePrefix) + pluginNamePrefix.length())
////                println("extractOpenapiGeneratorVersion - protectionDomain 1: [${protectionDomain}]")
//                if (protectionDomain.indexOf(pluginNameSuffix) >= 0) {
//                    protectionDomain = protectionDomain.substring(0, protectionDomain.indexOf(pluginNameSuffix))
////                    println("extractOpenapiGeneratorVersion - protectionDomain 2: [${protectionDomain}]")
//                    implVersion = protectionDomain
//                }
//            }
//        }
//        println("extractOpenapiGeneratorVersion - packageName: [${packageName}], specVersion: [${specVersion}]")
//        println("extractOpenapiGeneratorVersion - implVersion 2: [${implVersion}]")
        return implVersion
    }

}
