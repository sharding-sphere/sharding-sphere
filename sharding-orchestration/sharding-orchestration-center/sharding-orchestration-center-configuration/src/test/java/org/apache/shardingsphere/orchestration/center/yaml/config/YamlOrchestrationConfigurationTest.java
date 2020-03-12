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

package org.apache.shardingsphere.orchestration.center.yaml.config;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class YamlOrchestrationConfigurationTest {
    
    @Test
    public void assertInstanceConfigurationMapWithConstructMethod() {
        Map<String, YamlCenterRepositoryConfiguration> instanceConfigurationMap = new HashMap<>();
        YamlOrchestrationConfiguration yamlOrchestrationConfiguration = new YamlOrchestrationConfiguration(instanceConfigurationMap);
        assertThat(yamlOrchestrationConfiguration.getCenterRepositoryConfigurationMap(), is(instanceConfigurationMap));
    }
    
    @Test
    public void assertInstanceConfigurationMapWithSetMethod() {
        Map<String, YamlCenterRepositoryConfiguration> instanceConfigurationMap = new HashMap<>();
        YamlOrchestrationConfiguration yamlOrchestrationConfiguration = new YamlOrchestrationConfiguration();
        yamlOrchestrationConfiguration.setCenterRepositoryConfigurationMap(instanceConfigurationMap);
        assertThat(yamlOrchestrationConfiguration.getCenterRepositoryConfigurationMap(), is(instanceConfigurationMap));
    }
}
