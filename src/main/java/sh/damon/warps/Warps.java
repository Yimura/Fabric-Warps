package sh.damon.warps;

import net.fabricmc.api.ModInitializer;
import sh.damon.warps.command.CommandManager;
import sh.damon.warps.player.PlayerManager;
import sh.damon.warps.util.Log;

public class Warps implements ModInitializer {
	public static final String MOD_ID = "warp";
	public static final String MOD_NAME = "Warps";

	public final CommandManager commandManager = new CommandManager();
	public final PlayerManager playerManager = new PlayerManager();

	public static Log log = new Log(MOD_NAME);

	private static Warps instance;

	@Override
	public void onInitialize() {
		Warps.instance = this;

		this.commandManager.registerAll();

		Warps.log.info( "Mod is ready.");
	}

	public static Warps getInstance() {
		return instance;
	}
}
