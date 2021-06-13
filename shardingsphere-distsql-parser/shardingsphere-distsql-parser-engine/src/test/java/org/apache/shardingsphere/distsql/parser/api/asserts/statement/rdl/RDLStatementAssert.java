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

package org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.alter.AlterRuleStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.create.AddResourceStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.create.CreateRuleStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.alter.impl.AlterDatabaseDiscoveryRuleStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.alter.impl.AlterEncryptRuleStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.alter.impl.AlterReadwriteSplittingRuleStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.alter.impl.AlterShardingBindingTableRulesStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.alter.impl.AlterShardingBroadcastTableRulesStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.alter.impl.AlterShardingTableRuleStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.drop.DropResourceStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.asserts.statement.rdl.drop.DropRuleStatementAssert;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.SQLParserTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.alter.AlterDataBaseDiscoveryRuleStatementTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.alter.AlterEncryptRuleStatementTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.alter.AlterReadWriteSplittingRuleStatementTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.alter.AlterShardingBindingTableRulesStatementTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.alter.AlterShardingBroadcastTableRulesStatementTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.alter.AlterShardingTableRuleStatementTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.create.AddResourceStatementTestCase;
import org.apache.shardingsphere.distsql.parser.api.sql.jaxb.cases.domain.statement.rdl.drop.DropResourceStatementTestCase;
import org.apache.shardingsphere.distsql.parser.statement.rdl.RDLStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.alter.AlterRuleStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.alter.impl.AlterDatabaseDiscoveryRuleStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.alter.impl.AlterEncryptRuleStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.alter.impl.AlterReadwriteSplittingRuleStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.alter.impl.AlterShardingBindingTableRulesStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.alter.impl.AlterShardingBroadcastTableRulesStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.alter.impl.AlterShardingTableRuleStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.create.AddResourceStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.create.CreateRuleStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.drop.DropResourceStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.drop.DropRuleStatement;

/**
 * RDL statement assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RDLStatementAssert {

    /**
     * Assert SQL statement is correct with expected parser result.
     *
     * @param assertContext assert context
     * @param actual        actual RDL statement
     * @param expected      expected parser result
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final RDLStatement actual, final SQLParserTestCase expected) {
        if (actual instanceof AlterDatabaseDiscoveryRuleStatement) {
            AlterDatabaseDiscoveryRuleStatementAssert.assertIs(assertContext, (AlterDatabaseDiscoveryRuleStatement) actual, (AlterDataBaseDiscoveryRuleStatementTestCase) expected);
        } else if (actual instanceof AlterReadwriteSplittingRuleStatement) {
            AlterReadwriteSplittingRuleStatementAssert.assertIs(assertContext, (AlterReadwriteSplittingRuleStatement) actual, (AlterReadWriteSplittingRuleStatementTestCase) expected);
        } else if (actual instanceof AlterShardingBindingTableRulesStatement) {
            AlterShardingBindingTableRulesStatementAssert.assertIs(assertContext, (AlterShardingBindingTableRulesStatement) actual, (AlterShardingBindingTableRulesStatementTestCase) expected);
        } else if (actual instanceof AlterEncryptRuleStatement) {
            AlterEncryptRuleStatementAssert.assertIs(assertContext, (AlterEncryptRuleStatement) actual, (AlterEncryptRuleStatementTestCase) expected);
        } else if (actual instanceof AlterShardingBroadcastTableRulesStatement) {
            AlterShardingBroadcastTableRulesStatementAssert.assertIs(assertContext, (AlterShardingBroadcastTableRulesStatement) actual, (AlterShardingBroadcastTableRulesStatementTestCase) expected);
        } else if (actual instanceof AlterShardingTableRuleStatement) {
            AlterShardingTableRuleStatementAssert.assertIs(assertContext, (AlterShardingTableRuleStatement) actual, (AlterShardingTableRuleStatementTestCase) expected);
        } else if (actual instanceof CreateRuleStatement) {
            CreateRuleStatementAssert.assertIs(assertContext, (CreateRuleStatement) actual, expected);
        } else if (actual instanceof AddResourceStatement) {
            AddResourceStatementAssert.assertIs(assertContext, (AddResourceStatement) actual, (AddResourceStatementTestCase) expected);
        } else if (actual instanceof AlterRuleStatement) {
            AlterRuleStatementAssert.assertIs(assertContext, (AlterRuleStatement) actual, expected);
        } else if (actual instanceof DropResourceStatement) {
            DropResourceStatementAssert.assertIs(assertContext, (DropResourceStatement) actual, (DropResourceStatementTestCase) expected);
        } else if (actual instanceof DropRuleStatement) {
            DropRuleStatementAssert.assertIs(assertContext, (DropRuleStatement) actual, expected);
        }
    }
}
