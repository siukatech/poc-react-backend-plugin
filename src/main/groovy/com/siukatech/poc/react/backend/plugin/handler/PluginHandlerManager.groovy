package com.siukatech.poc.react.backend.plugin.handler

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

class PluginHandlerManager {

    private static final String PACKAGE_NAME_HANDLER = "com.siukatech.poc.react.backend.plugin.handler"
    private Map<String, PluginHandler> PLUGIN_HANDLER_MAP = new HashMap<>();
    private Project project

    PluginHandlerManager(Project project) {
        this.project = project
    }

    int getPluginHandlerMapSize() {
        return PLUGIN_HANDLER_MAP.size()
    }

    PluginHandler resolvePluginHandler(String pluginSimpleName) {
        Logger log = project.logger
        String pluginHandlerName = pluginSimpleName + "Handler"
        boolean isPluginHandlerExistedBefore = PLUGIN_HANDLER_MAP.containsKey(pluginHandlerName);
        boolean isPluginHandlerCreated = false;
        PluginHandler pluginHandler = PLUGIN_HANDLER_MAP.get(pluginHandlerName);
        if (isPluginHandlerExistedBefore) {
            pluginHandler = PLUGIN_HANDLER_MAP.get(pluginHandlerName)
        }
        else {
            try {
                pluginHandler = Class.forName(PACKAGE_NAME_HANDLER + "." + pluginHandlerName).newInstance(new Object[] {})
                PLUGIN_HANDLER_MAP.put(pluginHandlerName, pluginHandler)
                boolean isPluginHandlerExistedAfter = PLUGIN_HANDLER_MAP.containsKey(pluginHandlerName);
                isPluginHandlerCreated = true
                log.debug("resolvePluginHandler - pluginSimpleName: [${pluginSimpleName}"
                        + "], pluginHandlerName: [${pluginHandlerName}"
                        + "], isPluginHandlerExistedBefore: [${isPluginHandlerExistedBefore}"
                        + "], isPluginHandlerExistedAfter: [${isPluginHandlerExistedAfter}"
                        + "], isPluginHandlerCreated: [${isPluginHandlerCreated}"
                        + "]")
            }
            catch (Exception exception) {
                println(exception.fillInStackTrace())
            }
        }
//        log.debug("resolvePluginHandler - pluginSimpleName: [${pluginSimpleName}"
//                + "], pluginHandlerName: [${pluginHandlerName}"
//                + "], isPluginHandlerExisted: [${isPluginHandlerExisted}"
//                + "], isPluginHandlerCreated: [${isPluginHandlerCreated}"
//                + "]")
        return pluginHandler

    }

    static PluginHandler resolvePluginHandler(Plugin plugin) {
        return resolvePluginHandler(plugin.class.simpleName)
    }
}
