package sh.damon.warps.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import sh.damon.warps.Warps;
import sh.damon.warps.command.BaseCommand;
import sh.damon.warps.player.Player;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * This is an example command it implements BaseCommand.
 * Every command needs the below to methods to be present to work.
 */
public class WarpList implements BaseCommand {
    /**
     * This method will be called to tell the server how your command works,
     * for more information on this you can read here https://fabricmc.net/wiki/tutorial:commands
     */
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("warp")
                .then(literal("list").executes(this)
            )
        );
    }

    /**
     * This method has the actual code that needs to be ran when the command is triggered by someone
     */
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity serverPlayerEntity = source.getPlayer();

        final Warps instance = Warps.getInstance();
        Player player = instance.playerManager.get(serverPlayerEntity);

        StringBuilder warpList = new StringBuilder();
        for (String warpName : player.warpData.getWarpList()) {
            if (warpList.length() != 0) warpList.append(", ");
            warpList.append(warpName);
        }
        if (warpList.length() == 0) warpList.append("None");

        source.sendFeedback(new LiteralText(String.format("Your warp list: %s", warpList)), false);

        return Command.SINGLE_SUCCESS;
    }
}
