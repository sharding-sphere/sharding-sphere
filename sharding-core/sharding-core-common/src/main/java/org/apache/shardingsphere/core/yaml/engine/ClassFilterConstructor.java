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

package org.apache.shardingsphere.core.yaml.engine;

import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.Collection;

/**
 * Class filter Constructor for YAML load as map.
 *
 * @author chenqingyang
 */
@RequiredArgsConstructor
public final class ClassFilterConstructor extends Constructor {
    
    private final Collection<Class<?>> acceptClasses;
    
    @Override
    protected Class<?> getClassForName(final String name) throws ClassNotFoundException {
        for (Class<? extends Object> clz : acceptClasses) {
            if (name.equals(clz.getName())) {
                return super.getClassForName(name);
            }
        }
        throw new IllegalArgumentException(String.format("Class is not accepted: %s", name));
    }
}
