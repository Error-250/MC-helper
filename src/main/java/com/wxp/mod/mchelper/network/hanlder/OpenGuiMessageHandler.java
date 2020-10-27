package com.wxp.mod.mchelper.network.hanlder;

import com.wxp.mod.mchelper.McHelper;
import com.wxp.mod.mchelper.network.OpenGuiMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/** @author wxp */
public class OpenGuiMessageHandler implements IMessageHandler<OpenGuiMessage, IMessage> {
  @Override
  public IMessage onMessage(OpenGuiMessage message, MessageContext ctx) {
    if (Side.SERVER.equals(ctx.side)) {
      ctx.getServerHandler()
          .player
          .getServerWorld()
          .addScheduledTask(
              () -> {
                EntityPlayerMP playerMP = ctx.getServerHandler().player;
                playerMP.openGui(
                    McHelper.INSTANCE,
                    message.getGuiId(),
                    playerMP.world,
                    playerMP.getPosition().getX(),
                    playerMP.getPosition().getY(),
                    playerMP.getPosition().getZ());
              });
    }
    return null;
  }
}
