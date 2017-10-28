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

package com.scorpion.huakerongtong.turnip.tribes.transport;
import com.scorpion.huakerongtong.turnip.tribes.ChannelException;
import com.scorpion.huakerongtong.turnip.tribes.ChannelMessage;
import com.scorpion.huakerongtong.turnip.tribes.Member;

/**
 * @author Filip Hanik
 * @since 5.5.16
 */
public interface MultiPointSender extends DataSender
{
    public void sendMessage(Member[] destination, ChannelMessage data) throws ChannelException;
    public void setMaxRetryAttempts(int attempts);
    public void setDirectBuffer(boolean directBuf);
    public void add(Member member);
    public void remove(Member member);
}
