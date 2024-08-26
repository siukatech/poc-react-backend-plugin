package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
import org.gradle.api.Project

interface PluginHandler {

    String getName()

    void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig)

}