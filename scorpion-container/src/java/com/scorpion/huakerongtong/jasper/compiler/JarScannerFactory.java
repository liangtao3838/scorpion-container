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
package com.scorpion.huakerongtong.jasper.compiler;

import javax.servlet.ServletContext;

import com.scorpion.huakerongtong.rabbit.JarScanner;
import com.scorpion.huakerongtong.rabbit.util.scan.StandardJarScanner;

/**
 * Provide a mechanism for Jasper to obtain a reference to the JarScanner
 * implementation.
 */
public class JarScannerFactory {

    private JarScannerFactory() {
        // Don't want any instances so hide the default constructor.
    }

    /**
     * Obtain the {@link JarScanner} associated with the specified {@link
     * ServletContext}. It is obtained via a context parameter.
     */
    public static JarScanner getJarScanner(ServletContext ctxt) {
        JarScanner jarScanner = 
            (JarScanner) ctxt.getAttribute(JarScanner.class.getName());
        if (jarScanner == null) {
            ctxt.log(Localizer.getMessage("jsp.warning.noJarScanner"));
            jarScanner = new StandardJarScanner();
        }
        return jarScanner;
    }

}
