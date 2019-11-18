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

package org.apache.shardingsphere.core.metadata.datasource.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shardingsphere.core.metadata.datasource.exception.UnrecognizedDatabaseURLException;
import org.apache.shardingsphere.spi.database.DataSourceInfo;
import org.apache.shardingsphere.spi.database.DataSourceMetaData;

import com.google.common.base.Strings;

import lombok.Getter;

/**
 * Data source meta data for SQLServer.
 *
 * @author panjuan
 */
@Getter
public final class SQLServerDataSourceMetaData implements DataSourceMetaData {
    
    private static final int DEFAULT_PORT = 1433;
    
    private final String hostName;
    
    private final int port;
    
    private final String schemaName;
    
    private final String catalog;
    
    private final Pattern pattern = Pattern.compile("jdbc:(microsoft:)?sqlserver://([\\w\\-\\.]+):?([0-9]*);\\S*(DatabaseName|database)=([\\w\\-]+);?", Pattern.CASE_INSENSITIVE);

    public SQLServerDataSourceMetaData(final DataSourceInfo dataSourceInfo) {
        String url = dataSourceInfo.getUrl();

        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new UnrecognizedDatabaseURLException(url, pattern.pattern());
        }
        hostName = matcher.group(2);
        port = Strings.isNullOrEmpty(matcher.group(3)) ? DEFAULT_PORT : Integer.valueOf(matcher.group(3));
        catalog = matcher.group(5);
        schemaName = null;
    }
}
