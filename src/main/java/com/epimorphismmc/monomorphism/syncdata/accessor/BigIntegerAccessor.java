package com.epimorphismmc.monomorphism.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;

import java.math.BigInteger;

public class BigIntegerAccessor extends CustomObjectAccessor<BigInteger> {
    protected BigIntegerAccessor() {
        super(BigInteger.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, BigInteger value) {
        return null;
    }

    @Override
    public BigInteger deserialize(AccessorOp op, ITypedPayload<?> payload) {
        return null;
    }
}
