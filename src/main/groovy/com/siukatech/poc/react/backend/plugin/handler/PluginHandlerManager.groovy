package com.siukatech.poc.react.backend.plugin.handler

import org.gradle.api.Plugin

class PluginHandlerManager {

    private static final String PACKAGE_NAME_HANDLER = "com.siukatech.poc.react.backend.plugin.handler"
    private Map<String, PluginHandler> PLUGIN_HANDLER_MAP = new HashMap<>();

    int getPluginHandlerMapSize() {
        return PLUGIN_HANDLER_MAP.size()
    }

    PluginHandler resolvePluginHandler(String pluginSimpleName) {
        String pluginHandlerName = pluginSimpleName + "Handler"
        boolean isPluginHandlerExisted = PLUGIN_HANDLER_MAP.containsKey(pluginHandlerName);
        boolean isPluginHandlerCreated = false;
        PluginHandler pluginHandler = PLUGIN_HANDLER_MAP.get(pluginHandlerName);
        if (isPluginHandlerExisted) {
            pluginHandler = PLUGIN_HANDLER_MAP.get(pluginHandlerName)
        }
        else {
            try {
                pluginHandler = Class.forName(PACKAGE_NAME_HANDLER + "." + pluginHandlerName).newInstance(new Object[] {})
                PLUGIN_HANDLER_MAP.put(pluginHandlerName, pluginHandler)
//                println("resolvePluginHandler - 1 - pluginSimpleName: [${pluginSimpleName}"
//                        + "], pluginHandlerName: [${pluginHandlerName}"
//                        + "], isPluginHandlerExisted: [${isPluginHandlerExisted}"
//                        + "], isPluginHandlerCreated: [${isPluginHandlerCreated}"
//                        + "]")
                isPluginHandlerCreated = true
//                println("resolvePluginHandler - 2 - pluginSimpleName: [${pluginSimpleName}"
//                        + "], pluginHandlerName: [${pluginHandlerName}"
//                        + "], isPluginHandlerExisted: [${isPluginHandlerExisted}"
//                        + "], isPluginHandlerCreated: [${isPluginHandlerCreated}"
//                        + "]")
            }
            catch (Exception exception) {
                println(exception.fillInStackTrace())
            }
        }
//        println("resolvePluginHandler - pluginSimpleName: [${pluginSimpleName}"
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
