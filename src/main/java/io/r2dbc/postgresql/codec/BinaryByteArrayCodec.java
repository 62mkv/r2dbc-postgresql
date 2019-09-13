/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.r2dbc.postgresql.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.r2dbc.postgresql.client.Parameter;
import io.r2dbc.postgresql.message.Format;
import io.r2dbc.postgresql.type.PostgresqlObjectId;
import io.r2dbc.postgresql.util.Assert;
import reactor.core.publisher.Mono;

import static io.r2dbc.postgresql.message.Format.FORMAT_TEXT;
import static io.r2dbc.postgresql.type.PostgresqlObjectId.BYTEA;

final class BinaryByteArrayCodec extends AbstractBinaryCodec<byte[]> {

    BinaryByteArrayCodec(ByteBufAllocator byteBufAllocator) {
        super(byte[].class, byteBufAllocator);
    }

    @Override
    byte[] doDecode(ByteBuf buffer, PostgresqlObjectId dataType, Format format, Class<? extends byte[]> type) {
        return decode(format, buffer);
    }

    @Override
    Parameter doEncode(byte[] value) {
        Assert.requireNonNull(value, "value must not be null");

        return create(BYTEA, FORMAT_TEXT,
            Mono.fromSupplier(() -> toHexFormat(Unpooled.wrappedBuffer(value)))
        );
    }

}
