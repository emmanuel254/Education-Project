/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.educationproject.config;

import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author litem
 */
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        register(JerseyObjectMapperProvider.class);
        register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);

        packages("com.test.educationproject.api", "com.fasterxml.jackson.jaxrs");

        property("jersey.config.jsonFeature", "JacksonFeature");
        property("jersey.config.disableMoxyJson.server", true);
    }
}
