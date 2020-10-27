package com.wxp.mod.mchelper.helper;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.domain.Location;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.network.LocationCapabilitySyncMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentString;

import java.util.Objects;

/** @author wxp location辅助,主要是通用的跳转/创建/删除功能。 */
public class LocationHelper {
  public static void jumpToLocation(
      EntityPlayerMP entityPlayerMP, LocationCapability locationCapability, String alias) {
    Location matchedLocation = locationCapability.getLocationByAlias(entityPlayerMP.world, alias);
    if (matchedLocation == null) {
      entityPlayerMP.sendMessage(new TextComponentString("Location not found."));
      return;
    }
    if (entityPlayerMP.isRiding()) {
      // 骑乘状态下, 将被骑乘的坐骑一起移动.
      Objects.requireNonNull(entityPlayerMP.getRidingEntity())
          .setPositionAndUpdate(
              matchedLocation.getPosition().x,
              matchedLocation.getPosition().y,
              matchedLocation.getPosition().z);
    }
    entityPlayerMP.setPositionAndUpdate(
        matchedLocation.getPosition().x,
        matchedLocation.getPosition().y,
        matchedLocation.getPosition().z);
  }

  public static void deleteLocation(
      EntityPlayerMP entityPlayer, LocationCapability locationCapability, String alias) {
    if (locationCapability.deleteLocationByAlias(entityPlayer.world, alias)) {
      LocationCapabilitySyncMessage locationCapabilitySyncMessage =
          new LocationCapabilitySyncMessage(entityPlayer);
      NetworkManager.sendTo(locationCapabilitySyncMessage, entityPlayer);
      entityPlayer.sendMessage(new TextComponentString("done."));
    } else {
      entityPlayer.sendMessage(new TextComponentString("Location not found."));
    }
  }

  public static void saveLocation(
      EntityPlayerMP entityPlayer, LocationCapability locationCapability, Location location) {
    String errMsg = locationCapability.saveLocation(entityPlayer.world, location);
    if (StringUtils.isNullOrEmpty(errMsg)) {
      LocationCapabilitySyncMessage locationCapabilitySyncMessage =
          new LocationCapabilitySyncMessage(entityPlayer);
      NetworkManager.sendTo(locationCapabilitySyncMessage, entityPlayer);
      entityPlayer.sendMessage(new TextComponentString("done."));
    } else {
      entityPlayer.sendMessage(new TextComponentString(errMsg));
    }
  }
}
