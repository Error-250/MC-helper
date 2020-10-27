package com.wxp.mod.mchelper.network;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/** @author wxp */
@Getter
public class AbstractNbtMessage implements IMessage {
  protected NBTTagCompound nbt = null;
  protected Boolean isReady;

  AbstractNbtMessage() {
    isReady = false;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    nbt = ByteBufUtils.readTag(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    if (this.nbt == null) {
      return;
    }
    ByteBufUtils.writeTag(buf, nbt);
  }
}
