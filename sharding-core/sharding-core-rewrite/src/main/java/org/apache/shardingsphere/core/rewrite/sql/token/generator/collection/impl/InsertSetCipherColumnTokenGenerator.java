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

package org.apache.shardingsphere.core.rewrite.sql.token.generator.collection.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.Setter;
import org.apache.shardingsphere.core.optimize.statement.SQLStatementContext;
import org.apache.shardingsphere.core.optimize.statement.impl.InsertSQLStatementContext;
import org.apache.shardingsphere.core.parse.sql.segment.dml.assignment.AssignmentSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.assignment.SetAssignmentsSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.expr.simple.ParameterMarkerExpressionSegment;
import org.apache.shardingsphere.core.parse.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.EncryptRuleAware;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.collection.CollectionSQLTokenGenerator;
import org.apache.shardingsphere.core.rewrite.sql.token.pojo.impl.InsertSetCipherColumnToken;
import org.apache.shardingsphere.core.rule.EncryptRule;
import org.apache.shardingsphere.spi.encrypt.ShardingEncryptor;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Insert set cipher column token generator.
 *
 * @author panjuan
 */
@Setter
public final class InsertSetCipherColumnTokenGenerator implements CollectionSQLTokenGenerator, EncryptRuleAware {
    
    private EncryptRule encryptRule;
    
    @Override
    public boolean isGenerateSQLToken(final SQLStatementContext sqlStatementContext) {
        Optional<SetAssignmentsSegment> setAssignmentsSegment = sqlStatementContext.getSqlStatement().findSQLSegment(SetAssignmentsSegment.class);
        return sqlStatementContext.getSqlStatement() instanceof InsertStatement && setAssignmentsSegment.isPresent();
    }
    
    @Override
    public Collection<InsertSetCipherColumnToken> generateSQLTokens(final SQLStatementContext sqlStatementContext) {
        Optional<SetAssignmentsSegment> sqlSegment = sqlStatementContext.getSqlStatement().findSQLSegment(SetAssignmentsSegment.class);
        Preconditions.checkState(sqlSegment.isPresent());
        Collection<InsertSetCipherColumnToken> result = new LinkedList<>();
        for (AssignmentSegment each : sqlSegment.get().getAssignments()) {
            Optional<InsertSetCipherColumnToken> insertSetEncryptValueToken = createInsertSetEncryptValueToken((InsertSQLStatementContext) sqlStatementContext, each);
            if (insertSetEncryptValueToken.isPresent()) {
                result.add(insertSetEncryptValueToken.get());
            }
        }
        return result;
    }
    
    private Optional<InsertSetCipherColumnToken> createInsertSetEncryptValueToken(final InsertSQLStatementContext insertSQLStatementContext, final AssignmentSegment segment) {
        String tableName = insertSQLStatementContext.getTablesContext().getSingleTableName();
        Optional<ShardingEncryptor> shardingEncryptor = encryptRule.findShardingEncryptor(tableName, segment.getColumn().getName());
        if (shardingEncryptor.isPresent()) {
            String cipherColumnName = encryptRule.getCipherColumn(tableName, segment.getColumn().getName());
            ExpressionSegment cipherValue = getCipherValue(insertSQLStatementContext, segment);
            return Optional.of(new InsertSetCipherColumnToken(segment.getStartIndex(), segment.getStopIndex(), cipherColumnName, cipherValue));
        }
        return Optional.absent();
    }
    
    private ExpressionSegment getCipherValue(final InsertSQLStatementContext insertSQLStatementContext, final AssignmentSegment assignmentSegment) {
        return assignmentSegment.getValue() instanceof ParameterMarkerExpressionSegment ? assignmentSegment.getValue()
                : insertSQLStatementContext.getInsertValueContexts().get(0).getValueExpressions().get(insertSQLStatementContext.getColumnNames().indexOf(assignmentSegment.getColumn().getName()));
    }
}
