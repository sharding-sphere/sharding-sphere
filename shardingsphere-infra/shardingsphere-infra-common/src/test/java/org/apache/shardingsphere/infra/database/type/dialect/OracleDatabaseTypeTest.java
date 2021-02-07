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

package org.apache.shardingsphere.infra.database.type.dialect;

import org.apache.shardingsphere.infra.database.metadata.dialect.OracleDataSourceMetaData;
import org.apache.shardingsphere.sql.parser.sql.common.constant.QuoteCharacter;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public final class OracleDatabaseTypeTest extends AbstractDatabaseTypeTest {
    
    @Test
    public void assertGetName() {
        assertThat(new OracleDatabaseType().getName(), is("Oracle"));
    }
    
    @Test
    public void assertGetJdbcUrlPrefixes() {
        assertThat(new OracleDatabaseType().getJdbcUrlPrefixes(), is(Collections.singleton("jdbc:oracle:")));
    }
    
    @Test
    public void assertOracleDataSourceMetaData() {
        assertThat(new OracleDatabaseType().getDataSourceMetaData("jdbc:oracle:oci:@127.0.0.1/ds_0", "scott"), instanceOf(OracleDataSourceMetaData.class));
    }
    
    @Test
    public void assertGetSchema() throws SQLException {
        when(getConnection().getSchema()).thenReturn(USER_NAME);
        when(getConnection().getMetaData()).thenReturn(getDatabaseMetaData());
        String oracleSchema = getSchema(new OracleDatabaseType());
        assertThat(oracleSchema, is(USER_NAME));
    }
    
    @Test
    public void assertFormatTableNamePattern() {
        String oracleTableNamePattern = formatTableNamePattern(new OracleDatabaseType());
        assertThat(oracleTableNamePattern, is(TABLE_NAME_PATTERN.toUpperCase()));
    }
    
    @Test
    public void assertGetQuoteCharacter() {
        QuoteCharacter actual = new OracleDatabaseType().getQuoteCharacter();
        assertThat(actual.getStartDelimiter(), is("\""));
        assertThat(actual.getEndDelimiter(), is("\""));
    }
}
