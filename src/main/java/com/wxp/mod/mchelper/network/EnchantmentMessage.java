package com.wxp.mod.mchelper.network;

import lombok.NoArgsConstructor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.nbt.NBTTagCompound;

/** @author wxp */
@NoArgsConstructor
public class EnchantmentMessage extends AbstractNbtMessage {
  public EnchantmentMessage(EnchantmentData enchantmentData) {
    this.nbt = serialize(enchantmentData);
    this.isReady = true;
  }

  public EnchantmentData getEnchantmentData() {
    return deserialize(this.nbt);
  }

  private NBTTagCompound serialize(EnchantmentData enchantmentData) {
    NBTTagCompound nbtTagCompound = new NBTTagCompound();
    nbtTagCompound.setInteger(
        "enchantment_id", Enchantment.getEnchantmentID(enchantmentData.enchantment));
    nbtTagCompound.setInteger("level", enchantmentData.enchantmentLevel);
    return nbtTagCompound;
  }

  private EnchantmentData deserialize(NBTTagCompound nbtTagCompound) {
    return new EnchantmentData(
        Enchantment.getEnchantmentByID(nbtTagCompound.getInteger("enchantment_id")),
        nbtTagCompound.getInteger("level"));
  }
}
