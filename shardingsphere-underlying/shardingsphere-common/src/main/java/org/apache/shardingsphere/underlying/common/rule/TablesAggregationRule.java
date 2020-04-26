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

package org.apache.shardingsphere.underlying.common.rule;

import java.util.Collection;
import java.util.Optional;

/**
 * Tables aggregation rule.
 */
public interface TablesAggregationRule extends BaseRule {
    
    /**
     * Get all actual tables.
     * 
     * @return all actual tables
     */
    Collection<String> getAllActualTables();
    
    /**
     * Is need accumulate.
     * 
     * @param tables table names
     * @return need accumulate
     */
    boolean isNeedAccumulate(Collection<String> tables);
    
    /**
     * Find logic table name via actual table name.
     *
     * @param actualTable actual table name
     * @return logic table name
     */
    Optional<String> findLogicTableByActualTable(String actualTable);
}
