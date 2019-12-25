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

package org.apache.shardingsphere.core.rewrite.feature.shadow.token.generator.impl;

import com.google.common.base.Preconditions;
import lombok.Setter;
import org.apache.shardingsphere.core.rewrite.feature.shadow.token.generator.BaseShadowSQLTokenGenerator;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.CollectionSQLTokenGenerator;
import org.apache.shardingsphere.core.rewrite.sql.token.pojo.SQLToken;
import org.apache.shardingsphere.core.rewrite.sql.token.pojo.generic.RemoveToken;
import org.apache.shardingsphere.sql.parser.relation.statement.SQLStatementContext;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.AndPredicate;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.PredicateSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.generic.WhereSegmentAvailable;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Predicate column token generator for shadow.
 *
 * @author zhyee
 */
@Setter
public final class ShadowPredicateColumnTokenGenerator extends BaseShadowSQLTokenGenerator implements CollectionSQLTokenGenerator {

    @Override
    protected boolean isGenerateSQLTokenForShadow(final SQLStatementContext sqlStatementContext) {
        return sqlStatementContext.getSqlStatement() instanceof WhereSegmentAvailable && ((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().isPresent();
    }

    @Override
    public Collection<SQLToken> generateSQLTokens(final SQLStatementContext sqlStatementContext) {
        Preconditions.checkState(((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().isPresent());
        Collection<SQLToken> result = new LinkedList<>();
        for (AndPredicate each : ((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().get().getAndPredicates()) {
            result.addAll(generateSQLTokens(sqlStatementContext, each));
        }
        return result;
    }

    private Collection<SQLToken> generateSQLTokens(final SQLStatementContext sqlStatementContext, final AndPredicate andPredicate) {
        Collection<SQLToken> result = new LinkedList<>();
        LinkedList<PredicateSegment> predicates = (LinkedList<PredicateSegment>) andPredicate.getPredicates();
        for (int i = 0; i < predicates.size(); i++) {
            if (!getShadowRule().getColumn().equals(predicates.get(i).getColumn().getName())) {
                continue;
            }
            if (predicates.size() == 1) {
                int startIndex = ((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().get().getStartIndex();
                int stopIndex = ((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().get().getStopIndex();
                result.add(new RemoveToken(startIndex, stopIndex));
                return result;
            }
            if (i == 0) {
                int startIndex = predicates.get(i).getStartIndex();
                int stopIndex = predicates.get(i + 1).getStartIndex() - 1;
                result.add(new RemoveToken(startIndex, stopIndex));
                return result;
            }
            int startIndex = predicates.get(i - 1).getStopIndex() + 1;
            int stopIndex = predicates.get(i).getStopIndex();
            result.add(new RemoveToken(startIndex, stopIndex));
            return result;
        }
        return result;
    }
}
