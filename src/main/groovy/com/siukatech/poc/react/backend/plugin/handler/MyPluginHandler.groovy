package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
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

class MyPluginHandler extends AbstractPluginHandler {

    public static String PLUGIN_GROUP = "react-backend-plugin"

    @Override
    void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig) {
        println("handle - MyPluginHandler")

        project.tasks.register("printDebugInfo", Task) {
            group = PLUGIN_GROUP
            doLast {
//                ReactBackendExtension reactBackendConfig = project.extensions.findByName(ReactBackendExtension.EXTENSION_NAME)
                println "reactBackendConfig.specUri: [${reactBackendConfig.specUri}]"
                println "reactBackendConfig.packageRoot: [${reactBackendConfig.packageRoot}]"
                println "reactBackendConfig.schemaMappingsMap: [${reactBackendConfig.schemaMappingsMap}]"
            }
        }

//        if (project.hasProperty("platformSnapshotUri")) {
//            String platformSnapshotUri = project.property("platformSnapshotUri") as String
//            println("handle - platformSnapshotUri: [${platformSnapshotUri}]")
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
