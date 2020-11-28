package com.wxp.mod.mchelper.network.hanlder;

import com.wxp.mod.mchelper.gui.container.GuiEnchantmentContainer;
import com.wxp.mod.mchelper.network.EnchantmentMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/** @author wxp */
public class EnchantmentMessageHandler implements IMessageHandler<EnchantmentMessage, IMessage> {
  @Override
  public IMessage onMessage(EnchantmentMessage message, MessageContext ctx) {
    if (Side.SERVER.equals(ctx.side)) {
      ctx.getServerHandler()
          .player
          .getServerWorld()
          .addScheduledTask(
              () -> {
                EntityPlayerMP playerMP = ctx.getServerHandler().player;
                if (playerMP.openContainer instanceof GuiEnchantmentContainer) {
                  GuiEnchantmentContainer enchantmentContainer =
                      (GuiEnchantmentContainer) playerMP.openContainer;
                  enchantmentContainer.enchantmentItem(message.getEnchantmentData());
                }
              });
    }
    return null;
  }
}
