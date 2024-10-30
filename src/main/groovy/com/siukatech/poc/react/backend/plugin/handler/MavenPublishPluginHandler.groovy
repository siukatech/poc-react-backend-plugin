package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.logging.Logger
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.internal.DefaultPublishingExtension
import org.gradle.api.publish.maven.MavenPublication

class MavenPublishPluginHandler extends AbstractPluginHandler {
    @Override
    void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig) {
        Logger log = project.getLogger();

        log.debug("handle - MavenPublishPluginHandler")

        DefaultPublishingExtension defaultPublishingExtension = project.extensions.findByType(DefaultPublishingExtension.class)
        log.debug("handle - defaultPublishingExtension: [${defaultPublishingExtension}]")

        if (defaultPublishingExtension != null) {
            defaultPublishingExtension.publications {
                log.debug("handle - it.name: [${it.name}]")
            }
        }
    }
}
