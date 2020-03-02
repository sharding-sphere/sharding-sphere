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

package org.apache.shardingsphere.sql.parser.relation.statement.dml;

import lombok.Getter;
import lombok.ToString;
import org.apache.shardingsphere.sql.parser.relation.segment.table.TablesContext;
import org.apache.shardingsphere.sql.parser.relation.statement.CommonSQLStatementContext;
import org.apache.shardingsphere.sql.parser.sql.predicate.PredicateExtractor;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.AndPredicate;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.PredicateSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.WhereSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.UpdateStatement;
import org.apache.shardingsphere.sql.parser.relation.segment.table.TableAvailable;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Update SQL statement context.
 */
@Getter
@ToString(callSuper = true)
public final class UpdateStatementContext extends CommonSQLStatementContext<UpdateStatement> implements TableAvailable {
    
    private final TablesContext tablesContext;
    
    public UpdateStatementContext(final UpdateStatement sqlStatement) {
        super(sqlStatement);
        tablesContext = new TablesContext(sqlStatement.getTables());
    }
    
    @Override
    public Collection<TableSegment> getAllTables() {
        Collection<TableSegment> result = new LinkedList<>(getSqlStatement().getTables());
        if (getSqlStatement().getWhere().isPresent()) {
            result.addAll(getAllTablesFromWhere(getSqlStatement().getWhere().get()));
        }
        return result;
    }
    
    private Collection<TableSegment> getAllTablesFromWhere(final WhereSegment where) {
        Collection<TableSegment> result = new LinkedList<>();
        for (AndPredicate each : where.getAndPredicates()) {
            for (PredicateSegment predicate : each.getPredicates()) {
                result.addAll(new PredicateExtractor(getSqlStatement().getTables(), predicate).extractTables());
            }
        }
        return result;
    }
}
