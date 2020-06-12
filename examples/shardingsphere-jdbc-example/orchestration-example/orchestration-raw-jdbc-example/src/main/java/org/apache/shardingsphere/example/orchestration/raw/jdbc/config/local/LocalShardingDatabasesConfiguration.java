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

package org.apache.shardingsphere.example.orchestration.raw.jdbc.config.local;

import org.apache.shardingsphere.driver.orchestration.api.OrchestrationShardingSphereDataSourceFactory;
import org.apache.shardingsphere.example.config.ExampleConfiguration;
import org.apache.shardingsphere.example.core.api.DataSourceUtil;
import org.apache.shardingsphere.orchestration.center.config.CenterConfiguration;
import org.apache.shardingsphere.orchestration.center.config.OrchestrationConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.algorithm.KeyGenerateAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.strategy.algorithm.sharding.inline.InlineShardingAlgorithm;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class LocalShardingDatabasesConfiguration implements ExampleConfiguration {
    
    private final Map<String, CenterConfiguration> centerConfigurationMap;
    
    public LocalShardingDatabasesConfiguration(final Map<String, CenterConfiguration> centerConfigurationMap) {
        this.centerConfigurationMap = centerConfigurationMap;
    }
    
    private static KeyGenerateStrategyConfiguration getKeyGeneratorConfiguration() {
        return new KeyGenerateStrategyConfiguration("order_id", "snowflake");
    }
    
    @Override
    public DataSource getDataSource() throws SQLException {
        OrchestrationConfiguration orchestrationConfig = new OrchestrationConfiguration(centerConfigurationMap);
        return OrchestrationShardingSphereDataSourceFactory.createDataSource(createDataSourceMap(), Collections.singleton(createShardingRuleConfiguration()), new Properties(), orchestrationConfig);
    }
    
    private ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        result.getTables().add(getOrderTableRuleConfiguration());
        result.getTables().add(getOrderItemTableRuleConfiguration());
        result.getBindingTableGroups().add("t_order, t_order_item");
        result.getBroadcastTables().add("t_address");
        InlineShardingAlgorithm shardingAlgorithm = new InlineShardingAlgorithm();
        shardingAlgorithm.getProps().setProperty("algorithm.expression", "demo_ds_${user_id % 2}");
        result.setDefaultDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", shardingAlgorithm));
        result.getKeyGenerators().put("snowflake", new KeyGenerateAlgorithmConfiguration("SNOWFLAKE", getProps()));
        return result;
    }
    
    private ShardingTableRuleConfiguration getOrderTableRuleConfiguration() {
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("t_order");
        result.setKeyGenerateStrategy(getKeyGeneratorConfiguration());
        return result;
    }
    
    private ShardingTableRuleConfiguration getOrderItemTableRuleConfiguration() {
        return new ShardingTableRuleConfiguration("t_order_item");
    }
    
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("demo_ds_0", DataSourceUtil.createDataSource("demo_ds_0"));
        result.put("demo_ds_1", DataSourceUtil.createDataSource("demo_ds_1"));
        return result;
    }
    
    private static Properties getProps() {
        Properties result = new Properties();
        result.setProperty("worker.id", "123");
        return result;
    }
}
