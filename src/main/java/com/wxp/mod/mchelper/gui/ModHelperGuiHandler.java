package com.wxp.mod.mchelper.gui;

import com.wxp.mod.mchelper.block.tileentity.TileEntityFarmKeeper;
import com.wxp.mod.mchelper.gui.container.GuiEmptyContainer;
import com.wxp.mod.mchelper.gui.container.GuiFarmKeeperContainer;
import com.wxp.mod.mchelper.gui.guicontainer.GuiEmptyGuiContainer;
import com.wxp.mod.mchelper.gui.guicontainer.GuiFarmKeeperGuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/** @author wxp gui管理 */
public class ModHelperGuiHandler implements IGuiHandler {
  public static final int ID_LOCATION_UI = 1;
  public static final int ID_FARM_KEEPER_UI = 2;

  @Nullable
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == ID_LOCATION_UI) {
      return new GuiEmptyContainer(player);
    }
    if (ID == ID_FARM_KEEPER_UI) {
      return new GuiFarmKeeperContainer(
          player, (TileEntityFarmKeeper) world.getTileEntity(new BlockPos(x, y, z)));
    }
    return null;
  }

  @Nullable
  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == ID_LOCATION_UI) {
      return new GuiEmptyGuiContainer(new GuiEmptyContainer(player));
    }
    if (ID == ID_FARM_KEEPER_UI) {
      return new GuiFarmKeeperGuiContainer(
          new GuiFarmKeeperContainer(
              player, (TileEntityFarmKeeper) world.getTileEntity(new BlockPos(x, y, z))));
    }
    return null;
  }
}
