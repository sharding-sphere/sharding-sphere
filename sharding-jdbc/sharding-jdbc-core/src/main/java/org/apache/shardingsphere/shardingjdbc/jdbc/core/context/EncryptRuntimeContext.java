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

package org.apache.shardingsphere.shardingjdbc.jdbc.core.context;

import org.apache.shardingsphere.encrypt.metadata.EncryptTableMetaDataDecorator;
import org.apache.shardingsphere.encrypt.metadata.EncryptTableMetaDataLoader;
import org.apache.shardingsphere.encrypt.rule.EncryptRule;
import org.apache.shardingsphere.sql.parser.binder.metadata.schema.SchemaMetaData;
import org.apache.shardingsphere.underlying.common.database.type.DatabaseType;
import org.apache.shardingsphere.underlying.common.metadata.schema.decorator.SchemaMetaDataDecorator;
import org.apache.shardingsphere.underlying.common.metadata.schema.loader.RuleSchemaMetaDataLoader;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Runtime context for encrypt.
 */
public final class EncryptRuntimeContext extends SingleDataSourceRuntimeContext<EncryptRule> {
    
    public EncryptRuntimeContext(final DataSource dataSource, final EncryptRule encryptRule, final Properties props, final DatabaseType databaseType) throws SQLException {
        super(dataSource, encryptRule, props, databaseType);
    }
    
    @Override
    protected SchemaMetaData loadSchemaMetaData(final DataSource dataSource) throws SQLException {
        RuleSchemaMetaDataLoader loader = new RuleSchemaMetaDataLoader();
        loader.registerLoader(getRule(), new EncryptTableMetaDataLoader());
        SchemaMetaData schemaMetaData = loader.load(getDatabaseType(), dataSource, getProperties());
        return SchemaMetaDataDecorator.decorate(schemaMetaData, getRule(), new EncryptTableMetaDataDecorator());
    }
}
