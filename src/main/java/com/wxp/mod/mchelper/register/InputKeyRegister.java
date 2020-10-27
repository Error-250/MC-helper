package com.wxp.mod.mchelper.register;

import com.wxp.mod.mchelper.manager.InputKeyManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/** @author wxp 系统按键注册 */
public class InputKeyRegister {
  public static void registerKey() {
    for (KeyBinding keyBinding : InputKeyManager.getInitializedKey()) {
      ClientRegistry.registerKeyBinding(keyBinding);
    }
  }
}
