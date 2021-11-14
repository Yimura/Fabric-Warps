package sh.damon.warps.player;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerManager {
    private final HashMap<UUID, Player> players;

    public PlayerManager() {
        this.players = new HashMap<>();
    }

    public Player get(ServerPlayerEntity serverPlayerEntity) {
        Player player;

        if (this.players.containsKey(serverPlayerEntity.getUuid())) {
            player = this.players.get(serverPlayerEntity.getUuid());

            player.updateServerPlayerEntity(serverPlayerEntity);
        }
        else {
            player = new Player(serverPlayerEntity);

            this.players.put(serverPlayerEntity.getUuid(), player);
        }

        return player;
    }
}