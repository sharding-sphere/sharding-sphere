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

package org.apache.shardingsphere.underlying.common.metadata.refresh.impl;

import org.apache.shardingsphere.sql.parser.binder.statement.ddl.AlterTableStatementContext;

import org.apache.shardingsphere.underlying.common.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.underlying.common.metadata.refresh.MetaDataRefreshStrategy;
import org.apache.shardingsphere.underlying.common.metadata.refresh.TableMetaDataLoaderCallback;

import java.sql.SQLException;

/**
 * Alter table statement meta data refresh strategy.
 */
public final class AlterTableStatementMetaDataRefreshStrategy implements MetaDataRefreshStrategy<AlterTableStatementContext> {
    
    @Override
    public void refreshMetaData(final ShardingSphereMetaData metaData, final AlterTableStatementContext sqlStatementContext, final TableMetaDataLoaderCallback callback) throws SQLException {
        String tableName = sqlStatementContext.getSqlStatement().getTable().getTableName().getIdentifier().getValue();
        metaData.getSchema().put(tableName, callback.load(tableName));
    }
}

