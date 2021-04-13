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

package org.apache.shardingsphere.db.protocol.postgresql.packet.generic;

import io.netty.buffer.ByteBuf;
import org.apache.shardingsphere.db.protocol.postgresql.packet.ByteBufTestUtils;
import org.apache.shardingsphere.db.protocol.postgresql.packet.PostgreSQLIdentifierTagType;
import org.apache.shardingsphere.db.protocol.postgresql.packet.handshake.PostgreSQLAuthenticationMD5PasswordPacket;
import org.apache.shardingsphere.db.protocol.postgresql.payload.PostgreSQLPacketPayload;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class PostgreSQLAuthenticationMD5PasswordPacketTest {
    
    @Test
    public void assertReadWrite() {
        byte[] md5Salt = "salt".getBytes();
        int expectedLength = 4 + md5Salt.length;
        ByteBuf byteBuf = ByteBufTestUtils.createByteBuf(expectedLength);
        PostgreSQLPacketPayload payload = new PostgreSQLPacketPayload(byteBuf);
        PostgreSQLAuthenticationMD5PasswordPacket packet = new PostgreSQLAuthenticationMD5PasswordPacket(md5Salt);
        assertThat(packet.getIdentifier(), is(PostgreSQLIdentifierTagType.AUTHENTICATION_REQUEST.getValue()));
        packet.write(payload);
        assertThat(byteBuf.writerIndex(), is(expectedLength));
        assertThat(byteBuf.readInt(), is(5));
        byte[] actualMd5Salt = new byte[4];
        byteBuf.readBytes(actualMd5Salt);
        assertThat(actualMd5Salt, is(md5Salt));
    }
}
