/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.test.integration.junit.container.governance.impl;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.apache.shardingsphere.governance.core.yaml.config.YamlGovernanceCenterConfiguration;
import org.apache.shardingsphere.test.integration.junit.container.governance.ShardingSphereGovernanceContainer;
import org.apache.shardingsphere.test.integration.junit.param.model.ParameterizedArray;

import java.util.Properties;

public class EmbeddedZookeeperContainer extends ShardingSphereGovernanceContainer {
    
    private final TestingServer server;
    
    @SneakyThrows
    public EmbeddedZookeeperContainer(final ParameterizedArray parameterizedArray) {
        super("zooKeeperServer", "zooKeeperServer", true, parameterizedArray);
        this.server = new TestingServer(false);
    }
    
    @SneakyThrows
    @Override
    public void start() {
        super.start();
        server.restart();
    }
    
    @Override
    public boolean isHealthy() {
        return true;
    }
    
    @Override
    public String getServerLists() {
        return server.getConnectString();
    }
    
    @Override
    protected YamlGovernanceCenterConfiguration createGovernanceCenterConfiguration() {
        YamlGovernanceCenterConfiguration configuration = new YamlGovernanceCenterConfiguration();
        configuration.setServerLists(getServerLists());
        configuration.setType("zookeeper");
        Properties props = new Properties();
        props.setProperty("retryIntervalMilliseconds", "500");
        props.setProperty("timeToLiveSeconds", "60");
        props.setProperty("maxRetries", "3");
        props.setProperty("operationTimeoutMilliseconds", "500");
        configuration.setProps(props);
        return configuration;
    }
    
    @SneakyThrows
    @Override
    public void close() {
        server.stop();
    }
    
}
