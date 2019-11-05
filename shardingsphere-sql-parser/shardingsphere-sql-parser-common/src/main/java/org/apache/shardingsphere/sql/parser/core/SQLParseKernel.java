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

package org.apache.shardingsphere.sql.parser.core;

import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.shardingsphere.spi.database.BranchDatabaseType;
import org.apache.shardingsphere.spi.database.DatabaseType;
import org.apache.shardingsphere.sql.parser.core.extractor.SQLSegmentsExtractorEngine;
import org.apache.shardingsphere.sql.parser.core.filler.SQLStatementFillerEngine;
import org.apache.shardingsphere.sql.parser.core.parser.SQLAST;
import org.apache.shardingsphere.sql.parser.core.parser.SQLParserEngine;
import org.apache.shardingsphere.sql.parser.core.rule.registry.ParseRuleRegistry;
import org.apache.shardingsphere.sql.parser.sql.segment.SQLSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.SQLStatement;

import java.util.Collection;
import java.util.Map;

/**
 * SQL parse kernel.
 *
 * @author duhongjun
 * @author zhangliang
 */
public final class SQLParseKernel {
    
    private final SQLParserEngine parserEngine;
    
    private final SQLSegmentsExtractorEngine extractorEngine;
    
    private final SQLStatementFillerEngine fillerEngine;
    
    public SQLParseKernel(final ParseRuleRegistry parseRuleRegistry, final DatabaseType databaseType, final String sql) {
        String databaseTypeName = getDatabaseTypeName(databaseType);
        parserEngine = new SQLParserEngine(parseRuleRegistry, databaseTypeName, sql);
        extractorEngine = new SQLSegmentsExtractorEngine();
        fillerEngine = new SQLStatementFillerEngine(parseRuleRegistry, databaseTypeName);
    }
    
    private static String getDatabaseTypeName(final DatabaseType databaseType) {
        if (databaseType instanceof BranchDatabaseType) {
            return ((BranchDatabaseType) databaseType).getTrunkDatabaseType().getName();
        }
        return databaseType.getName();
    }
    
    /**
     * Parse SQL.
     *
     * @return SQL statement
     */
    public SQLStatement parse() {
        SQLAST ast = parserEngine.parse();
        Collection<SQLSegment> sqlSegments = extractorEngine.extract(ast);
        Map<ParserRuleContext, Integer> parameterMarkerIndexes = ast.getParameterMarkerIndexes();
        return fillerEngine.fill(sqlSegments, parameterMarkerIndexes.size(), ast.getSqlStatementRule());
    }
}
