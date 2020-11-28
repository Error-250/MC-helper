package com.wxp.mod.mchelper.helper;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

/** @author wxp */
public class ItemStackHelper {
  public static List<EnchantmentData> getItemStackEnchantmentList(ItemStack itemStack) {
    if (!itemStack.isItemEnchanted()) {
      return Collections.emptyList();
    }
    List<EnchantmentData> enchantmentData = Lists.newArrayList();
    for (NBTBase nbtBase : itemStack.getEnchantmentTagList()) {
      NBTTagCompound nbtTagCompound = (NBTTagCompound) nbtBase;
      int id = nbtTagCompound.getShort("id");
      int level = nbtTagCompound.getShort("lvl");
      Enchantment enchantment = Enchantment.getEnchantmentByID(id);
      enchantmentData.add(new EnchantmentData(enchantment, level));
    }
    return enchantmentData;
  }
}
