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

package org.apache.shardingsphere.sql.parser.sql.segment.dml.column;

import org.apache.shardingsphere.sql.parser.core.constant.QuoteCharacter;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.value.identifier.IdentifierValue;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ColumnSegmentTest {
    
    @Test
    public void assertGetQualifiedNameWithoutOwner() {
        ColumnSegment actual = new ColumnSegment(0, 0, "col", QuoteCharacter.NONE);
        assertThat(actual.getQualifiedName(), is("col"));
    }
    
    @Test
    public void assertGetQualifiedNameWithOwner() {
        ColumnSegment actual = new ColumnSegment(0, 0, "col", QuoteCharacter.NONE);
        actual.setOwner(new TableSegment(0, 0, new IdentifierValue("tbl")));
        assertThat(actual.getQualifiedName(), is("tbl.col"));
    }
}
