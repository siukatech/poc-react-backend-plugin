package com.siukatech.poc.react.backend.plugin

class ReactBackendExtension {
    public static String EXTENSION_NAME = "reactBackendConfig"
    String specUri
    String packageRoot
    Map<String, String> schemaMappingsMap

    String springBootVersion
    String springSecurityVersion
}
