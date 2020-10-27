package com.wxp.mod.mchelper.eventhandler;

import com.wxp.mod.mchelper.capability.provider.LocationCapabilityProvider;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.network.LocationCapabilitySyncMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** @author wxp 实体相关事件.主要用于给玩家附加location能力, 并在加入世界的时候从服务端同步数据到客户端。 */
@Mod.EventBusSubscriber(modid = ModConfig.MOD_ID)
public class EntityEventHandler {
  @SubscribeEvent
  public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
    Entity entity = event.getObject();
    if (entity instanceof EntityPlayer) {
      event.addCapability(
          new ResourceLocation(ModConfig.MOD_ID, "location_saved"),
          new LocationCapabilityProvider());
    }
  }

  @SubscribeEvent
  public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
    if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
      EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
      LocationCapabilitySyncMessage locationCapabilitySyncMessage =
          new LocationCapabilitySyncMessage(player);
      NetworkManager.sendTo(locationCapabilitySyncMessage, player);
    }
  }
}
