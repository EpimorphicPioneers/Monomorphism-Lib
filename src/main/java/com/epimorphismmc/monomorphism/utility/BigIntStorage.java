package com.epimorphismmc.monomorphism.utility;

import com.epimorphismmc.monomorphism.math.BigIntMath;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public class BigIntStorage {
    @Setter
    private BigInteger storage;

    @Getter
    @Nullable private final BigInteger maxCapacity;

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

    public long add(long toAdd) {
        if (capacityInput == 0) return 0;

        long freeInput = capacityInput - bufferInput;
        if (freeInput <= toAdd) {
            var added = freeInput;
            this.bufferInput = capacityInput;
            toAdd -= added;

            flushBuffers();
            freeInput = capacityInput - bufferInput;
            if (toAdd > freeInput) {
                added += freeInput;
                this.bufferInput = capacityInput;
                return added;
            }
            this.bufferInput += toAdd;
            added += toAdd;
            return added;
        }
        this.bufferInput += toAdd;
        return toAdd;
    }

    public BigInteger add(BigInteger toAdd) {
        if (maxCapacity != null) {
            var freeStorage = maxCapacity.subtract(storage);
            if (freeStorage.compareTo(toAdd) < 0) {
                this.storage = maxCapacity;
                return freeStorage;
            }
        }

        this.storage = storage.add(toAdd);
        return toAdd;
    }

    public long remove(long toRemove) {
        if (bufferOutput < toRemove) {
            var removed = bufferOutput;
            this.bufferOutput = 0;
            toRemove -= removed;

            flushBuffers();
            if (toRemove > bufferOutput) {
                removed += bufferOutput;
                this.bufferOutput = 0;
                return removed;
            }
            this.bufferOutput -= toRemove;
            removed += toRemove;
            return removed;
        }
        this.bufferOutput -= toRemove;
        return toRemove;
    }

    public BigInteger remove(BigInteger toRemove) {
        return add(toRemove.negate()).negate();
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

        this.capacityInput = maxCapacity != null
                ? BigIntMath.getLongValue(maxCapacity.subtract(storage))
                : Long.MAX_VALUE;
    }
}
