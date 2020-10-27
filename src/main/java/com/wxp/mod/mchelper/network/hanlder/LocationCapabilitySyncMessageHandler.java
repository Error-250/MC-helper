package com.wxp.mod.mchelper.network.hanlder;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import com.wxp.mod.mchelper.network.LocationCapabilitySyncMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/** @author wxp */
public class LocationCapabilitySyncMessageHandler
    implements IMessageHandler<LocationCapabilitySyncMessage, IMessage> {
  @Override
  public IMessage onMessage(LocationCapabilitySyncMessage message, MessageContext ctx) {
    if (Side.CLIENT == ctx.side) {
      Minecraft.getMinecraft()
          .addScheduledTask(
              () -> {
                EntityPlayer player = Minecraft.getMinecraft().player;
                LocationCapability locationCapability =
                    player.getCapability(CapabilityManager.locationCapability, null);
                if (locationCapability != null) {
                  locationCapability.clearLocation(player.world);
                  CapabilityManager.locationCapabilityStorage.readNBT(
                      CapabilityManager.locationCapability,
                      locationCapability,
                      null,
                      message.getNbt());
                }
              });
    }
    return null;
  }
}
