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

package org.apache.shardingsphere.test.integration.engine.param.domain;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.test.integration.cases.IntegrateTestCaseContext;
import org.apache.shardingsphere.test.integration.cases.assertion.IntegrateTestCaseAssertion;
import org.apache.shardingsphere.test.integration.engine.param.SQLExecuteType;

/**
 * Parameterized array of assertion based integrate test.
 */
@RequiredArgsConstructor
public final class AssertionParameterizedArray implements ParameterizedArray {
    
    private final IntegrateTestCaseContext testCaseContext;
    
    private final IntegrateTestCaseAssertion assertion;
    
    private final String scenario;
    
    private final DatabaseType databaseType;
    
    private final SQLExecuteType sqlExecuteType;
    
    @Override
    public Object[] toArrays() {
        Object[] result = new Object[6];
        result[0] = testCaseContext.getParentPath();
        result[1] = assertion;
        result[2] = scenario;
        result[3] = databaseType.getName();
        result[4] = sqlExecuteType;
        result[5] = testCaseContext.getTestCase().getSql();
        return result;
    }
}
