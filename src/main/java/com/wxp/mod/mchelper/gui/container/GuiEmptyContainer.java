package com.wxp.mod.mchelper.gui.container;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/** @author wxp location服务端container */
public class GuiEmptyContainer extends Container {
  private EntityPlayer player;
  private LocationCapability locationCapability;

  public GuiEmptyContainer(EntityPlayer player) {
    this.player = player;
    this.locationCapability = player.getCapability(CapabilityManager.locationCapability, null);
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.locationCapability != null;
  }

  public EntityPlayer getPlayer() {
    return player;
  }

  public LocationCapability getLocationCapability() {
    return locationCapability;
  }
}
