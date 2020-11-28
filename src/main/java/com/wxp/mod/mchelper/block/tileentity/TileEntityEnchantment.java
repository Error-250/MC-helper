package com.wxp.mod.mchelper.block.tileentity;

import com.google.common.collect.Lists;
import com.wxp.mod.mchelper.helper.ItemStackHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** @author wxp */
public class TileEntityEnchantment extends TileEntityEnchantmentTable {
  @Getter @Setter private int enchantmentPowerLevel;

  @Getter
  private ItemStackHandler enchantmentHandler =
      new ItemStackHandler(2) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
          if (slot == 1) {
            NonNullList<ItemStack> stackNonNullList = OreDictionary.getOres("gemLapis");
            return stackNonNullList.stream()
                .anyMatch(itemStack -> OreDictionary.itemMatches(stack, itemStack, false));
          }
          return super.isItemValid(slot, stack);
        }
      };

  @Getter private List<EnchantmentData> enchantmentDataList = new ArrayList<>();

  @Getter private final int maxPowerLevel = 50;

  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    return null;
  }

  @Override
  public String getGuiID() {
    return "";
  }

  @Override
  public void update() {
    super.update();
    if (this.world.isRemote) {
      return;
    }
    if (enchantmentHandler.getStackInSlot(0).isEmpty()
        && enchantmentHandler.getStackInSlot(1).isEmpty()) {
      this.enchantmentDataList.clear();
      return;
    }
    if (enchantmentHandler.getStackInSlot(0).isEmpty()) {
      EntityPlayer entityplayer =
          this.world.getClosestPlayer(
              (double) ((float) this.pos.getX() + 0.5F),
              (double) ((float) this.pos.getY() + 0.5F),
              (double) ((float) this.pos.getZ() + 0.5F),
              3.0D,
              false);
      if (entityplayer == null) {
        return;
      }
      int maxLevel =
          entityplayer.capabilities.isCreativeMode
              ? maxPowerLevel
              : Math.min(entityplayer.experienceLevel, maxPowerLevel);
      if (enchantmentPowerLevel < maxLevel) {
        tryUpdateEnchantPower(enchantmentHandler.getStackInSlot(1));
      }
      this.enchantmentDataList.clear();
    } else {
      if (enchantmentDataList.size() == 0) {
        generateRandomEnchantment();
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.enchantmentPowerLevel = compound.getInteger("enchant_power_level");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("enchant_power_level", enchantmentPowerLevel);
    return super.writeToNBT(compound);
  }

  private void generateRandomEnchantment() {
    ItemStack itemStack = enchantmentHandler.getStackInSlot(0);
    if (!itemStack.getItem().isEnchantable(itemStack)) {
      return;
    }
    List<EnchantmentData> enchantmentDataList =
        getAllowEnchantmentList(enchantmentPowerLevel, itemStack);
    if (enchantmentDataList.size() == 0) {
      return;
    }
    if (itemStack.isItemEnchanted()) {
      ItemStackHelper.getItemStackEnchantmentList(itemStack)
          .forEach(
              enchantmentData -> {
                EnchantmentHelper.removeIncompatible(enchantmentDataList, enchantmentData);
              });
    }
    if (enchantmentDataList.size() == 0) {
      return;
    }
    this.enchantmentDataList.clear();
    for (int i = 0; i < 3; i++) {
      if (enchantmentDataList.size() == 0) {
        break;
      }
      int randomInt = this.world.rand.nextInt(enchantmentDataList.size());
      EnchantmentData enchantmentData = enchantmentDataList.get(randomInt);
      enchantmentDataList.remove(randomInt);
      this.enchantmentDataList.add(enchantmentData);
    }
  }

  private List<EnchantmentData> getAllowEnchantmentList(int enchantmentLevel, ItemStack itemStack) {
    if (Items.BOOK.equals(itemStack.getItem())) {
      return Collections.emptyList();
    }
    List<EnchantmentData> enchantmentData = Lists.newArrayList();
    for (Enchantment enchantment : Enchantment.REGISTRY) {
      if (!enchantment.canApplyAtEnchantingTable(itemStack)) {
        continue;
      }
      int maxLevel = 0;
      for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
        if (enchantmentLevel >= enchantment.getMinEnchantability(level)) {
          maxLevel = level;
        } else {
          break;
        }
      }
      if (maxLevel > 0) {
        enchantmentData.add(new EnchantmentData(enchantment, maxLevel));
      }
    }
    return enchantmentData;
  }

  private void tryUpdateEnchantPower(ItemStack itemStack) {
    if (!Items.DYE.equals(itemStack.getItem())) {
      return;
    }
    if (itemStack.getCount() < getNeedDyeForNextLevel()) {
      return;
    }
    itemStack.shrink(getNeedDyeForNextLevel());
    enchantmentPowerLevel += 1;
  }

  private int getNeedDyeForNextLevel() {
    if (enchantmentPowerLevel < 10) {
      return 10;
    } else if (enchantmentPowerLevel < 20) {
      return 20;
    } else if (enchantmentPowerLevel < 30) {
      return 30;
    } else if (enchantmentPowerLevel < 40) {
      return 40;
    } else {
      return 50;
    }
  }
}
