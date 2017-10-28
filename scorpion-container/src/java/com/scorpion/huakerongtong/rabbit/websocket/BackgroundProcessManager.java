/*
 *  Licensed to the HKRT Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the HKRT License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.scorpion.huakerongtong.rabbit.websocket;

import java.util.HashSet;
import java.util.Set;

import com.scorpion.huakerongtong.juli.logging.Log;
import com.scorpion.huakerongtong.juli.logging.LogFactory;
import com.scorpion.huakerongtong.rabbit.util.ExceptionUtils;
import com.scorpion.huakerongtong.rabbit.util.res.StringManager;

/**
 * Provides a background processing mechanism that triggers roughly once a
 * second. The class maintains a thread that only runs when there is at least
 * one instance of {@link BackgroundProcess} registered.
 */
public class BackgroundProcessManager {

    private static final Log log =
            LogFactory.getLog(BackgroundProcessManager.class);
    private static final StringManager sm =
            StringManager.getManager(Constants.PACKAGE_NAME);
    private static final BackgroundProcessManager instance;


    static {
        instance = new BackgroundProcessManager();
    }


    public static BackgroundProcessManager getInstance() {
        return instance;
    }

    private final Set<BackgroundProcess> processes = new HashSet<BackgroundProcess>();
    private final Object processesLock = new Object();
    private WsBackgroundThread wsBackgroundThread = null;

    private BackgroundProcessManager() {
        // Hide default constructor
    }


    public void register(BackgroundProcess process) {
        synchronized (processesLock) {
            if (processes.size() == 0) {
                wsBackgroundThread = new WsBackgroundThread(this);
                wsBackgroundThread.setContextClassLoader(
                        this.getClass().getClassLoader());
                wsBackgroundThread.setDaemon(true);
                wsBackgroundThread.start();
            }
            processes.add(process);
        }
    }


    public void unregister(BackgroundProcess process) {
        synchronized (processesLock) {
            processes.remove(process);
            if (wsBackgroundThread != null && processes.size() == 0) {
                wsBackgroundThread.halt();
                wsBackgroundThread = null;
            }
        }
    }


    private void process() {
        Set<BackgroundProcess> currentProcesses = new HashSet<BackgroundProcess>();
        synchronized (processesLock) {
            currentProcesses.addAll(processes);
        }
        for (BackgroundProcess process : currentProcesses) {
            try {
                process.backgroundProcess();
            } catch (Throwable t) {
                ExceptionUtils.handleThrowable(t);
                log.error(sm.getString(
                        "backgroundProcessManager.processFailed"), t);
            }
        }
    }


    /*
     * For unit testing.
     */
    int getProcessCount() {
        synchronized (processesLock) {
            return processes.size();
        }
    }


    void shutdown() {
        synchronized (processesLock) {
            processes.clear();
            if (wsBackgroundThread != null) {
                wsBackgroundThread.halt();
                wsBackgroundThread = null;
            }
        }
    }


    private static class WsBackgroundThread extends Thread {

        private final BackgroundProcessManager manager;
        private volatile boolean running = true;

        public WsBackgroundThread(BackgroundProcessManager manager) {
            setName("WebSocket background processing");
            this.manager = manager;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Ignore
                }
                manager.process();
            }
        }

        public void halt() {
            setName("WebSocket background processing - stopping");
            running = false;
        }
    }
}
