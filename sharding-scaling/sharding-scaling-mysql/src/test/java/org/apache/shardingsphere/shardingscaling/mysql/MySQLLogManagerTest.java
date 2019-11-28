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

package org.apache.shardingsphere.shardingscaling.mysql;

import lombok.SneakyThrows;

import org.apache.shardingsphere.shardingscaling.core.config.JdbcDataSourceConfiguration;
import org.apache.shardingsphere.shardingscaling.core.config.RdbmsConfiguration;
import org.apache.shardingsphere.shardingscaling.utils.ReflectionUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MySQLLogManagerTest {
    
    private static final String LOG_FILE_NAME = "binlog-000001";
    
    private static final long LOG_POSITION = 4L;
    
    private static final String SERVER_ID = "555555";
    
    @Mock
    private DataSource dataSource;
    
    @Mock
    private Connection connection;
    
    private RdbmsConfiguration rdbmsConfiguration;
    
    @Before
    public void setUp() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        PreparedStatement positionStatement = mockPositionStatement();
        when(connection.prepareStatement("show master status")).thenReturn(positionStatement);
        PreparedStatement serverIdStatement = mockServerIdStatement();
        when(connection.prepareStatement("show variables like 'server_id'")).thenReturn(serverIdStatement);
        rdbmsConfiguration = new RdbmsConfiguration();
        rdbmsConfiguration.setDataSourceConfiguration(new JdbcDataSourceConfiguration("", "", ""));
    }
    
    @Test
    public void assertGetCurrentPosition() throws NoSuchFieldException, IllegalAccessException {
        MySQLLogManager mySQLLogManager = new MySQLLogManager(rdbmsConfiguration);
        ReflectionUtil.setFieldValueToClass(mySQLLogManager, "dataSource", dataSource);
        BinlogPosition actual = mySQLLogManager.getCurrentPosition();
        assertThat(actual.getServerId(), is(SERVER_ID));
        assertThat(actual.getFilename(), is(LOG_FILE_NAME));
        assertThat(actual.getPosition(), is(LOG_POSITION));
    }
    
    @Test(expected = RuntimeException.class)
    public void assertGetCurrentPositionInitFailed() {
        MySQLLogManager mySQLLogManager = new MySQLLogManager(rdbmsConfiguration);
        BinlogPosition actual = mySQLLogManager.getCurrentPosition();
    }
    
    @Test
    public void assertUpdateCurrentPosition() {
        MySQLLogManager mySQLLogManager = new MySQLLogManager(rdbmsConfiguration);
        BinlogPosition expected = new BinlogPosition(SERVER_ID, LOG_FILE_NAME, LOG_POSITION);
        mySQLLogManager.updateCurrentPosition(expected);
        assertThat(mySQLLogManager.getCurrentPosition(), is(expected));
    }
    
    @SneakyThrows
    private PreparedStatement mockPositionStatement() {
        PreparedStatement result = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(result.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn(LOG_FILE_NAME);
        when(resultSet.getLong(2)).thenReturn(LOG_POSITION);
        return result;
    }
    
    @SneakyThrows
    private PreparedStatement mockServerIdStatement() {
        PreparedStatement result = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(result.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(2)).thenReturn(SERVER_ID);
        return result;
    }
}

