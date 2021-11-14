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
public class CreateWarp implements BaseCommand {
    /**
     * This method will be called to tell the server how your command works,
     * for more information on this you can read here https://fabricmc.net/wiki/tutorial:commands
     */
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("warp")
                .then(literal("create")
                    .then(
                        argument("name", StringArgumentType.word()
                    ).executes(this)
                )
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

        final String warpName = StringArgumentType.getString(context, "name");
        source.sendFeedback(new LiteralText(player.warpData.setWarp(warpName) == null ? String.format("Created new warp called '%s'", warpName) : String.format("Old warp called '%s' has been overwritten.", warpName)), false);

        return Command.SINGLE_SUCCESS;
    }
}