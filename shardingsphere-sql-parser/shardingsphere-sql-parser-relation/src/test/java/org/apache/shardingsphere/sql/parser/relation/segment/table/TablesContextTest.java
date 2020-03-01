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

package org.apache.shardingsphere.sql.parser.relation.segment.table;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.apache.shardingsphere.sql.parser.relation.metadata.RelationMetas;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.AliasSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.OwnerSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement;
import org.apache.shardingsphere.sql.parser.sql.value.identifier.IdentifierValue;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class TablesContextTest {
    
    @Test
    public void assertGetTableNames() {
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.getTables().add(createTableSegment("table_1", "tbl_1"));
        selectStatement.getTables().add(createTableSegment("table_2", "tbl_2"));
        TablesContext tablesContext = new TablesContext(selectStatement);
        assertThat(tablesContext.getTableNames(), CoreMatchers.<Collection<String>>is(Sets.newHashSet("table_1", "table_2")));
    }
    
    @Test
    public void assertFindTableNameWhenSingleTable() {
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.getTables().add(createTableSegment("table_1", "tbl_1"));
        assertTrue(new TablesContext(selectStatement).findTableName(null, null).isPresent());
    }
    
    @Test
    public void assertFindTableNameWhenColumnSegmentOwnerPresent() {
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.getTables().add(createTableSegment("table_1", "tbl_1"));
        selectStatement.getTables().add(createTableSegment("table_2", "tbl_2"));
        TablesContext tablesContext = new TablesContext(selectStatement);
        ColumnSegment columnSegment = mock(ColumnSegment.class);
        when(columnSegment.getOwner()).thenReturn(Optional.of(new OwnerSegment(0, 10, new IdentifierValue("table_1"))));
        assertTrue(tablesContext.findTableName(columnSegment, null).isPresent());
    }
    
    @Test
    public void assertFindTableNameWhenColumnSegmentOwnerAbsent() {
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.getTables().add(createTableSegment("table_1", "tbl_1"));
        selectStatement.getTables().add(createTableSegment("table_2", "tbl_2"));
        TablesContext tablesContext = new TablesContext(selectStatement);
        ColumnSegment columnSegment = new ColumnSegment(0, 0, new IdentifierValue("col"));
        RelationMetas relationMetas = mock(RelationMetas.class);
        assertFalse(tablesContext.findTableName(columnSegment, relationMetas).isPresent());
    }
    
    @Test
    public void assertFindTableNameWhenColumnSegmentOwnerAbsentAndRelationMetasContainsColumn() {
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.getTables().add(createTableSegment("table_1", "tbl_1"));
        selectStatement.getTables().add(createTableSegment("table_2", "tbl_2"));
        ColumnSegment columnSegment = new ColumnSegment(0, 0, new IdentifierValue("col"));
        RelationMetas relationMetas = mock(RelationMetas.class);
        when(relationMetas.containsColumn(anyString(), anyString())).thenReturn(true);
        assertTrue(new TablesContext(selectStatement).findTableName(columnSegment, relationMetas).isPresent());
    }
    
    @Test
    public void assertInstanceCreatedWhenNoExceptionThrown() {
        InsertStatement sqlStatement = new InsertStatement();
        TableSegment tableSegment = new TableSegment(0, 10, new IdentifierValue("tbl"));
        tableSegment.setOwner(new OwnerSegment(0, 0, new IdentifierValue("schema")));
        sqlStatement.setTable(tableSegment);
        new TablesContext(sqlStatement);
    }
    
    private TableSegment createTableSegment(final String tableName, final String alias) {
        TableSegment result = new TableSegment(0, 0, new IdentifierValue(tableName));
        AliasSegment aliasSegment = new AliasSegment(0, 0, new IdentifierValue(alias));
        result.setAlias(aliasSegment);
        return result;
    }
}
