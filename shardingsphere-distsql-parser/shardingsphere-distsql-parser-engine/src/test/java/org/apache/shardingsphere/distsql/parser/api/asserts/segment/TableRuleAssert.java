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

package org.apache.shardingsphere.distsql.parser.api.asserts.segment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.distsql.parser.api.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.segment.impl.ExpectedTableRule;
import org.apache.shardingsphere.distsql.parser.segment.TableRuleSegment;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Table rule assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TableRuleAssert {

    /**
     * Assert table rule is correct with expected parser result.
     *
     * @param assertContext assert context
     * @param actual        actual table rule
     * @param expected      expected table rule test case
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final TableRuleSegment actual, final ExpectedTableRule expected) {
        if (null != expected) {
            assertNotNull(assertContext.getText("Actual should exist."), actual);
            assertThat(assertContext.getText(String.format("`%s`'s table rule segment assertion error: ", actual.getClass().getSimpleName())),
                    actual.getLogicTable(), is(expected.getLogicTable()));
            assertThat(assertContext.getText(String.format("`%s`'s table rule segment assertion error: ", actual.getClass().getSimpleName())),
                    actual.getDataSources(), is(expected.getDataSources()));
            assertThat(assertContext.getText(String.format("`%s`'s table rule segment assertion error: ", actual.getClass().getSimpleName())),
                    actual.getTableStrategyColumn(), is(expected.getTableStrategyColumn()));
            assertThat(assertContext.getText(String.format("`%s`'s table rule segment assertion error: ", actual.getClass().getSimpleName())),
                    actual.getKeyGenerateStrategyColumn(), is(expected.getKeyGenerateStrategyColumn()));
            FunctionAssert.assertIs(assertContext, actual.getTableStrategy(), expected.getTableStrategy());
            FunctionAssert.assertIs(assertContext, actual.getKeyGenerateStrategy(), expected.getKeyGenerateStrategy());
        } else {
            assertNull(assertContext.getText("Actual should not exist."), actual);
        }
    }
}
