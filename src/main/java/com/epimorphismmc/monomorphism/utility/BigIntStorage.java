package com.epimorphismmc.monomorphism.utility;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public class BigIntStorage {
    @Setter
    private BigInteger storage;
    @Getter @Nullable
    private final BigInteger maxCapacity;
    private long bufferInput;
    private long bufferOutput;
    private long capacityInput;

    public BigIntStorage() {
        this(null);
    }

    public BigIntStorage(@Nullable BigInteger maxCapacity) {
        this(BigInteger.ZERO, maxCapacity);

    }

    public BigIntStorage(BigInteger storage, @Nullable BigInteger maxCapacity) {
        this.storage = storage;
        this.maxCapacity = maxCapacity;
        flushBuffers();
    }

    public long add(long value) {
        if (capacityInput == 0) return 0;

        long free = capacityInput - bufferInput;
        if (free <= value) {
            this.bufferInput = capacityInput;
            flushBuffers();
            return free;
        }
        this.bufferInput += value;
        return value;
    }

    public BigInteger add(BigInteger value) {
        if (maxCapacity != null) {
            var free = maxCapacity.subtract(storage);
            if (free.compareTo(value) < 0) {
                this.storage = maxCapacity;
                return free;
            }
        }

        this.storage = storage.add(value);
        return value;
    }

    public long remove(long value) {
        if (bufferOutput < value) {
            var output = bufferOutput;
            this.bufferOutput = 0;
            value -= output;

            flushBuffers();
            if (value > bufferOutput) {
                output += bufferOutput;
                this.bufferOutput = 0;
                return output;
            }
            this.bufferOutput -= value;
            output += value;
            return output;
        }
        this.bufferOutput -= value;
        return value;
    }

    public BigInteger remove(BigInteger value) {
        return add(value.negate()).negate();
    }

    public BigInteger getStorage() {
        return storage.add(BigInteger.valueOf(bufferOutput + bufferInput));
    }

    private void flushBuffers() {
        long capacityOutput = Long.MAX_VALUE;
        long freeOutput = capacityOutput - bufferOutput;
        if (freeOutput < bufferInput) {
            this.bufferOutput = capacityOutput;
            this.bufferOutput -= bufferInput;
            this.bufferInput -= add(BigInteger.valueOf(bufferInput - freeOutput)).longValue();
        } else {
            this.bufferOutput = bufferInput;
            this.bufferInput = 0;
        }

        freeOutput = capacityOutput - bufferOutput;
        if (freeOutput > 0 && storage.compareTo(BigInteger.ZERO) > 0) {
            var newStorage = storage.subtract(BigInteger.valueOf(freeOutput));
            if (newStorage.compareTo(BigInteger.ZERO) < 0) {
                this.bufferOutput += storage.longValue();
                this.storage = BigInteger.ZERO;
            } else {
                this.bufferOutput += freeOutput;
                this.storage = newStorage;
            }
        }

        this.capacityInput = maxCapacity != null ? MOMathUtils.getLongNumber(maxCapacity.subtract(storage)) : Long.MAX_VALUE;
    }
}
