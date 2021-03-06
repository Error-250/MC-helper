package com.wxp.mod.mchelper.manager;

import com.wxp.mod.mchelper.McHelper;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.gui.ModHelperGuiHandler;
import com.wxp.mod.mchelper.network.*;
import com.wxp.mod.mchelper.network.hanlder.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/** @author wxp 网络管理 */
public class NetworkManager {
  private static SimpleNetworkWrapper simpleNetworkWrapper =
      NetworkRegistry.INSTANCE.newSimpleChannel(ModConfig.MOD_ID);

  private static IGuiHandler guiHandler = new ModHelperGuiHandler();

  public static void initNetwork() {
    NetworkRegistry.INSTANCE.registerGuiHandler(McHelper.INSTANCE, guiHandler);

    simpleNetworkWrapper.registerMessage(
        LocationCapabilitySyncMessageHandler.class,
        LocationCapabilitySyncMessage.class,
        0,
        Side.CLIENT);
    simpleNetworkWrapper.registerMessage(
        OpenGuiMessageHandler.class, OpenGuiMessage.class, 1, Side.SERVER);
    simpleNetworkWrapper.registerMessage(
        LocationUpdateMessageHandler.class, LocationUpdateMessage.class, 2, Side.SERVER);
    simpleNetworkWrapper.registerMessage(
        BlockStateSwitchMessageHandler.class, BlockStateSwitchMessage.class, 3, Side.SERVER);
    simpleNetworkWrapper.registerMessage(
        EnchantmentMessageHandler.class, EnchantmentMessage.class, 4, Side.SERVER);
  }

  public static void sendTo(AbstractNbtMessage message, EntityPlayerMP playerMP) {
    if (message.getIsReady()) {
      simpleNetworkWrapper.sendTo(message, playerMP);
    }
  }

  public static void sendToServer(IMessage message) {
    simpleNetworkWrapper.sendToServer(message);
  }
}
