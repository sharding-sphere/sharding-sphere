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

package org.apache.shardingsphere.sql.parser.integrate.asserts.statement;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sql.parser.integrate.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.parameter.ParameterMarkerAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.ddl.AlterTableStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.ddl.CreateIndexStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.ddl.CreateTableStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.dml.DeleteStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.ddl.DropIndexStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.dml.InsertStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.dml.SelectStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.tcl.TCLStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.statement.dml.UpdateStatementAssert;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.SQLParserTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.ddl.AlterTableStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.ddl.CreateIndexStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.ddl.CreateTableStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.ddl.DropIndexStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.dml.DeleteStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.dml.InsertStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.dml.SelectStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.dml.UpdateStatementTestCase;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.statement.tcl.TCLStatementTestCase;
import org.apache.shardingsphere.sql.parser.sql.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.AlterTableStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.CreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.CreateTableStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.DropIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.DeleteStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.UpdateStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.TCLStatement;

/**
 * SQL statement assert.
 *
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQLStatementAssert {
    
    /**
     * Assert SQL statement is correct with expected parser result.
     * 
     * @param assertContext assert context
     * @param actual actual SQL statement
     * @param expected expected parser result
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final SQLStatement actual, final SQLParserTestCase expected) {
        ParameterMarkerAssert.assertCount(assertContext, actual.getParametersCount(), expected.getParameters().size());
        if (actual instanceof SelectStatement) {
            SelectStatementAssert.assertIs(assertContext, (SelectStatement) actual, (SelectStatementTestCase) expected);
        } else if (actual instanceof UpdateStatement) {
            UpdateStatementAssert.assertIs(assertContext, (UpdateStatement) actual, (UpdateStatementTestCase) expected);
        } else if (actual instanceof DeleteStatement) {
            DeleteStatementAssert.assertIs(assertContext, (DeleteStatement) actual, (DeleteStatementTestCase) expected);
        } else if (actual instanceof InsertStatement) {
            InsertStatementAssert.assertIs(assertContext, (InsertStatement) actual, (InsertStatementTestCase) expected);
        } else if (actual instanceof CreateTableStatement) {
            CreateTableStatementAssert.assertIs(assertContext, (CreateTableStatement) actual, (CreateTableStatementTestCase) expected);
        } else if (actual instanceof AlterTableStatement) {
            AlterTableStatementAssert.assertIs(assertContext, (AlterTableStatement) actual, (AlterTableStatementTestCase) expected);
        } else if (actual instanceof CreateIndexStatement) {
            CreateIndexStatementAssert.assertIs(assertContext, (CreateIndexStatement) actual, (CreateIndexStatementTestCase) expected);
        } else if (actual instanceof DropIndexStatement) {
            DropIndexStatementAssert.assertIs(assertContext, (DropIndexStatement) actual, (DropIndexStatementTestCase) expected);
        } else if (actual instanceof TCLStatement) {
            TCLStatementAssert.assertIs((TCLStatement) actual, (TCLStatementTestCase) expected);
        }
    }
}
