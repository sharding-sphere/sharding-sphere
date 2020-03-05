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

package org.apache.shardingsphere.sql.parser.relation.statement.ddl;

import lombok.Getter;
import org.apache.shardingsphere.sql.parser.relation.segment.table.TableAvailable;
import org.apache.shardingsphere.sql.parser.relation.segment.table.TablesContext;
import org.apache.shardingsphere.sql.parser.relation.statement.CommonSQLStatementContext;
import org.apache.shardingsphere.sql.parser.sql.segment.ddl.index.IndexSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.CreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.generic.IndexSegmentsAvailable;

import java.util.Collection;
import java.util.Collections;

/**
 * Create index statement context.
 */
@Getter
public final class CreateIndexStatementContext extends CommonSQLStatementContext<CreateIndexStatement> implements TableAvailable, IndexSegmentsAvailable {
    
    private final TablesContext tablesContext;
    
    public CreateIndexStatementContext(final CreateIndexStatement sqlStatement) {
        super(sqlStatement);
        tablesContext = new TablesContext(sqlStatement.getTable());
    }
    
    @Override
    public Collection<TableSegment> getAllTables() {
        return null == getSqlStatement().getTable() ? Collections.emptyList() : Collections.singletonList(getSqlStatement().getTable());
    }
    
    @Override
    public Collection<IndexSegment> getIndexes() {
        return null == getSqlStatement().getIndex() ? Collections.emptyList() : Collections.singletonList(getSqlStatement().getIndex());
    }
}
