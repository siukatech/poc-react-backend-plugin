package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
import org.gradle.api.Project

abstract class AbstractPluginHandler implements PluginHandler {
    @Override
    String getName() {
        return this.class.name
    }

//    abstract void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig)
}
