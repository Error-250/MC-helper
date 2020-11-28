package com.wxp.mod.mchelper.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/** @author wxp */
public abstract class AbstractGuiAllPlayerInventoryContainer extends AbstractGuiContainer {
  public AbstractGuiAllPlayerInventoryContainer(EntityPlayer entityPlayer) {
    this(entityPlayer, 8, 74);
  }

  public AbstractGuiAllPlayerInventoryContainer(
      EntityPlayer entityPlayer, int xOffset, int yOffset) {
    super(entityPlayer);
    for (int i = 0; i < 9; i++) {
      this.addSlotToContainer(new Slot(entityPlayer.inventory, i, xOffset + i * 18, yOffset + 58));
    }

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        this.addSlotToContainer(
            new Slot(entityPlayer.inventory, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
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
