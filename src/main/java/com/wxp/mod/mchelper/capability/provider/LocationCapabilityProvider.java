package com.wxp.mod.mchelper.capability.provider;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.capability.impl.LocationCapabilityImpl;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author wxp 主要用于给用户附加location能力.配合类: LocationCapabilityProvider LocationCapabilityStorage
 *     PlayerEventHandler#onPlayerClone EntityEventHandler#onAttachCapabilitiesEntity
 *     EntityEventHandler#onPlayerJoinWorld Location CapabilityManager
 */
public class LocationCapabilityProvider implements ICapabilitySerializable {
  private LocationCapability locationCapability = new LocationCapabilityImpl();

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    return CapabilityManager.locationCapability.equals(capability);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    if (hasCapability(capability, facing)) {
      return CapabilityManager.locationCapability.cast(locationCapability);
    }
    return null;
  }

  @Override
  public NBTBase serializeNBT() {
    return CapabilityManager.locationCapabilityStorage.writeNBT(
        CapabilityManager.locationCapability, locationCapability, null);
  }

  @Override
  public void deserializeNBT(NBTBase nbt) {
    CapabilityManager.locationCapabilityStorage.readNBT(
        CapabilityManager.locationCapability, locationCapability, null, nbt);
  }
}
