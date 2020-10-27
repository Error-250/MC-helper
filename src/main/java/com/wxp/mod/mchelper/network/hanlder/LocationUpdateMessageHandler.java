package com.wxp.mod.mchelper.network.hanlder;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.domain.Location;
import com.wxp.mod.mchelper.domain.LocationUpdateData;
import com.wxp.mod.mchelper.helper.LocationHelper;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.network.LocationCapabilitySyncMessage;
import com.wxp.mod.mchelper.network.LocationUpdateMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/** @author wxp */
public class LocationUpdateMessageHandler
    implements IMessageHandler<LocationUpdateMessage, IMessage> {
  @Override
  public IMessage onMessage(LocationUpdateMessage message, MessageContext ctx) {
    if (Side.SERVER.equals(ctx.side)) {
      ctx.getServerHandler()
          .player
          .getServerWorld()
          .addScheduledTask(
              () -> {
                EntityPlayerMP playerMP = ctx.getServerHandler().player;
                LocationCapability locationCapability =
                    playerMP.getCapability(CapabilityManager.locationCapability, null);
                if (locationCapability == null) {
                  return;
                }
                LocationUpdateData updateData = message.getUpdateDate();
                switch (updateData.getOperate()) {
                  case UPDATE:
                    if (LocationUpdateData.SupportUpdateField.ALLOW_NEAREST.equals(
                        updateData.getField())) {
                      locationCapability.setAllowNearestLocation(updateData.getAllowNearest());
                      LocationCapabilitySyncMessage locationCapabilitySyncMessage =
                          new LocationCapabilitySyncMessage(playerMP);
                      NetworkManager.sendTo(locationCapabilitySyncMessage, playerMP);
                    } else if (LocationUpdateData.SupportUpdateField.LOCATION.equals(
                        updateData.getField())) {
                      Location location =
                          locationCapability.getLocationByAlias(
                              playerMP.world, updateData.getLocation().getAlias());
                      location.setDesc(updateData.getLocation().getDesc());
                      LocationCapabilitySyncMessage locationCapabilitySyncMessage =
                          new LocationCapabilitySyncMessage(playerMP);
                      NetworkManager.sendTo(locationCapabilitySyncMessage, playerMP);
                    }
                    break;
                  case ADD:
                    if (LocationUpdateData.SupportUpdateField.LOCATION.equals(
                        updateData.getField())) {
                      String errMsg =
                          locationCapability.saveLocation(playerMP.world, updateData.getLocation());
                      if (!StringUtils.isNullOrEmpty(errMsg)) {
                        playerMP.sendMessage(new TextComponentString(errMsg));
                      } else {
                        LocationCapabilitySyncMessage locationCapabilitySyncMessage =
                            new LocationCapabilitySyncMessage(playerMP);
                        NetworkManager.sendTo(locationCapabilitySyncMessage, playerMP);
                      }
                    }
                    break;
                  case JUMP:
                    LocationHelper.jumpToLocation(
                        playerMP, locationCapability, updateData.getLocation().getAlias());
                    break;
                  case DELETE:
                    LocationHelper.deleteLocation(
                        playerMP, locationCapability, updateData.getLocation().getAlias());
                    break;
                  default:
                }
              });
    }
    return null;
  }
}
