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

package org.apache.shardingsphere.governance.core.config;

import lombok.Data;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Mock a data source.
 */
@Data
public final class DataSourceFixture implements DataSource {
    
    private String driverClassName;
    
    private String url;
    
    private String username;
    
    private String password;
    
    private List<String> connectionInitSqls;
    
    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }
    
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        return null;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return null;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return false;
    }
    
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }
    
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        
    }
    
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        
    }
    
    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }
    
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
