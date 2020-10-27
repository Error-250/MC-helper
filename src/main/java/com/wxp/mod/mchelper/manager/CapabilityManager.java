package com.wxp.mod.mchelper.manager;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.capability.impl.LocationCapabilityImpl;
import com.wxp.mod.mchelper.capability.storage.LocationCapabilityStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * @author wxp
 */
public class CapabilityManager {
  @CapabilityInject(LocationCapability.class)
  public static Capability<LocationCapability> locationCapability;

  public static LocationCapabilityStorage locationCapabilityStorage = new LocationCapabilityStorage();

  public static void initCapability() {
    net.minecraftforge.common.capabilities.CapabilityManager.INSTANCE.register(LocationCapability.class, locationCapabilityStorage, LocationCapabilityImpl::new);
  }
}
