package atonkish.reinfchest;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import atonkish.reinfchest.stat.ModStats;

public class ReinforcedChestsMod implements ModInitializer {
	public static final String MOD_ID = "reinfchest";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Stats
		ModStats.init();
	}
}
