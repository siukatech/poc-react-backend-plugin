package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
import com.siukatech.poc.react.backend.plugin.ReactBackendPlugin
import com.siukatech.poc.react.backend.plugin.task.BuildInfoTask
import org.gradle.api.Action
import org.gradle.api.ActionConfiguration
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.ComponentMetadataSupplier
import org.gradle.api.artifacts.ComponentMetadataVersionLister
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.AuthenticationContainer
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.MavenRepositoryContentDescriptor
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.artifacts.repositories.RepositoryContentDescriptor
import org.gradle.api.credentials.Credentials
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.JavaPlugin
import org.gradle.language.jvm.tasks.ProcessResources

class MyPluginHandler extends AbstractPluginHandler {

    @Override
    void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig) {
        Logger log = project.getLogger()
//        println("handle - MyPluginHandler")

        project.tasks.register("printDebugInfo", Task) {
            group = ReactBackendPlugin.PLUGIN_GROUP
            doLast {
//                ReactBackendExtension reactBackendConfig = project.extensions.findByName(ReactBackendExtension.EXTENSION_NAME)
                log.debug "reactBackendConfig.specUri: [${reactBackendConfig.specUri}]"
                log.debug "reactBackendConfig.packageRoot: [${reactBackendConfig.packageRoot}]"
                log.debug "reactBackendConfig.schemaMappingsMap: [${reactBackendConfig.schemaMappingsMap}]"
            }
        }

        log.debug("handle - register - BuildInfoTask")
        project.tasks.register(BuildInfoTask.MY_TASK_NAME, BuildInfoTask) {
//            BuildInfoTask buildInfoTask = it
//            dependsOn(processResources)
//            Task classesTask = project.tasks.findByName(JavaPlugin.CLASSES_TASK_NAME)
//            dependsOn(classesTask)
            ProcessResources processResources = project.tasks.findByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
//            log.debug "register - buildInfoTask - processResources: [${processResources}]"
//            println "register - buildInfoTask - processResources: [${processResources}]"
            dependsOn(processResources)
//            BuildInfoTask buildInfoTask = project.tasks.findByName(MY_TASK_NAME)
//            project.tasks.named(JavaPlugin.CLASSES_TASK_NAME) {
//                dependsOn(buildInfoTask)
//            }
        }
        BuildInfoTask buildInfoTask = project.tasks.findByName(BuildInfoTask.MY_TASK_NAME)
        project.tasks.named(JavaPlugin.CLASSES_TASK_NAME) {
            dependsOn(buildInfoTask)
        }

//        if (project.hasProperty("platformSnapshotUri")) {
//            String platformSnapshotUri = project.property("platformSnapshotUri") as String
//            log.debug("handle - platformSnapshotUri: [${platformSnapshotUri}]")
////            project.repositories.add({
////                name = 'platformSnapshot'
////                allowInsecureProtocol = true
////                credentials(PasswordCredentials)
////                url = "${platformSnapshotUri}"
////            } as MavenArtifactRepository)
////            }) .maven {
//////                maven {
////                    name = 'platformSnapshot'
////                    allowInsecureProtocol = true
////                    credentials(PasswordCredentials)
////                    url = "${platformSnapshotUri}"
//////                }
////            }
//        }

    }
}
