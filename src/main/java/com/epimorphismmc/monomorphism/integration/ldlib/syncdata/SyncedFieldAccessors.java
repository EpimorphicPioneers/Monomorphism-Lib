package com.epimorphismmc.monomorphism.integration.ldlib.syncdata;

import com.epimorphismmc.monomorphism.integration.ldlib.syncdata.accessor.BigIntegerAccessor;
import com.epimorphismmc.monomorphism.integration.ldlib.syncdata.payload.ByteArrayPayload;

import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;

public class SyncedFieldAccessors {

    public static final IAccessor BIG_INTEGER_ACCESSOR = new BigIntegerAccessor();

    public static void init() {
        TypedPayloadRegistries.register(
                ByteArrayPayload.class, ByteArrayPayload::new, BIG_INTEGER_ACCESSOR, -1);
    }
}
