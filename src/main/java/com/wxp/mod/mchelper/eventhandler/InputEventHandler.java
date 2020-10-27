package com.wxp.mod.mchelper.eventhandler;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.gui.ModHelperGuiHandler;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import com.wxp.mod.mchelper.manager.InputKeyManager;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.network.OpenGuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

/** @author wxp 输入相关的事件。主要用于打开location的界面 */
@Mod.EventBusSubscriber(modid = ModConfig.MOD_ID)
public class InputEventHandler {
  @SubscribeEvent
  public static void onKeyInput(InputEvent.KeyInputEvent event) {
    if (Side.SERVER.equals(FMLCommonHandler.instance().getSide())) {
      return;
    }
    if (InputKeyManager.KEY_GUI_LOCATION.isPressed()) {
      EntityPlayer player = Minecraft.getMinecraft().player;
      if (player.hasCapability(CapabilityManager.locationCapability, null)) {
        LocationCapability locationCapability =
            player.getCapability(CapabilityManager.locationCapability, null);
        if (locationCapability == null) {
          return;
        }
        OpenGuiMessage openGuiMessage = new OpenGuiMessage(ModHelperGuiHandler.ID_LOCATION_UI);
        NetworkManager.sendToServer(openGuiMessage);
      }
    }
  }
}
