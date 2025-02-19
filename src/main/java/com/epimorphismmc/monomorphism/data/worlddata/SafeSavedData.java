package com.epimorphismmc.monomorphism.data.worlddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.saveddata.SavedData;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * SavedData that is more resistant to crashes while writing. Thank you AE2 for the idea!
 *
 * @author GateGuardian
 * @date : 2024/8/20
 */
@ParametersAreNonnullByDefault
public abstract class SafeSavedData extends SavedData {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void save(File file) {
        if (!this.isDirty()) {
            return;
        }

        File tempFile = file.toPath().getParent().resolve(file.getName() + ".temp").toFile();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("data", this.save(new CompoundTag()));
        NbtUtils.addCurrentDataVersion(compoundTag);
        try {
            // Write to temp file first.
            NbtIo.writeCompressed(compoundTag, tempFile);
            // Delete old file.
            if (file.exists()) {
                if (!file.delete()) {
                    LOGGER.error("Could not delete old file {}", file);
                }
            }
            // Rename temp file to the correct name.
            if (!tempFile.renameTo(file)) {
                LOGGER.error("Could not rename file {} to {}", tempFile, file);
            }
        } catch (IOException iOException) {
            LOGGER.error("Could not save data {}", this, iOException);
        }
        this.setDirty(false);
    }
}
