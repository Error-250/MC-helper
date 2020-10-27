package com.wxp.mod.mchelper.eventhandler;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** @author wxp 玩家相关事件。主要用于玩家死亡以及在各个世界穿梭的时候复制location能力。 */
@Mod.EventBusSubscriber(modid = ModConfig.MOD_ID)
public class PlayerEventHandler {
  @SubscribeEvent
  public static void onPlayerClone(PlayerEvent.Clone event) {
    EntityPlayer entityPlayer = event.getOriginal();
    LocationCapability originLocationCapability =
        entityPlayer.getCapability(CapabilityManager.locationCapability, null);
    if (originLocationCapability != null) {
      // 玩家死亡时 复制玩家生前的能力
      NBTBase nbtBase =
          CapabilityManager.locationCapabilityStorage.writeNBT(
              CapabilityManager.locationCapability, originLocationCapability, null);
      LocationCapability locationCapability =
          event.getEntityPlayer().getCapability(CapabilityManager.locationCapability, null);
      CapabilityManager.locationCapabilityStorage.readNBT(
          CapabilityManager.locationCapability, locationCapability, null, nbtBase);
    }
  }
}
