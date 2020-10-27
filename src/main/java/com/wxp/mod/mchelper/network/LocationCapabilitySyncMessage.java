package com.wxp.mod.mchelper.network;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author wxp
 */
@NoArgsConstructor
public class LocationCapabilitySyncMessage extends AbstractNbtMessage {
  public LocationCapabilitySyncMessage(EntityPlayer entityPlayer) {
    if (entityPlayer.hasCapability(CapabilityManager.locationCapability, null)) {
      LocationCapability locationCapability =
              entityPlayer.getCapability(CapabilityManager.locationCapability, null);
      if (locationCapability != null) {
        this.nbt = (NBTTagCompound) CapabilityManager.locationCapabilityStorage.writeNBT(CapabilityManager.locationCapability, locationCapability, null);
        this.isReady = true;
      }
    }
  }
}
