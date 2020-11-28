package com.wxp.mod.mchelper.gui.container;

import com.wxp.mod.mchelper.block.tileentity.TileEntityEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.SlotItemHandler;

/** @author wxp */
public class GuiEnchantmentContainer extends AbstractGuiAllPlayerInventoryContainer {
  private TileEntityEnchantment tileEntityEnchantment;

  public GuiEnchantmentContainer(
      EntityPlayer entityPlayer, TileEntityEnchantment tileEntityEnchantment) {
    super(entityPlayer, 8, 84);
    this.tileEntityEnchantment = tileEntityEnchantment;
    this.addSlotToContainer(
        new SlotItemHandler(tileEntityEnchantment.getEnchantmentHandler(), 0, 14, 47));
    this.addSlotToContainer(
        new SlotItemHandler(tileEntityEnchantment.getEnchantmentHandler(), 1, 35, 47));
  }

  @Override
  int getGuiSlotSize() {
    return 2;
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (IContainerListener listener : this.listeners) {
      if (tileEntityEnchantment.getEnchantmentDataList().size() > 0) {
        for (int i = 0; i < tileEntityEnchantment.getEnchantmentDataList().size(); i++) {
          EnchantmentData enchantmentData =
              this.tileEntityEnchantment.getEnchantmentDataList().get(i);
          listener.sendWindowProperty(
              this,
              i,
              Enchantment.getEnchantmentID(enchantmentData.enchantment) * 10
                  + enchantmentData.enchantmentLevel);
        }
      } else {
        listener.sendWindowProperty(this, 0, -1);
      }
      listener.sendWindowProperty(this, 3, this.tileEntityEnchantment.getEnchantmentPowerLevel());
    }
  }

  @Override
  public void updateProgressBar(int id, int data) {
    super.updateProgressBar(id, data);
    if (id >= 0 && id <= 2) {
      if (data == -1) {
        tileEntityEnchantment.getEnchantmentDataList().clear();
        return;
      }
      if (tileEntityEnchantment.getEnchantmentDataList().size() > id) {
        tileEntityEnchantment.getEnchantmentDataList().remove(id);
      }
      int enchantmentId = data / 10;
      int level = data % 10;
      EnchantmentData enchantmentData =
          new EnchantmentData(Enchantment.getEnchantmentByID(enchantmentId), level);
      tileEntityEnchantment.getEnchantmentDataList().add(id, enchantmentData);
    }
    if (id == 3) {
      this.tileEntityEnchantment.setEnchantmentPowerLevel(data);
    }
  }

  public void enchantmentItem(EnchantmentData enchantmentData) {
    if (!getEntityPlayer().capabilities.isCreativeMode) {
      if (tileEntityEnchantment.getEnchantmentHandler().getStackInSlot(1).isEmpty()) {
        return;
      }
      if (tileEntityEnchantment.getEnchantmentHandler().getStackInSlot(1).getCount()
          < enchantmentData.enchantmentLevel) {
        return;
      }
    }
    if (tileEntityEnchantment.getEnchantmentHandler().getStackInSlot(0).isEmpty()) {
      return;
    }
    // 可以附魔
    tileEntityEnchantment
        .getEnchantmentHandler()
        .getStackInSlot(0)
        .addEnchantment(enchantmentData.enchantment, enchantmentData.enchantmentLevel);
    tileEntityEnchantment
        .getEnchantmentHandler()
        .getStackInSlot(1)
        .shrink(enchantmentData.enchantmentLevel);
    this.getEntityPlayer()
        .world
        .playSound(
            (EntityPlayer) null,
            tileEntityEnchantment.getPos(),
            SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
            SoundCategory.BLOCKS,
            1.0F,
            this.getEntityPlayer().world.rand.nextFloat() * 0.1F + 0.9F);
  }

  public TileEntityEnchantment getTileEntityEnchantment() {
    return tileEntityEnchantment;
  }
}
