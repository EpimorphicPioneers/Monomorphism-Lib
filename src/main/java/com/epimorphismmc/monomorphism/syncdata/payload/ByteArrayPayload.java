package com.epimorphismmc.monomorphism.syncdata.payload;

import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
public class ByteArrayPayload extends ObjectTypedPayload<byte[]> {

    public static ByteArrayPayload of(byte[] value) {
        ByteArrayPayload payload = new ByteArrayPayload();
        payload.setPayload(value);
        return payload;
    }

    public void writePayload(FriendlyByteBuf buf) {
        buf.writeByteArray(payload);
    }

    public void readPayload(FriendlyByteBuf buf) {
        this.payload = buf.readByteArray();
    }

    @Nullable @Override
    public Tag serializeNBT() {
        return new ByteArrayTag(payload);
    }

    @Override
    public void deserializeNBT(Tag tag) {
        this.payload = ((ByteArrayTag) tag).getAsByteArray();
    }
}
