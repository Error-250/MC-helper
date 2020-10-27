package com.wxp.mod.mchelper.manager;

import com.wxp.mod.mchelper.config.ModConfig;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author wxp */
public class InputKeyManager {
  private static List<KeyBinding> registeredKeyBind;
  public static KeyBinding KEY_GUI_LOCATION;

  public static void initKey() {
    registeredKeyBind = new ArrayList<>();
    KEY_GUI_LOCATION =
        new KeyBinding(
            "key.gui.location",
            Keyboard.KEY_PERIOD,
            String.format("key.category.%s", ModConfig.MOD_ID));
    registeredKeyBind.add(KEY_GUI_LOCATION);
  }

  public static Collection<KeyBinding> getInitializedKey() {
    return registeredKeyBind;
  }
}
