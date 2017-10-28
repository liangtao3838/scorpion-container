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

package com.scorpion.huakerongtong.turnip.tribes.transport;

import com.scorpion.huakerongtong.turnip.tribes.Channel;
import com.scorpion.huakerongtong.turnip.tribes.ChannelException;
import com.scorpion.huakerongtong.turnip.tribes.ChannelMessage;
import com.scorpion.huakerongtong.turnip.tribes.ChannelSender;
import com.scorpion.huakerongtong.turnip.tribes.Member;
import com.scorpion.huakerongtong.turnip.tribes.transport.nio.PooledParallelSender;
import com.scorpion.huakerongtong.turnip.tribes.util.StringManager;

/**
 * Transmit message to other cluster members
 * Actual senders are created based on the replicationMode
 * type 
 * 
 * @author Filip Hanik
 */
public class ReplicationTransmitter implements ChannelSender {

    private Channel channel;

    /**
     * The descriptive information about this implementation.
     */
    private static final String info = "ReplicationTransmitter/3.0";

    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager.getManager(Constants.Package);


    public ReplicationTransmitter() {
    }

    private MultiPointSender transport = new PooledParallelSender();

    /**
     * Return descriptive information about this implementation and the
     * corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    public String getInfo() {
        return (info);
    }

    public MultiPointSender getTransport() {
        return transport;
    }

    public void setTransport(MultiPointSender transport) {
        this.transport = transport;
    }
    
    // ------------------------------------------------------------- public
    
    /**
     * Send data to one member
     * @see com.scorpion.huakerongtong.turnip.tribes.ChannelSender#sendMessage(com.scorpion.huakerongtong.turnip.tribes.ChannelMessage, com.scorpion.huakerongtong.turnip.tribes.Member[])
     */
    @Override
    public void sendMessage(ChannelMessage message, Member[] destination) throws ChannelException {
        MultiPointSender sender = getTransport();
        sender.sendMessage(destination,message);
    }
    
    
    /**
     * start the sender and register transmitter mbean
     * 
     * @see com.scorpion.huakerongtong.turnip.tribes.ChannelSender#start()
     */
    @Override
    public void start() throws java.io.IOException {
        getTransport().connect();
    }

    /**
     * stop the sender and deregister mbeans (transmitter, senders)
     * 
     * @see com.scorpion.huakerongtong.turnip.tribes.ChannelSender#stop()
     */
    @Override
    public synchronized void stop() {
        getTransport().disconnect();
        channel = null;
    }

    /**
     * Call transmitter to check for sender socket status
     * 
     * @see com.scorpion.huakerongtong.turnip.ha.tcp.SimpleTcpCluster#backgroundProcess()
     */
    @Override
    public void heartbeat() {
        if (getTransport()!=null) getTransport().keepalive();
    }

    /**
     * add new cluster member and create sender ( s. replicationMode) transfer
     * current properties to sender
     * 
     * @see com.scorpion.huakerongtong.turnip.tribes.ChannelSender#add(com.scorpion.huakerongtong.turnip.tribes.Member)
     */
    @Override
    public synchronized void add(Member member) {
        getTransport().add(member);
    }

    /**
     * remove sender from transmitter. ( deregister mbean and disconnect sender )
     * 
     * @see com.scorpion.huakerongtong.turnip.tribes.ChannelSender#remove(com.scorpion.huakerongtong.turnip.tribes.Member)
     */
    @Override
    public synchronized void remove(Member member) {
        getTransport().remove(member);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    // ------------------------------------------------------------- protected


}
