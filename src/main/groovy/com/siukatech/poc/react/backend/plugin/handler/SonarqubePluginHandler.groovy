package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension

class SonarqubePluginHandler extends AbstractPluginHandler {
    @Override
    void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig) {
        Logger log = project.getLogger();
//        log.debug("handle - SonarqubePluginHandler")

        project.plugins.apply(JacocoPlugin)

//        JacocoPluginExtension jacocoPluginExtension = {
//
//        }

    }
}
