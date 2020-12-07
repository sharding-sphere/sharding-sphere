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

package org.apache.shardingsphere.driver.executor;

import org.apache.shardingsphere.driver.executor.callback.DriverJDBCExecutorCallback;
import org.apache.shardingsphere.infra.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.context.metadata.MetaDataContexts;
import org.apache.shardingsphere.infra.executor.kernel.model.ExecutionGroup;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.ConnectionMode;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.SQLExecutorExceptionHandler;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutionUnit;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutor;
import org.apache.shardingsphere.infra.executor.sql.execute.engine.driver.jdbc.JDBCExecutorCallback;
import org.apache.shardingsphere.infra.executor.sql.execute.result.query.QueryResult;
import org.apache.shardingsphere.infra.route.context.RouteUnit;
import org.apache.shardingsphere.infra.rule.type.DataNodeContainedRule;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Statement executor.
 */
public final class StatementExecutor extends AbstractStatementExecutor {
    
    public StatementExecutor(final Map<String, DataSource> dataSourceMap, final MetaDataContexts metaDataContexts, final JDBCExecutor jdbcExecutor) {
        super(dataSourceMap, metaDataContexts, jdbcExecutor);
    }
    
    @Override
    public List<QueryResult> executeQuery(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups) throws SQLException {
        boolean isExceptionThrown = SQLExecutorExceptionHandler.isExceptionThrown();
        JDBCExecutorCallback<QueryResult> callback = createJDBCExecutorCallbackWithQueryResult(isExceptionThrown);
        return getJdbcExecutor().execute(executionGroups, callback);
    }
    
    private JDBCExecutorCallback<QueryResult> createJDBCExecutorCallbackWithQueryResult(final boolean isExceptionThrown) {
        return new DriverJDBCExecutorCallback(getMetaDataContexts().getDatabaseType(), isExceptionThrown) {
            
            @Override
            protected ResultSet execute(final String sql, final Statement statement) throws SQLException {
                return statement.executeQuery(sql);
            }
        };
    }
    
    @Override
    public int executeUpdate(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, 
                             final SQLStatementContext<?> sqlStatementContext, final Collection<RouteUnit> routeUnits) throws SQLException {
        return executeUpdate(executionGroups, (sql, statement) -> statement.executeUpdate(sql), sqlStatementContext, routeUnits);
    }
    
    /**
     * Execute update with auto generated keys.
     * 
     * @param executionGroups execution groups
     * @param sqlStatementContext SQL statement context
     * @param routeUnits route units
     * @param autoGeneratedKeys auto generated keys' flag
     * @return effected records count
     * @throws SQLException SQL exception
     */
    public int executeUpdate(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final SQLStatementContext<?> sqlStatementContext,
                             final Collection<RouteUnit> routeUnits, final int autoGeneratedKeys) throws SQLException {
        return executeUpdate(executionGroups, (sql, statement) -> statement.executeUpdate(sql, autoGeneratedKeys), sqlStatementContext, routeUnits);
    }
    
    /**
     * Execute update with column indexes.
     *
     * @param executionGroups execution groups
     * @param sqlStatementContext SQL statement context
     * @param routeUnits route units
     * @param columnIndexes column indexes
     * @return effected records count
     * @throws SQLException SQL exception
     */
    public int executeUpdate(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final SQLStatementContext<?> sqlStatementContext,
                             final Collection<RouteUnit> routeUnits, final int[] columnIndexes) throws SQLException {
        return executeUpdate(executionGroups, (sql, statement) -> statement.executeUpdate(sql, columnIndexes), sqlStatementContext, routeUnits);
    }
    
