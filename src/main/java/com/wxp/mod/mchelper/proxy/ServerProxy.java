package com.wxp.mod.mchelper.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author wxp
 * when you need regist a block/item or other thing in game will user this.
 * But do not do any operate that should do in ClientProxy, for example GUI,sound.
 */
public class ServerProxy {
  public void preInit(FMLPreInitializationEvent event) {
  }

  public void init(FMLInitializationEvent event) {
  }
}
