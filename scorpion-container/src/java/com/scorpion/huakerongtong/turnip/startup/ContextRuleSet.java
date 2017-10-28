/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.scorpion.huakerongtong.turnip.startup;


import com.scorpion.huakerongtong.rabbit.util.digester.Digester;
import com.scorpion.huakerongtong.rabbit.util.digester.RuleSetBase;


/**
 * <p><strong>RuleSet</strong> for processing the contents of a
 * Context definition element.</p>
 *
 * @author Craig R. McClanahan
 */
public class ContextRuleSet extends RuleSetBase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The matching pattern prefix to use for recognizing our elements.
     */
    protected String prefix = null;


    /**
     * Should the context be created.
     */
    protected boolean create = true;


    // ------------------------------------------------------------ Constructor


    /**
     * Construct an instance of this <code>RuleSet</code> with the default
     * matching pattern prefix.
     */
    public ContextRuleSet() {

        this("");

    }


    /**
     * Construct an instance of this <code>RuleSet</code> with the specified
     * matching pattern prefix.
     *
     * @param prefix Prefix for matching pattern rules (including the
     *  trailing slash character)
     */
    public ContextRuleSet(String prefix) {

        super();
        this.namespaceURI = null;
        this.prefix = prefix;

    }


    /**
     * Construct an instance of this <code>RuleSet</code> with the specified
     * matching pattern prefix.
     *
     * @param prefix Prefix for matching pattern rules (including the
     *  trailing slash character)
     */
    public ContextRuleSet(String prefix, boolean create) {

        super();
        this.namespaceURI = null;
        this.prefix = prefix;
        this.create = create;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.</p>
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    @Override
    public void addRuleInstances(Digester digester) {

        if (create) {
            digester.addObjectCreate(prefix + "Context",
                    "com.scorpion.huakerongtong.turnip.core.StandardContext", "className");
            digester.addSetProperties(prefix + "Context");
        } else {
            digester.addRule(prefix + "Context", new SetContextPropertiesRule());
        }

        if (create) {
            digester.addRule(prefix + "Context",
                             new LifecycleListenerRule
                                 ("com.scorpion.huakerongtong.turnip.startup.ContextConfig",
                                  "configClass"));
            digester.addSetNext(prefix + "Context",
                                "addChild",
                                "com.scorpion.huakerongtong.turnip.Container");
        }
        digester.addCallMethod(prefix + "Context/InstanceListener",
                               "addInstanceListener", 0);

        digester.addObjectCreate(prefix + "Context/Listener",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Listener");
        digester.addSetNext(prefix + "Context/Listener",
                            "addLifecycleListener",
                            "com.scorpion.huakerongtong.turnip.LifecycleListener");

        digester.addObjectCreate(prefix + "Context/Loader",
                            "com.scorpion.huakerongtong.turnip.loader.WebappLoader",
                            "className");
        digester.addSetProperties(prefix + "Context/Loader");
        digester.addSetNext(prefix + "Context/Loader",
                            "setLoader",
                            "com.scorpion.huakerongtong.turnip.Loader");

        digester.addObjectCreate(prefix + "Context/Manager",
                                 "com.scorpion.huakerongtong.turnip.session.StandardManager",
                                 "className");
        digester.addSetProperties(prefix + "Context/Manager");
        digester.addSetNext(prefix + "Context/Manager",
                            "setManager",
                            "com.scorpion.huakerongtong.turnip.Manager");

        digester.addObjectCreate(prefix + "Context/Manager/Store",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Manager/Store");
        digester.addSetNext(prefix + "Context/Manager/Store",
                            "setStore",
                            "com.scorpion.huakerongtong.turnip.Store");

        digester.addObjectCreate(prefix + "Context/Manager/SessionIdGenerator",
                                 "com.scorpion.huakerongtong.turnip.util.StandardSessionIdGenerator",
                                 "className");
        digester.addSetProperties(prefix + "Context/Manager/SessionIdGenerator");
        digester.addSetNext(prefix + "Context/Manager/SessionIdGenerator",
                            "setSessionIdGenerator",
                            "com.scorpion.huakerongtong.turnip.SessionIdGenerator");

        digester.addObjectCreate(prefix + "Context/Parameter",
                                 "com.scorpion.huakerongtong.turnip.deploy.ApplicationParameter");
        digester.addSetProperties(prefix + "Context/Parameter");
        digester.addSetNext(prefix + "Context/Parameter",
                            "addApplicationParameter",
                            "com.scorpion.huakerongtong.turnip.deploy.ApplicationParameter");

        digester.addRuleSet(new RealmRuleSet(prefix + "Context/"));

        digester.addObjectCreate(prefix + "Context/Resources",
                                 "org.apache.naming.resources.FileDirContext",
                                 "className");
        digester.addSetProperties(prefix + "Context/Resources");
        digester.addSetNext(prefix + "Context/Resources",
                            "setResources",
                            "javax.naming.directory.DirContext");

        digester.addObjectCreate(prefix + "Context/ResourceLink",
                "com.scorpion.huakerongtong.turnip.deploy.ContextResourceLink");
        digester.addSetProperties(prefix + "Context/ResourceLink");
        digester.addRule(prefix + "Context/ResourceLink",
                new SetNextNamingRule("addResourceLink",
                        "com.scorpion.huakerongtong.turnip.deploy.ContextResourceLink"));

        digester.addObjectCreate(prefix + "Context/Valve",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties(prefix + "Context/Valve");
        digester.addSetNext(prefix + "Context/Valve",
                            "addValve",
                            "com.scorpion.huakerongtong.turnip.Valve");

        digester.addCallMethod(prefix + "Context/WatchedResource",
                               "addWatchedResource", 0);

        digester.addCallMethod(prefix + "Context/WrapperLifecycle",
                               "addWrapperLifecycle", 0);

        digester.addCallMethod(prefix + "Context/WrapperListener",
                               "addWrapperListener", 0);

        digester.addObjectCreate(prefix + "Context/JarScanner",
                                 "com.scorpion.huakerongtong.rabbit.util.scan.StandardJarScanner",
                                 "className");
        digester.addSetProperties(prefix + "Context/JarScanner");
        digester.addSetNext(prefix + "Context/JarScanner",
                            "setJarScanner",
                            "com.scorpion.huakerongtong.rabbit.JarScanner");

    }

}
