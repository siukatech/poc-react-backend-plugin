package com.siukatech.poc.react.backend.plugin.task

import com.siukatech.poc.react.backend.plugin.ReactBackendPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources

/**
 * Reference:
 * https://github.com/spasam/spring-boot-build-info
 */
class BuildInfoTask extends DefaultTask {

    public static String MY_TASK_NAME = "genBuildInfo"
    private static String BUILD_INFO_PREFIX = "build."

    BuildInfoTask() {
        this.group = ReactBackendPlugin.PLUGIN_GROUP
//        ProcessResources processResources = project.tasks.findByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
//        this.dependsOn(processResources)
//        BuildInfoTask buildInfoTask = project.tasks.findByName(MY_TASK_NAME)
//        project.tasks.named(JavaPlugin.CLASSES_TASK_NAME) {
//            dependsOn(buildInfoTask)
//        }
    }

//    String getArtifactName() {
//        String artifactName = null
//        Jar jar = project.tasks.findByName("jar")
//        artifactName = jar == null ? null : jar.getArchiveBaseName()
//        return artifactName
//    }

    @TaskAction
    void exec() {
        File buildInfoPropFile = getBuildInfoPropFile()
//        String artifactName = getArtifactName()
        String buildInfo = "" +
                BUILD_INFO_PREFIX + "name=" + project.name + "\n" +
                BUILD_INFO_PREFIX + "group=" + project.group + "\n" +
                BUILD_INFO_PREFIX + "version=" + project.version + "\n"
//        if (artifactName != null) buildInfo += BUILD_INFO_PREFIX + "artifact=" + artifactName
        buildInfoPropFile.withWriter {
            it.writeLine(buildInfo)
        }
    }

    @OutputFile
    File getBuildInfoPropFile() {
        ProcessResources processResources = project.tasks.findByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
        return new File(processResources.destinationDir, "META-INF/build-info.properties")
    }

}
