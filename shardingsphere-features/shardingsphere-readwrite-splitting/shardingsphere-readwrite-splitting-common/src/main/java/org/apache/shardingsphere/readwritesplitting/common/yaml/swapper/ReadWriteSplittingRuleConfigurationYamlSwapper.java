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

package org.apache.shardingsphere.readwritesplitting.common.yaml.swapper;

import org.apache.shardingsphere.readwritesplitting.common.constant.ReadwriteSplittingOrder;
import org.apache.shardingsphere.readwritesplitting.common.yaml.config.YamlReadWriteSplittingRuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.infra.yaml.swapper.YamlRuleConfigurationSwapper;
import org.apache.shardingsphere.infra.yaml.swapper.algorithm.ShardingSphereAlgorithmConfigurationYamlSwapper;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.common.yaml.config.rule.YamlReadWriteSplittingDataSourceRuleConfiguration;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Readwrite-splitting rule configuration YAML swapper.
 */
public final class ReadWriteSplittingRuleConfigurationYamlSwapper 
        implements YamlRuleConfigurationSwapper<YamlReadWriteSplittingRuleConfiguration, ReadwriteSplittingRuleConfiguration> {
    
    private final ShardingSphereAlgorithmConfigurationYamlSwapper algorithmSwapper = new ShardingSphereAlgorithmConfigurationYamlSwapper();
    
    @Override
    public YamlReadWriteSplittingRuleConfiguration swapToYamlConfiguration(final ReadwriteSplittingRuleConfiguration data) {
        YamlReadWriteSplittingRuleConfiguration result = new YamlReadWriteSplittingRuleConfiguration();
        result.setDataSources(data.getDataSources().stream().collect(
                Collectors.toMap(ReadwriteSplittingDataSourceRuleConfiguration::getName, this::swapToYamlConfiguration, (oldValue, currentValue) -> oldValue, LinkedHashMap::new)));
        if (null != data.getLoadBalancers()) {
            data.getLoadBalancers().forEach((key, value) -> result.getLoadBalancers().put(key, algorithmSwapper.swapToYamlConfiguration(value)));
        }
        return result;
    }
    
    private YamlReadWriteSplittingDataSourceRuleConfiguration swapToYamlConfiguration(final ReadwriteSplittingDataSourceRuleConfiguration dataSourceRuleConfig) {
        YamlReadWriteSplittingDataSourceRuleConfiguration result = new YamlReadWriteSplittingDataSourceRuleConfiguration();
        result.setName(dataSourceRuleConfig.getName());
        result.setAutoAwareDataSourceName(dataSourceRuleConfig.getAutoAwareDataSourceName());
        result.setWriteDataSourceName(dataSourceRuleConfig.getWriteDataSourceName());
        result.setReadDataSourceNames(dataSourceRuleConfig.getReadDataSourceNames());
        result.setLoadBalancerName(dataSourceRuleConfig.getLoadBalancerName());
        return result;
    }
    
    @Override
    public ReadwriteSplittingRuleConfiguration swapToObject(final YamlReadWriteSplittingRuleConfiguration yamlConfig) {
        Collection<ReadwriteSplittingDataSourceRuleConfiguration> dataSources = new LinkedList<>();
        for (Entry<String, YamlReadWriteSplittingDataSourceRuleConfiguration> entry : yamlConfig.getDataSources().entrySet()) {
            dataSources.add(swapToObject(entry.getKey(), entry.getValue()));
        }
        Map<String, ShardingSphereAlgorithmConfiguration> loadBalancerMap = new LinkedHashMap<>(yamlConfig.getLoadBalancers().entrySet().size(), 1);
        if (null != yamlConfig.getLoadBalancers()) {
            yamlConfig.getLoadBalancers().forEach((key, value) -> loadBalancerMap.put(key, algorithmSwapper.swapToObject(value)));
        }
        return new ReadwriteSplittingRuleConfiguration(dataSources, loadBalancerMap);
    }
    
    private ReadwriteSplittingDataSourceRuleConfiguration swapToObject(final String name, final YamlReadWriteSplittingDataSourceRuleConfiguration yamlDataSourceRuleConfig) {
        return new ReadwriteSplittingDataSourceRuleConfiguration(name, yamlDataSourceRuleConfig.getAutoAwareDataSourceName(),
                yamlDataSourceRuleConfig.getWriteDataSourceName(), yamlDataSourceRuleConfig.getReadDataSourceNames(), yamlDataSourceRuleConfig.getLoadBalancerName());
    }
    
    @Override
    public Class<ReadwriteSplittingRuleConfiguration> getTypeClass() {
        return ReadwriteSplittingRuleConfiguration.class;
    }
    
    @Override
    public String getRuleTagName() {
        return "READWRITE_SPLITTING";
    }
    
    @Override
    public int getOrder() {
        return ReadwriteSplittingOrder.ORDER;
    }
}
