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

package org.apache.shardingsphere.db.protocol.postgresql.packet.handshake;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.db.protocol.postgresql.packet.PostgreSQLIdentifierPacket;
import org.apache.shardingsphere.db.protocol.postgresql.packet.PostgreSQLIdentifierTagType;
import org.apache.shardingsphere.db.protocol.postgresql.payload.PostgreSQLPacketPayload;

/**
 * AuthenticationMD5Password (backend) packet for PostgreSQL.
 */
@RequiredArgsConstructor
public final class PostgreSQLAuthenticationMD5PasswordPacket implements PostgreSQLIdentifierPacket {
    
    private static final int AUTH_REQ_MD5 = 5;
    
    private final byte[] md5Salt;
    
    @Override
    public void write(final PostgreSQLPacketPayload payload) {
        payload.writeInt4(AUTH_REQ_MD5);
        payload.writeBytes(md5Salt);
    }
    
    @Override
    public char getIdentifier() {
        return PostgreSQLIdentifierTagType.AUTHENTICATION_REQUEST.getValue();
    }
}
