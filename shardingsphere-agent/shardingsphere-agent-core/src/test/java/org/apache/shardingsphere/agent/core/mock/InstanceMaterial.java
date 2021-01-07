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

package org.apache.shardingsphere.agent.core.mock;

import java.io.IOException;
import java.util.Deque;

/**
 * Have to redefine this class dynamic, so never add `final` modifier.
 */
public class InstanceMaterial {
    
    /**
     * Mock method for testing.
     *
     * @param queue collector
     * @return result
     */
    public String mock(final Deque<String> queue) {
        queue.add("on");
        return "invocation";
    }
    
    /**
     * Mock method for testing with exception.
     *
     * @param queue collector
     * @return result
     * @throws IOException io exception
     */
    public String mockWithException(final Deque<String> queue) throws IOException {
        throw new IOException();
    }
    
}
