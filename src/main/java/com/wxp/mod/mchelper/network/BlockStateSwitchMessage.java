package com.wxp.mod.mchelper.network;

import com.wxp.mod.mchelper.domain.FarmKeeperSwitchUpdateData;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/** @author wxp */
@NoArgsConstructor
public class BlockStateSwitchMessage extends AbstractNbtMessage {
  public BlockStateSwitchMessage(FarmKeeperSwitchUpdateData farmKeeperSwitchUpdateData) {
    this.nbt = serialize(farmKeeperSwitchUpdateData);
    this.isReady = true;
  }

  public FarmKeeperSwitchUpdateData getUpdateDate() {
    return deserialize(this.nbt);
  }

  private NBTTagCompound serialize(FarmKeeperSwitchUpdateData updateData) {
    NBTTagCompound compound = new NBTTagCompound();
    compound.setInteger("x", updateData.getPosition().getX());
    compound.setInteger("y", updateData.getPosition().getY());
    compound.setInteger("z", updateData.getPosition().getZ());
    compound.setBoolean("switch", updateData.isFarmSwitch());
    return compound;
  }

  private FarmKeeperSwitchUpdateData deserialize(NBTTagCompound tag) {
    BlockPos position = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    FarmKeeperSwitchUpdateData updateData = new FarmKeeperSwitchUpdateData();
    updateData.setPosition(position);
    updateData.setFarmSwitch(tag.getBoolean("switch"));
    return updateData;
  }
}
