package com.epimorphismmc.monomorphism.syncdata.accessor;

import com.epimorphismmc.monomorphism.syncdata.payload.ByteArrayPayload;
import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;

import java.math.BigInteger;

public class BigIntegerAccessor extends CustomObjectAccessor<BigInteger> {
    public BigIntegerAccessor() {
        super(BigInteger.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, BigInteger value) {
        return ByteArrayPayload.of(value.toByteArray());
    }

    @Override
    public BigInteger deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof ByteArrayPayload byteArrayPayload) {
            return new BigInteger(byteArrayPayload.getPayload());
        }
        return null;
    }
}
