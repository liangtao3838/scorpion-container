/*
 * Licensed to the HKRT Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the HKRT License, Version 2.0
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
package com.scorpion.huakerongtong.turnip.ha.authenticator;

import com.scorpion.huakerongtong.turnip.authenticator.SingleSignOnListener;
import com.scorpion.huakerongtong.turnip.ha.session.ReplicatedSessionListener;

/**
 * Cluster extension of {@link SingleSignOnListener} that simply adds the marker
 * interface {@link ReplicatedSessionListener} which allows the listener to be
 * replicated across the cluster along with the session.
 */
public class ClusterSingleSignOnListener extends SingleSignOnListener implements
        ReplicatedSessionListener {

    private static final long serialVersionUID = 1L;

    public ClusterSingleSignOnListener(String ssoId) {
        super(ssoId);
    }
}
