/*
 * Licensed to the HKRT Software Foundation (HKRTSF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the HKRT License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.scorpion.huakerongtong.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scorpion.huakerongtong.turnip.security;

import java.security.Security;

import com.scorpion.huakerongtong.turnip.startup.CatalinaProperties;

/**
 * Util class to protect Catalina against package access and insertion.
 * The code are been moved from Catalina.java
 * @author the Catalina.java authors
 * @author Jean-Francois Arcand
 */
public final class SecurityConfig{
    private static SecurityConfig singleton = null;

    private static final com.scorpion.huakerongtong.juli.logging.Log log=
        com.scorpion.huakerongtong.juli.logging.LogFactory.getLog( SecurityConfig.class );


    private static final String PACKAGE_ACCESS =  "sun.,"
                                                + "com.scorpion.huakerongtong.turnip."
                                                + ",com.scorpion.huakerongtong.jasper."
                                                + ",com.scorpion.huakerongtong.coyote."
                                                + ",com.scorpion.huakerongtong.rabbit.";

    // FIX ME package "javax." was removed to prevent HotSpot
    // fatal internal errors
    private static final String PACKAGE_DEFINITION= "java.,sun."
                                                + ",com.scorpion.huakerongtong.turnip."
                                                + ",com.scorpion.huakerongtong.coyote."
                                                + ",com.scorpion.huakerongtong.rabbit."
                                                + ",com.scorpion.huakerongtong.jasper.";
    /**
     * List of protected package from conf/catalina.properties
     */
    private String packageDefinition;


    /**
     * List of protected package from conf/catalina.properties
     */
    private String packageAccess;


    /**
     * Create a single instance of this class.
     */
    private SecurityConfig(){
        try{
            packageDefinition = CatalinaProperties.getProperty("package.definition");
            packageAccess = CatalinaProperties.getProperty("package.access");
        } catch (java.lang.Exception ex){
            if (log.isDebugEnabled()){
                log.debug("Unable to load properties using CatalinaProperties", ex);
            }
        }
    }


    /**
     * Returns the singleton instance of that class.
     * @return an instance of that class.
     */
    public static SecurityConfig newInstance(){
        if (singleton == null){
            singleton = new SecurityConfig();
        }
        return singleton;
    }


    /**
     * Set the security package.access value.
     */
    public void setPackageAccess(){
        // If catalina.properties is missing, protect all by default.
        if (packageAccess == null){
            setSecurityProperty("package.access", PACKAGE_ACCESS);
        } else {
            setSecurityProperty("package.access", packageAccess);
        }
    }


    /**
     * Set the security package.definition value.
     */
     public void setPackageDefinition(){
        // If catalina.properties is missing, protect all by default.
         if (packageDefinition == null){
            setSecurityProperty("package.definition", PACKAGE_DEFINITION);
         } else {
            setSecurityProperty("package.definition", packageDefinition);
         }
    }


    /**
     * Set the proper security property
     * @param properties the package.* property.
     */
    private final void setSecurityProperty(String properties, String packageList){
        if (System.getSecurityManager() != null){
            String definition = Security.getProperty(properties);
            if( definition != null && definition.length() > 0 ){
                if (packageList.length() > 0) {
                    definition = definition + ',' + packageList;
                }
            } else {
                definition = packageList;
            }

            Security.setProperty(properties, definition);
        }
    }


}


