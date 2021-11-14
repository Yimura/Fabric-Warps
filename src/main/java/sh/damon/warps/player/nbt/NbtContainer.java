package sh.damon.warps.player.nbt;

import net.minecraft.nbt.NbtCompound;

public interface NbtContainer {
    /**
     * Called when Nbt data can be read from the entity
     * @param nbt
     */
    void load(NbtCompound nbt);
    /**
     * Called when it's time to save our data to Nbt
     * @param nbt
     */
    void save(NbtCompound nbt);
}