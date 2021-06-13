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

package org.apache.shardingsphere.infra.optimize.core.metadata.refresher.type;

import org.apache.shardingsphere.infra.eventbus.ShardingSphereEventBus;
import org.apache.shardingsphere.infra.metadata.schema.builder.SchemaBuilderMaterials;
import org.apache.shardingsphere.infra.metadata.schema.refresher.event.DropTableEvent;
import org.apache.shardingsphere.infra.optimize.core.metadata.FederateSchemaMetadata;
import org.apache.shardingsphere.infra.optimize.core.metadata.refresher.FederateRefresher;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropTableStatement;

import java.sql.SQLException;
import java.util.Collection;

/**
 * ShardingSphere Federate refresher for drop table statement.
 */
public class DropTableStatementFederateRefresher implements FederateRefresher<DropTableStatement> {

    @Override
    public void refresh(final FederateSchemaMetadata schema, final Collection<String> routeDataSourceNames, final DropTableStatement sqlStatement, final SchemaBuilderMaterials materials) throws SQLException {
        sqlStatement.getTables().forEach(each -> schema.remove(each.getTableName().getIdentifier().getValue()));
        for (SimpleTableSegment each : sqlStatement.getTables()) {
            ShardingSphereEventBus.getInstance().post(new DropTableEvent(each.getTableName().getIdentifier().getValue()));
        }
    }
}
