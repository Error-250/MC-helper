package com.wxp.mod.mchelper.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/** @author wxp */
public abstract class AbstractGuiAllPlayerInventoryContainer extends AbstractGuiContainer {
  public AbstractGuiAllPlayerInventoryContainer(EntityPlayer entityPlayer) {
    super(entityPlayer);
    for (int i = 0; i < 9; i++) {
      this.addSlotToContainer(new Slot(entityPlayer.inventory, i, 8 + i * 18, 132));
    }

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        this.addSlotToContainer(
            new Slot(entityPlayer.inventory, j + i * 9 + 9, 8 + j * 18, 74 + i * 18));
      }
    }
  }

  @Override
  boolean isContainsQuakeInventory() {
    return true;
  }

  @Override
  boolean isContainsPlayerInventory() {
    return true;
  }
}
