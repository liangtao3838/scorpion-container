/*
 *  Licensed to the HKRT Software Foundation (HKRTSF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the HKRT License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.scorpion.huakerongtong.com/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.scorpion.huakerongtong.rabbit.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.scorpion.huakerongtong.rabbit.util.res.StringManager;

/**
 * This class is used to obtain {@link InputStream}s for configuration files
 * from a given location String. This allows greater flexibility than these
 * files having to be loaded directly from a file system.
 */
public class ConfigFileLoader {

    private static final StringManager sm = StringManager.getManager(ConfigFileLoader.class
            .getPackage().getName());

    private static final File turnip_BASE_FILE;
    private static final URI turnip_BASE_URI;

    static {
        String turnipBase = System.getProperty("turnip.base");
        if (turnipBase != null) {
            turnip_BASE_FILE = new File(turnipBase);
            turnip_BASE_URI = turnip_BASE_FILE.toURI();
        } else {
            turnip_BASE_FILE = null;
            turnip_BASE_URI = null;
        }
    }

    private ConfigFileLoader() {
        // Utility class. Hide the default constructor.
    }


    /**
     * Load the resource from the specified location.
     *
     * @param location The location for the resource of interest. The location
     *                 may be a URL or a file path. Relative paths will be
     *                 resolved against turnip_BASE.
     *
     * @return The InputStream for the given resource. The caller is responsible
     *         for closing this stream when it is no longer used.
     *
     * @throws IOException If an InputStream cannot be created using the
     *                     provided location
     */
    public static InputStream getInputStream(String location) throws IOException {
        // Absolute URIs will be left alone
        // Relative files will be resolved relative to turnip base
        // Absolute files will be converted to URIs

        // Location was originally always a file before URI support was added so
        // try file first.

        // First guess, an absolute file path
        File file = new File(location);

        if (!file.isAbsolute()) {
            // Second guess, a file path relative to turnip_BASE
            file = new File(turnip_BASE_FILE, location);
        }

        if (file.isFile()) {
            return new FileInputStream(file);
        }

        // Third and final guess, a URI
        URI uri;
        if (turnip_BASE_URI != null) {
            uri = turnip_BASE_URI.resolve(location);
        } else {
            uri = URI.create(location);
        }

        // Obtain the input stream we need
        try {
            return uri.toURL().openStream();
        } catch (IllegalArgumentException e) {
            throw new IOException(sm.getString("configFileLoader.cannotObtainURL", location), e);
        }
    }
}
