package com.wxp.mod.mchelper.network;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/** @author wxp */
@Getter
@NoArgsConstructor
public class OpenGuiMessage implements IMessage {
  private int guiId;

  public OpenGuiMessage(int guiId) {
    this.guiId = guiId;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    guiId = ByteBufUtils.readVarShort(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeVarShort(buf, guiId);
  }
}