    /**
     * Execute update with column names.
     *
     * @param executionGroups execution groups
     * @param sqlStatementContext SQL statement context
     * @param routeUnits route units
     * @param columnNames column names
     * @return effected records count
     * @throws SQLException SQL exception
     */
    public int executeUpdate(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final SQLStatementContext<?> sqlStatementContext,
                             final Collection<RouteUnit> routeUnits, final String[] columnNames) throws SQLException {
        return executeUpdate(executionGroups, (sql, statement) -> statement.executeUpdate(sql, columnNames), sqlStatementContext, routeUnits);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private int executeUpdate(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final Updater updater,
                              final SQLStatementContext<?> sqlStatementContext, final Collection<RouteUnit> routeUnits) throws SQLException {
        boolean isExceptionThrown = SQLExecutorExceptionHandler.isExceptionThrown();
        JDBCExecutorCallback jdbcExecutorCallback = new JDBCExecutorCallback<Integer>(getMetaDataContexts().getDatabaseType(), isExceptionThrown) {
            
            @Override
            protected Integer executeSQL(final String sql, final Statement statement, final ConnectionMode connectionMode) throws SQLException {
                return updater.executeUpdate(sql, statement);
            }
        };
        List<Integer> results = getJdbcExecutor().execute(executionGroups, jdbcExecutorCallback);
        refreshSchema(getMetaDataContexts().getDefaultMetaData(), sqlStatementContext.getSqlStatement(), routeUnits);
        if (isNeedAccumulate(getMetaDataContexts().getDefaultMetaData().getRuleMetaData().getRules().stream().filter(
            rule -> rule instanceof DataNodeContainedRule).collect(Collectors.toList()), sqlStatementContext)) {
            return accumulate(results);
        }
        return null == results.get(0) ? 0 : results.get(0);
    }
    
    @Override
    public boolean execute(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final SQLStatement sqlStatement, final Collection<RouteUnit> routeUnits) throws SQLException {
        return execute(executionGroups, (sql, statement) -> statement.execute(sql), sqlStatement, routeUnits);
    }
    
    /**
     * Execute SQL with auto generated keys.
     *
     * @param executionGroups execution groups
     * @param sqlStatement SQL statement
     * @param routeUnits route units
     * @param autoGeneratedKeys auto generated keys' flag
     * @return return true if is DQL, false if is DML
     * @throws SQLException SQL exception
     */
    public boolean execute(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final SQLStatement sqlStatement,
                           final Collection<RouteUnit> routeUnits, final int autoGeneratedKeys) throws SQLException {
        return execute(executionGroups, (sql, statement) -> statement.execute(sql, autoGeneratedKeys), sqlStatement, routeUnits);
    }
    
    /**
     * Execute SQL with column indexes.
     *
     * @param executionGroups execution groups
     * @param sqlStatement SQL statement
     * @param routeUnits route units
     * @param columnIndexes column indexes
     * @return return true if is DQL, false if is DML
     * @throws SQLException SQL exception
     */
    public boolean execute(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final SQLStatement sqlStatement,
                           final Collection<RouteUnit> routeUnits, final int[] columnIndexes) throws SQLException {
        return execute(executionGroups, (sql, statement) -> statement.execute(sql, columnIndexes), sqlStatement, routeUnits);
    }
    
    /**
     * Execute SQL with column names.
     *
     * @param executionGroups execution groups
     * @param sqlStatement SQL statement
     * @param routeUnits route units
     * @param columnNames column names
     * @return return true if is DQL, false if is DML
     * @throws SQLException SQL exception
     */
    public boolean execute(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final SQLStatement sqlStatement,
                           final Collection<RouteUnit> routeUnits, final String[] columnNames) throws SQLException {
        return execute(executionGroups, (sql, statement) -> statement.execute(sql, columnNames), sqlStatement, routeUnits);
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean execute(final Collection<ExecutionGroup<JDBCExecutionUnit>> executionGroups, final Executor executor,
                            final SQLStatement sqlStatement, final Collection<RouteUnit> routeUnits) throws SQLException {
        boolean isExceptionThrown = SQLExecutorExceptionHandler.isExceptionThrown();
        JDBCExecutorCallback jdbcExecutorCallback = new JDBCExecutorCallback<Boolean>(getMetaDataContexts().getDatabaseType(), isExceptionThrown) {
            
            @Override
            protected Boolean executeSQL(final String sql, final Statement statement, final ConnectionMode connectionMode) throws SQLException {
                return executor.execute(sql, statement);
            }
        };
        return executeAndRefreshMetaData(executionGroups, sqlStatement, routeUnits, jdbcExecutorCallback);
    }
    
    private interface Updater {
        
        int executeUpdate(String sql, Statement statement) throws SQLException;
    }
    
    private interface Executor {
        
        boolean execute(String sql, Statement statement) throws SQLException;
    }
}
