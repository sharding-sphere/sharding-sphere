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

package org.apache.shardingsphere.test.integration.engine.junit;

import lombok.SneakyThrows;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.test.integration.cases.SQLCommandType;
import org.apache.shardingsphere.test.integration.engine.junit.impl.ITRunnerCaseBasedParallelExecutor;
import org.apache.shardingsphere.test.integration.engine.junit.impl.ITRunnerScenarioBasedParallelExecutor;
import org.apache.shardingsphere.test.integration.engine.param.model.ParameterizedArray;
import org.apache.shardingsphere.test.integration.env.IntegrationTestEnvironment;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a strategy for scheduling when individual test methods should be run (in serial or parallel).
 * 
 * <p>
 * WARNING: still experimental, may go away.
 * </p>
 */
public final class ITRunnerScheduler implements RunnerScheduler {
    
    private final Field parametersField;
    
    private final Map<String, ITRunnerParallelExecutor> runnerExecutors;
    
    private volatile Field runnerField;
    
    public ITRunnerScheduler() {
        parametersField = getParametersField();
        runnerExecutors = getITRunnerExecutors();
    }
    
    @SneakyThrows(NoSuchFieldException.class)
    private Field getParametersField() {
        Field result = BlockJUnit4ClassRunnerWithParameters.class.getDeclaredField("parameters");
        result.setAccessible(true);
        return result;
    }
    
    private Map<String, ITRunnerParallelExecutor> getITRunnerExecutors() {
        Map<String, ITRunnerParallelExecutor> result = new HashMap<>(IntegrationTestEnvironment.getInstance().getDataSourceEnvironments().size() * 3, 1);
        for (DatabaseType each : IntegrationTestEnvironment.getInstance().getDataSourceEnvironments().keySet()) {
            result.put(getITRunnerExecutorKey(each.getName(), SQLCommandType.DQL.name()), new ITRunnerCaseBasedParallelExecutor());
            result.put(getITRunnerExecutorKey(each.getName(), ""), new ITRunnerScenarioBasedParallelExecutor());
        }
        return result;
    }
    
    private String getITRunnerExecutorKey(final String databaseType, final String sqlCommandType) {
        return String.join("_", databaseType, sqlCommandType);
    }
    
    @Override
    public void schedule(final Runnable childStatement) {
        Object[] parameters = getITParameters(childStatement);
        ParameterizedArray parameterizedArray = (ParameterizedArray) parameters[0];
        getITRunnerExecutor(parameterizedArray).execute(parameterizedArray, childStatement);
    }
    
    @SneakyThrows(ReflectiveOperationException.class)
    private Object[] getITParameters(final Runnable childStatement) {
        if (null == runnerField) {
            runnerField = childStatement.getClass().getDeclaredField("val$each");
            runnerField.setAccessible(true);
        }
        return (Object[]) parametersField.get(runnerField.get(childStatement));
    }
    
    private ITRunnerParallelExecutor getITRunnerExecutor(final ParameterizedArray parameterizedArray) {
        switch (parameterizedArray.getSqlCommandType()) {
            case DQL:
                return runnerExecutors.get(getITRunnerExecutorKey(parameterizedArray.getDatabaseType().getName(), SQLCommandType.DQL.name()));
            default:
                return runnerExecutors.get(getITRunnerExecutorKey(parameterizedArray.getDatabaseType().getName(), ""));
        }
    }
    
    @Override
    public void finished() {
        if (null != runnerExecutors) {
            runnerExecutors.values().forEach(ITRunnerParallelExecutor::finished);
        }
    }
}
