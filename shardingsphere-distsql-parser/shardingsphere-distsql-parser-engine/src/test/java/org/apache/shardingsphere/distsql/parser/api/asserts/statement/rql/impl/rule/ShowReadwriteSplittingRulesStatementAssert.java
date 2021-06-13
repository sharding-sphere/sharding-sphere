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

package org.apache.shardingsphere.distsql.parser.api.asserts.statement.rql.impl.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.distsql.parser.api.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.distsql.parser.api.asserts.segment.SchemaAssert;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rql.impl.ShowReadWriteSplittingRulesStatementTestCase;
import org.apache.shardingsphere.distsql.parser.statement.rql.show.impl.ShowReadwriteSplittingRulesStatement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Show readwrite splitting rule statement rules assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShowReadwriteSplittingRulesStatementAssert {

    /**
     * Assert show readwrite splitting rule statement is correct with expected parser result.
     *
     * @param assertContext assert context
     * @param actual        actual show readwrite splitting rules statement
     * @param expected      expected show readwrite splitting rules statement test case
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final ShowReadwriteSplittingRulesStatement actual, final ShowReadWriteSplittingRulesStatementTestCase expected) {
        if (null != expected.getSchema()) {
            assertTrue(assertContext.getText("Actual schema should exist."), actual.getSchema().isPresent());
            SchemaAssert.assertIs(assertContext, actual.getSchema().get(), expected.getSchema());
        } else {
            assertFalse(assertContext.getText("Actual schema should not exist."), actual.getSchema().isPresent());
        }
    }
}
