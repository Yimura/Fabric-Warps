package sh.damon.warps.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.damon.warps.Warps;
import sh.damon.warps.player.Player;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    public void onReadNbtData(NbtCompound nbt, CallbackInfo ci) {
        final ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) (Object) this;

        final Warps instance = Warps.getInstance();
        final Player player = instance.playerManager.get(serverPlayerEntity);

        player.loadNbtData(nbt);
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    public void onWriteNbtData(NbtCompound nbt, CallbackInfo ci) {
        final ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) (Object) this;

        final Warps instance = Warps.getInstance();
        final Player player = instance.playerManager.get(serverPlayerEntity);

        player.saveNbtData(nbt);
    }
}