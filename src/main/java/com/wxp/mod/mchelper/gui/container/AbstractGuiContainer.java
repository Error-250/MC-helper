package com.wxp.mod.mchelper.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author wxp
 */
public abstract class AbstractGuiContainer extends Container {
  private EntityPlayer entityPlayer;
  /**
   * 返回Gui新添加的slot列表
   *
   * @return slot列表
   */
  abstract int getGuiSlotSize();

  /**
   * 是否包含快捷物品栏
   *
   * @return true/false
   */
  abstract boolean isContainsQuakeInventory();

  /**
   * 是否包含玩家物品栏
   *
   * @return true/false
   */
  abstract boolean isContainsPlayerInventory();
  /**
   * 是否允許打開container
   *
   * @param playerIn 玩家
   * @return 是否允許
   */
  @Override
  public abstract boolean canInteractWith(EntityPlayer playerIn);

  public AbstractGuiContainer(EntityPlayer entityPlayer) {
    this.entityPlayer = entityPlayer;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    Slot slot = inventorySlots.get(index);

    if (slot == null || !slot.getHasStack()) {
      return ItemStack.EMPTY;
    }

    if (getGuiSlotSize() == 0) {
      return ItemStack.EMPTY;
    }

    if (!isContainsQuakeInventory() && !isContainsPlayerInventory()) {
      return ItemStack.EMPTY;
    }

    ItemStack newStack = slot.getStack();
    ItemStack oldStack = newStack.copy();

    boolean isMerged = false;

    int maxQuakeInventoryIndex = 9;
    int playerInventorySize = 27;
    if (index < maxQuakeInventoryIndex) {
      if (isContainsQuakeInventory()) {
        // 操作的是快捷物品栏
        int guiSlotStartIndex =
            isContainsPlayerInventory()
                ? maxQuakeInventoryIndex + playerInventorySize
                : maxQuakeInventoryIndex;
        isMerged =
            mergeItemStack(
                newStack, guiSlotStartIndex, guiSlotStartIndex + getGuiSlotSize(), false);
        if (isContainsPlayerInventory()) {
          // 尝试放置到玩家物品栏
          isMerged =
              isMerged
                  || mergeItemStack(
                      newStack,
                      maxQuakeInventoryIndex,
                      maxQuakeInventoryIndex + playerInventorySize,
                      false);
        }
      }
    }
    if (index >= maxQuakeInventoryIndex && index < maxQuakeInventoryIndex + getGuiSlotSize()) {
      if (isContainsQuakeInventory() && !isContainsPlayerInventory()) {
        isMerged = mergeItemStack(newStack, 0, maxQuakeInventoryIndex, false);
      }
    }
    if (index < playerInventorySize) {
      if (!isContainsQuakeInventory() && isContainsPlayerInventory()) {
        // 操作的是玩家物品栏
        isMerged =
            mergeItemStack(
                newStack, playerInventorySize, playerInventorySize + getGuiSlotSize(), false);
      }
    }
    if (index >= playerInventorySize && index < playerInventorySize + getGuiSlotSize()) {
      if (!isContainsQuakeInventory() && isContainsPlayerInventory()) {
        isMerged =
            mergeItemStack(
                newStack, playerInventorySize, playerInventorySize + getGuiSlotSize(), false);
      }
    }
    if (index >= maxQuakeInventoryIndex && index < maxQuakeInventoryIndex + playerInventorySize) {
      if (isContainsQuakeInventory() && isContainsPlayerInventory()) {
        // 操作物品栏
        isMerged =
            mergeItemStack(
                newStack,
                maxQuakeInventoryIndex + playerInventorySize,
                maxQuakeInventoryIndex + playerInventorySize + getGuiSlotSize(),
                false);
        isMerged = isMerged || mergeItemStack(newStack, 0, maxQuakeInventoryIndex, false);
      }
    }
    if (index >= maxQuakeInventoryIndex + playerInventorySize) {
      if (isContainsQuakeInventory() && isContainsPlayerInventory()) {
        isMerged = mergeItemStack(newStack, 0, maxQuakeInventoryIndex, false);
        isMerged =
            isMerged
                || mergeItemStack(
                    newStack,
                    maxQuakeInventoryIndex,
                    maxQuakeInventoryIndex + playerInventorySize,
                    false);
      }
    }

    if (!isMerged) {
      return ItemStack.EMPTY;
    }

    if (newStack.getCount() == 0) {
      slot.putStack(ItemStack.EMPTY);
    } else {
      slot.onSlotChanged();
    }

    slot.onTake(playerIn, newStack);

    return oldStack;
  }

  public EntityPlayer getEntityPlayer() {
    return entityPlayer;
  }
}
