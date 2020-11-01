package com.wxp.mod.mchelper.network.hanlder;

import com.wxp.mod.mchelper.block.tileentity.TileEntityFarmKeeper;
import com.wxp.mod.mchelper.network.BlockStateSwitchMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/** @author wxp */
public class BlockStateSwitchMessageHandler
    implements IMessageHandler<BlockStateSwitchMessage, IMessage> {
  @Override
  public IMessage onMessage(BlockStateSwitchMessage message, MessageContext ctx) {
    if (Side.SERVER.equals(ctx.side)) {
      ctx.getServerHandler()
          .player
          .getServerWorld()
          .addScheduledTask(
              () -> {
                TileEntityFarmKeeper tileEntityFarmKeeper =
                    (TileEntityFarmKeeper)
                        ctx.getServerHandler()
                            .player
                            .getServerWorld()
                            .getTileEntity(message.getUpdateDate().getPosition());
                if (tileEntityFarmKeeper != null) {
                  tileEntityFarmKeeper.setFarmKeeperSwitch(message.getUpdateDate().isFarmSwitch());
                }
              });
    }
    return null;
  }
}
