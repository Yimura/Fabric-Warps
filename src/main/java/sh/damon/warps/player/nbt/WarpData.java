package sh.damon.warps.player.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpData implements NbtContainer {
    private final String NBT_TAG = "WARP_DATA";
    private ServerPlayerEntity serverPlayerEntity;

    private HashMap<String, WarpLocation> warps = new HashMap<>();

    public Set<String> getWarpList() {
        return this.warps.keySet();
    }

    public boolean deleteWarp(String name) {
        return this.warps.remove(name) != null;
    }

    public boolean gotoWarp(String name) {
        WarpLocation warp = this.warps.getOrDefault(name, null);
        if (warp == null) return false;

        warp.teleport(this.serverPlayerEntity);

        return true;
    }

    public void setServerPlayerEntity(ServerPlayerEntity serverPlayerEntity) {
        this.serverPlayerEntity = serverPlayerEntity;
    }

    /**
     * Creates a warp at the current position in the world of the player, overwrites an existing warp.
     * @param name
     * @return True if a new entry was created, false if one was overwritten
     */
    public WarpLocation setWarp(String name) {
        return this.warps.put(name, new WarpLocation(this.serverPlayerEntity));
    }

    @Override
    public void load(NbtCompound nbt) {
        if (!nbt.contains(NBT_TAG)) return;

        NbtCompound warpNbt = nbt.getCompound(NBT_TAG);

        for (String key : warpNbt.getKeys()) {
            NbtCompound warpLocation = warpNbt.getCompound(key);

            this.warps.put(key, new WarpLocation(this.serverPlayerEntity, warpLocation));
        }
    }

    @Override
    public void save(NbtCompound nbt) {
        NbtCompound warpNbt = new NbtCompound();

        for (Map.Entry<String, WarpLocation> entry : this.warps.entrySet()) {
            warpNbt.put(entry.getKey(), entry.getValue().toNbtData());
        }

        nbt.put(NBT_TAG, warpNbt);
    }

    private static class WarpLocation {
        private final Vec3d position;
        private final ServerWorld world;
        private final float pitch;
        private final float yaw;

        public WarpLocation(ServerPlayerEntity serverPlayerEntity) {
            this.world = serverPlayerEntity.getServerWorld();

            this.position = serverPlayerEntity.getPos();

            this.pitch = serverPlayerEntity.getPitch();
            this.yaw = serverPlayerEntity.getYaw();
        }

        public WarpLocation(ServerPlayerEntity serverPlayerEntity, NbtCompound nbt) {
            final MinecraftServer server = serverPlayerEntity.getServer();

            assert server != null;
            this.world = server.getWorld(
                RegistryKey.of(Registry.WORLD_KEY, new Identifier(nbt.getString("world")))
            );
            assert world != null;

            final NbtCompound position = nbt.getCompound("position");
            this.position = new Vec3d(position.getDouble("x"), position.getDouble("y"), position.getDouble("z"));

            final NbtCompound camera = nbt.getCompound("camera");
            this.pitch = camera.getFloat("pitch");
            this.yaw = camera.getFloat("yaw");
        }

        public void teleport(ServerPlayerEntity serverPlayerEntity) {
            ChunkPos chunkPos = new ChunkPos(new BlockPos(this.position.x, this.position.y, this.position.z));

            this.world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, serverPlayerEntity.getId());
            serverPlayerEntity.stopRiding();

            if (serverPlayerEntity.getServerWorld() == this.world)
                serverPlayerEntity.networkHandler.requestTeleport(position.x, position.y, position.z, this.yaw, this.pitch);
            else
                serverPlayerEntity.teleport(this.world, position.x, position.y, position.z, this.yaw, this.pitch);
        }

        public NbtCompound toNbtData() {
            NbtCompound camera = new NbtCompound();
            camera.putFloat("pitch", this.pitch);
            camera.putFloat("yaw", this.yaw);

            NbtCompound position = new NbtCompound();
            position.putDouble("x", this.position.x);
            position.putDouble("y", this.position.y);
            position.putDouble("z", this.position.z);

            NbtCompound nbt = new NbtCompound();
            nbt.put("camera", camera);
            nbt.put("position", position);
            nbt.putString("world", this.world.getRegistryKey().getValue().toString());

            return nbt;
        }
    }
}
