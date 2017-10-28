/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
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

package com.scorpion.huakerongtong.coyote;

import java.io.IOException;

import com.scorpion.huakerongtong.rabbit.util.buf.ByteChunk;


/**
 * Output buffer.
 *
 * This class is used internally by the protocol implementation. All writes from
 * higher level code should happen via Resonse.doWrite().
 * 
 * @author Remy Maucherat
 */
public interface OutputBuffer {


    /**
     * Write the response. The caller ( tomcat ) owns the chunks.
     *
     * @param chunk data to write
     * @param response used to allow buffers that can be shared by multiple
     *          responses.
     * @throws IOException
     */
    public int doWrite(ByteChunk chunk, Response response)
        throws IOException;

    /**
     * Bytes written to the underlying socket. This includes the effects of
     * chunking, compression, etc.
     * 
     * @return  Bytes written for the current request
     */
    public long getBytesWritten();
}