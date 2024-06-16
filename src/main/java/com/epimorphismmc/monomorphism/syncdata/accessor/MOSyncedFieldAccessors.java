package com.epimorphismmc.monomorphism.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.payload.StringPayload;

public class MOSyncedFieldAccessors {

    public static final IAccessor BIG_INTEGER_ACCESSOR = new BigIntegerAccessor();

    public static void init() {
        TypedPayloadRegistries.register(
                StringPayload.class, StringPayload::new, BIG_INTEGER_ACCESSOR, -1);
    }
}
