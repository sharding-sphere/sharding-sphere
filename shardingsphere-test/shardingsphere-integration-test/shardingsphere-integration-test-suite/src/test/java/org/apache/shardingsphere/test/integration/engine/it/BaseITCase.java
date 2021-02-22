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

package org.apache.shardingsphere.test.integration.engine.it;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.test.integration.cases.assertion.IntegrationTestCaseAssertion;
import org.apache.shardingsphere.test.integration.cases.value.SQLValue;
import org.apache.shardingsphere.test.integration.common.SQLExecuteType;
import org.apache.shardingsphere.test.integration.env.EnvironmentType;
import org.apache.shardingsphere.test.integration.env.IntegrationTestEnvironment;
import org.apache.shardingsphere.test.integration.env.database.DatabaseEnvironmentManager;
import org.apache.shardingsphere.test.integration.env.dataset.DataSetEnvironmentManager;
import org.apache.shardingsphere.test.integration.junit.annotation.ContainerInitializer;
import org.apache.shardingsphere.test.integration.junit.annotation.ContainerType;
import org.apache.shardingsphere.test.integration.junit.annotation.Inject;
import org.apache.shardingsphere.test.integration.junit.annotation.OnContainer;
import org.apache.shardingsphere.test.integration.junit.condition.ConditionalOnProperty;
import org.apache.shardingsphere.test.integration.junit.container.ShardingSphereProxyContainer;
import org.apache.shardingsphere.test.integration.junit.container.StorageContainer;
import org.apache.shardingsphere.test.integration.junit.runner.ShardingSphereITRunner;
import org.apache.shardingsphere.test.integration.junit.runner.TestCaseDescription;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Getter(AccessLevel.PROTECTED)
@RunWith(ShardingSphereITRunner.class)
public abstract class BaseITCase {
    
    public static final String NOT_VERIFY_FLAG = "NOT_VERIFY";
    
    @Getter
    @OnContainer(name = "proxy")
    @ConditionalOnProperty(key = "it.adapter", expected = "proxy")
    private static ShardingSphereProxyContainer proxy;
    
    @Getter
    @OnContainer(name = "storage", type = ContainerType.STORAGE, hostName = "mysql.db.host")
    private static StorageContainer storage;
    
    @Inject
    private IntegrationTestCaseAssertion assertion;
    
    @Inject
    private DataSetEnvironmentManager dataSetEnvironmentManager;
    
    @Inject
    private String statement;
    
    @Inject
    private TestCaseDescription description;
    
    @Inject
    private SQLExecuteType sqlExecuteType;
    
    @Inject
    private String parentPath;
    
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    
    @ContainerInitializer
    protected static void initialize() {
        if (Objects.nonNull(proxy) && Objects.nonNull(storage)) {
            proxy.dependsOn(storage);
        }
    }
    
    protected DataSource getTargetDataSource() {
        return proxy.getDataSource();
    }
    
    protected String getStatement() throws ParseException {
        return sqlExecuteType == SQLExecuteType.Literal ? getLiteralSQL(statement) : statement;
    }
    
    protected String getLiteralSQL(final String sql) throws ParseException {
        List<Object> parameters = null == getAssertion() ? Collections.emptyList() : getAssertion().getSQLValues().stream().map(SQLValue::toString).collect(Collectors.toList());
        return parameters.isEmpty() ? sql : String.format(sql.replace("%", "$").replace("?", "%s"), parameters.toArray()).replace("$", "%").replace("%%", "%").replace("'%'", "'%%'");
    }
    
    @BeforeClass
    public static void executeInitSQLs() throws IOException, JAXBException, SQLException {
        if (EnvironmentType.DOCKER != IntegrationTestEnvironment.getInstance().getEnvType()) {
            DatabaseEnvironmentManager.executeInitSQLs();
        }
    }
    
    @After
    public final void tearDown() {
        if (getTargetDataSource() instanceof ShardingSphereDataSource) {
            ((ShardingSphereDataSource) getTargetDataSource()).getMetaDataContexts().getExecutorEngine().close();
        }
    }
}
