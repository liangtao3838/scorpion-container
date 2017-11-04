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
 */

package com.scorpion.huakerongtong.coyote;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.scorpion.huakerongtong.coyote.http11.upgrade.servlet31.HttpUpgradeHandler;
import com.scorpion.huakerongtong.rabbit.util.net.AbstractEndpoint.Handler.SocketState;
import com.scorpion.huakerongtong.rabbit.util.net.SSLSupport;
import com.scorpion.huakerongtong.rabbit.util.net.SocketStatus;
import com.scorpion.huakerongtong.rabbit.util.net.SocketWrapper;


/**
 * Common interface for processors of all protocols.
 */
public interface Processor<S> {
    Executor getExecutor();

    SocketState process(SocketWrapper<S> socketWrapper) throws IOException;

    SocketState event(SocketStatus status) throws IOException;

    SocketState asyncDispatch(SocketStatus status);
    SocketState asyncPostProcess();

    /**
     * @deprecated  Will be removed in Tomcat 8.0.x.
     */
    @Deprecated
    com.scorpion.huakerongtong.coyote.http11.upgrade.UpgradeInbound getUpgradeInbound();
    /**
     * @deprecated  Will be removed in Tomcat 8.0.x.
     */
    @Deprecated
    SocketState upgradeDispatch() throws IOException;

    HttpUpgradeHandler getHttpUpgradeHandler();
    SocketState upgradeDispatch(SocketStatus status) throws IOException;
    
    void errorDispatch();

    boolean isComet();
    boolean isAsync();
    boolean isUpgrade();

    Request getRequest();

    void recycle(boolean socketClosing);

    void setSslSupport(SSLSupport sslSupport);
}
