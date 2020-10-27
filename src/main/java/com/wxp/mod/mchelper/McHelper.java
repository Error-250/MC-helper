package com.wxp.mod.mchelper;

import com.wxp.mod.mchelper.command.LocationCommand;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import com.wxp.mod.mchelper.manager.InputKeyManager;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.proxy.ServerProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

/** @author wxp */
@Mod(modid = ModConfig.MOD_ID, name = ModConfig.NAME, version = ModConfig.VERSION)
public class McHelper {
  private static Logger logger;

  @Mod.Instance(ModConfig.MOD_ID)
  public static McHelper INSTANCE;

  @SidedProxy(
      clientSide = "com.wxp.mod.mchelper.proxy.ClientProxy",
      serverSide = "com.wxp.mod.mchelper.proxy.ServerProxy",
      modId = ModConfig.MOD_ID)
  public static ServerProxy modProxy;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    modProxy.preInit(event);
    CapabilityManager.initCapability();
    NetworkManager.initNetwork();
    InputKeyManager.initKey();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    modProxy.init(event);
  }

  @Mod.EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new LocationCommand());
  }

  public static Logger getLogger() {
    return logger;
  }
}
