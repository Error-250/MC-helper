package com.wxp.mod.mchelper.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/** @author wxp gui管理 */
public class ModHelperGuiHandler implements IGuiHandler {
  public static final int ID_LOCATION_UI = 1;

  @Nullable
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == ID_LOCATION_UI) {
      return new GuiEmptyContainer(player);
    }
    return null;
  }

  @Nullable
  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == ID_LOCATION_UI) {
      return new GuiEmptyGuiContainer(new GuiEmptyContainer(player));
    }
    return null;
  }
}
