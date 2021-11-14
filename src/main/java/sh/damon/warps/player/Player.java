package sh.damon.warps.player;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import sh.damon.warps.player.nbt.WarpData;

public class Player {
    private ServerPlayerEntity serverPlayerEntity;
    public WarpData warpData;

    public Player(ServerPlayerEntity serverPlayerEntity) {
        this.serverPlayerEntity = serverPlayerEntity;

        this.warpData = new WarpData(serverPlayerEntity);
    }

    public void updateServerPlayerEntity(ServerPlayerEntity serverPlayerEntity) {
        this.serverPlayerEntity = serverPlayerEntity;
    }

    public void loadNbtData(NbtCompound nbt) {
        this.warpData.load(nbt);
    }

    public void saveNbtData(NbtCompound nbt) {
        this.warpData.save(nbt);
    }
}