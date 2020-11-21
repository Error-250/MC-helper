package com.wxp.mod.mchelper.eventhandler;

import com.wxp.mod.mchelper.config.ModConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author wxp
 */
@Mod.EventBusSubscriber(modid = ModConfig.MOD_ID)
public class ConfigSyncHandler {
  @SubscribeEvent
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(ModConfig.MOD_ID)) {
      ConfigManager.sync(ModConfig.MOD_ID, Config.Type.INSTANCE);
    }
  }
}
