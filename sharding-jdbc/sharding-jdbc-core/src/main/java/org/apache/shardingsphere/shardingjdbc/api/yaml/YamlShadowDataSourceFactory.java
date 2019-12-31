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

package org.apache.shardingsphere.shardingjdbc.api.yaml;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.shardingsphere.api.config.shadow.ShadowRuleConfiguration;
import org.apache.shardingsphere.core.yaml.config.shadow.YamlRootShadowConfiguration;
import org.apache.shardingsphere.core.yaml.engine.YamlEngine;
import org.apache.shardingsphere.core.yaml.swapper.impl.ShadowRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.shardingjdbc.api.ShadowDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;


/**
 * Shadow data source factory for YAML.
 *
 * @author xiayan
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlShadowDataSourceFactory {

    /**
     * Create shadow data source.
     *
     * @param yamlFile YAML file for encrypt rule configuration with data sources
     * @return shadow data source
     */
    @SneakyThrows
    public static DataSource createDataSource(final File yamlFile) {
        YamlRootShadowConfiguration config = YamlEngine.unmarshal(yamlFile, YamlRootShadowConfiguration.class);
        ShadowRuleConfiguration ruleConfig = new ShadowRuleConfigurationYamlSwapper().swap(config.getShadowRule());
        return ShadowDataSourceFactory.createDataSource(config.getDataSources(), ruleConfig, config.getProps());
    }

    /**
     * Create shadow data source.
     *
     * @param yamlBytes YAML bytes for encrypt rule configuration with data sources
     * @return shadow data source
     */
    @SneakyThrows
    public static DataSource createDataSource(final byte[] yamlBytes) {
        YamlRootShadowConfiguration config = YamlEngine.unmarshal(yamlBytes, YamlRootShadowConfiguration.class);
        ShadowRuleConfiguration ruleConfig = new ShadowRuleConfigurationYamlSwapper().swap(config.getShadowRule());
        return ShadowDataSourceFactory.createDataSource(config.getDataSources(), ruleConfig, config.getProps());
    }
}
