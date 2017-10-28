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
package com.scorpion.huakerongtong.coyote.http11.upgrade;

import java.io.IOException;
import java.net.Socket;

import com.scorpion.huakerongtong.coyote.http11.upgrade.servlet31.HttpUpgradeHandler;
import com.scorpion.huakerongtong.juli.logging.Log;
import com.scorpion.huakerongtong.juli.logging.LogFactory;
import com.scorpion.huakerongtong.rabbit.util.net.SocketWrapper;

public class BioProcessor extends AbstractProcessor<Socket> {

    private static final Log log = LogFactory.getLog(BioProcessor.class);
    @Override
    protected Log getLog() {return log;}

    private static final int INFINITE_TIMEOUT = 0;

    public BioProcessor(SocketWrapper<Socket> wrapper,
            HttpUpgradeHandler httpUpgradeProcessor,
            int asyncWriteBufferSize) throws IOException {
        super(httpUpgradeProcessor, new BioServletInputStream(wrapper),
                new BioServletOutputStream(wrapper, asyncWriteBufferSize));

        wrapper.getSocket().setSoTimeout(INFINITE_TIMEOUT);
    }
}
