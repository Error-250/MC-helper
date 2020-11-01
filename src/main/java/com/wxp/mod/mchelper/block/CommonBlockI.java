package com.wxp.mod.mchelper.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

/** @author wxp */
public interface CommonBlockI {
  /**
   * 获取用于注册的item block
   *
   * @return item block
   */
  ItemBlock getItemBlock();

  /**
   * 获取自身, 用于注册
   *
   * @return 获取自身用于注册
   */
  Block getSelf();

  /**
   * 是否存在tile entity
   *
   * @param state 状态
   * @return 是否
   */
  boolean hasTileEntity(IBlockState state);

  /**
   * 获取tile entity的class
   *
   * @return class
   */
  Class<? extends TileEntity> getTileEntityClass();
}
