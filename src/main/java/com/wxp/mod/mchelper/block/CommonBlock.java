package com.wxp.mod.mchelper.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

/** @author wxp */
public abstract class CommonBlock extends Block {
  CommonBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
    super(blockMaterialIn, blockMapColorIn);
  }

  CommonBlock(Material materialIn) {
    this(materialIn, materialIn.getMaterialMapColor());
  }

  /**
   * 获取tile entity的class
   *
   * @return class
   */
  public abstract Class<? extends TileEntity> getTileEntityClass();

  /**
   * 获取用于注册的item block
   *
   * @return item block
   */
  public ItemBlock getItemBlock() {
    ItemBlock itemBlock = new ItemBlock(this);
    itemBlock.setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    return itemBlock;
  }
}
