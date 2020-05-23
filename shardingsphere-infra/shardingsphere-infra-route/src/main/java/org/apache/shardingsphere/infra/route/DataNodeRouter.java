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

package org.apache.shardingsphere.infra.route;

import org.apache.shardingsphere.infra.exception.ShardingSphereException;
import org.apache.shardingsphere.infra.spi.ShardingSphereServiceLoader;
import org.apache.shardingsphere.infra.spi.order.OrderedSPIRegistry;
import org.apache.shardingsphere.sql.parser.binder.SQLStatementContextFactory;
import org.apache.shardingsphere.sql.parser.binder.segment.insert.values.InsertValueContext;
import org.apache.shardingsphere.sql.parser.binder.statement.CommonSQLStatementContext;
import org.apache.shardingsphere.sql.parser.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.sql.parser.binder.statement.dml.InsertStatementContext;
import org.apache.shardingsphere.sql.parser.sql.statement.SQLStatement;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.infra.route.context.RouteContext;
import org.apache.shardingsphere.infra.route.context.RouteResult;
import org.apache.shardingsphere.infra.route.decorator.RouteDecorator;
import org.apache.shardingsphere.infra.route.hook.SPIRoutingHook;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Data node router.
 */
public final class DataNodeRouter {
    
    static {
        ShardingSphereServiceLoader.register(RouteDecorator.class);
    }
    
    private final ShardingSphereMetaData metaData;
    
    private final ConfigurationProperties properties;
    
    private final Map<ShardingSphereRule, RouteDecorator> decorators;
    
    private SPIRoutingHook routingHook;
    
    public DataNodeRouter(final ShardingSphereMetaData metaData, final ConfigurationProperties properties, final Collection<ShardingSphereRule> rules) {
        this.metaData = metaData;
        this.properties = properties;
        decorators = OrderedSPIRegistry.getRegisteredServices(rules, RouteDecorator.class);
        routingHook = new SPIRoutingHook();
    }
    
    /**
     * Route SQL.
     *
     * @param sqlStatement SQL statement
     * @param sql SQL
     * @param parameters SQL parameters
     * @return route context
     */
    public RouteContext route(final SQLStatement sqlStatement, final String sql, final List<Object> parameters) {
        routingHook.start(sql);
        try {
            RouteContext result = executeRoute(sqlStatement, sql, parameters);
            routingHook.finishSuccess(result, metaData.getSchema().getConfiguredSchemaMetaData());
            return result;
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            routingHook.finishFailure(ex);
            throw ex;
        }
    }
    
    @SuppressWarnings("unchecked")
    private RouteContext executeRoute(final SQLStatement sqlStatement, final String sql, final List<Object> parameters) {
        RouteContext result = createRouteContext(sqlStatement, sql, parameters);
        for (Entry<ShardingSphereRule, RouteDecorator> entry : decorators.entrySet()) {
            result = entry.getValue().decorate(result, metaData, entry.getKey(), properties);
        }
        return result;
    }
    
    private RouteContext createRouteContext(final SQLStatement sqlStatement, final String sql, final List<Object> parameters) {
        try {
            SQLStatementContext sqlStatementContext = SQLStatementContextFactory.newInstance(metaData.getSchema().getSchemaMetaData(), sql, parameters, sqlStatement);
            if (sqlStatement instanceof InsertStatement) {
                InsertStatementContext insertStatementContext = (InsertStatementContext) sqlStatementContext;
                InsertStatement insertStatement = (InsertStatement) sqlStatement;
                int count = 0;
                boolean insertWithColumn = insertStatement.getInsertColumns().isPresent() && insertStatement.getInsertColumns().get().getColumns().size() > 0;
                int countColumn = insertStatementContext.getColumnNames().size() - (insertWithColumn ? 0 : (insertStatementContext.getGeneratedKeyContext().isPresent()
                        && insertStatementContext.getGeneratedKeyContext().get().isGenerated()
                        && insertStatementContext.getColumnNames().contains(insertStatementContext.getGeneratedKeyContext().get().getColumnName()) ? 1 : 0));
                for (InsertValueContext each:insertStatementContext.getInsertValueContexts()) {
                    count++;
                    if (each.getValueExpressions().size() != countColumn) {
                        throw new ShardingSphereException("Column count doesn't match value count at row %s", count);
                    }
                }
            }
            return new RouteContext(sqlStatementContext, parameters, new RouteResult());
            // TODO should pass parameters for master-slave
        } catch (final IndexOutOfBoundsException ex) {
            return new RouteContext(new CommonSQLStatementContext(sqlStatement), parameters, new RouteResult());
        }
    }
}
