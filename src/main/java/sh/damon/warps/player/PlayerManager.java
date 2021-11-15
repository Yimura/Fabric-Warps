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
        final UUID id = serverPlayerEntity.getUuid();
        Player player;

        if (this.players.containsKey(id)) {
            player = this.players.get(id);

            player.updateServerPlayerEntity(serverPlayerEntity);
        }
        else {
            player = new Player(serverPlayerEntity);

            this.players.put(id, player);
        }

        return player;
    }
}