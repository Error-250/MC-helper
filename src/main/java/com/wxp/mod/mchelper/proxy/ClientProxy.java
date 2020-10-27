package com.wxp.mod.mchelper.proxy;

import com.wxp.mod.mchelper.register.InputKeyRegister;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/** @author wxp */
public class ClientProxy extends ServerProxy {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
  }

  @Override
  public void init(FMLInitializationEvent event) {
    InputKeyRegister.registerKey();
  }
}
