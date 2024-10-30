package com.siukatech.poc.react.backend.plugin.handler

import com.siukatech.poc.react.backend.plugin.ReactBackendExtension
import org.gradle.api.Project
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

class SpringBootPluginHandler extends AbstractPluginHandler {
    @Override
    void handle(Project project, String buildDir, ReactBackendExtension reactBackendConfig) {
//        println("handle - SpringBootPluginHandler")

        // Use withType to associate with SpringBootPlugin
        // Update the config of bootJar when SpringBootPlugin's BootJar is applied
        // withId or withType
        //   - withId is using the plugin id
        //   - withType is using the plugin class (required the import of plugin package)
        //
//        project.plugins.withId("org.springframework.boot") {
        project.plugins.withType(SpringBootPlugin) {
            project.tasks.named("bootJar", BootJar) {
                doLast {
                    exclude('application*.properties', 'application*.yml')
                }
            }

            project.tasks.named("bootRun", BootRun) {
//                println("handle - bootRun - System.getenv: [" + System.getenv("jvmArgs") + "]")
//                println("handle - bootRun - System.getpropertu: [" + System.getProperty("jvmArgs") + "]")
//                def jvmArgsStr = System.getenv("jvmArgs")
                def jvmArgsStr = System.getProperty("jvmArgs")
                def jvmArgsList = jvmArgsStr == null ? [] : jvmArgsStr.split('\\s+') as List
//                println("handle - bootRun - jvmArgsStr: [" + jvmArgsStr + "], jvmArgsList: [" + jvmArgsList + "]")
                jvmArgs(
//                    ["-Duser.timezone=UTC"]
                        jvmArgsList
                )
            }
        }

    }
}
