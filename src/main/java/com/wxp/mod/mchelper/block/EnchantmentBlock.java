package com.wxp.mod.mchelper.block;

import com.wxp.mod.mchelper.McHelper;
import com.wxp.mod.mchelper.block.tileentity.TileEntityEnchantment;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.gui.ModHelperGuiHandler;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/** @author wxp */
public class EnchantmentBlock extends CommonBlock {
  private final String name = "enchantment_block";

  public EnchantmentBlock() {
    super(Material.ROCK, MapColor.RED);
    this.setLightOpacity(0);
    setRegistryName(name);
    setUnlocalizedName(name);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public Class<? extends TileEntity> getTileEntityClass() {
    return TileEntityEnchantment.class;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    super.randomDisplayTick(stateIn, worldIn, pos, rand);

    for (int i = -2; i <= 2; ++i) {
      for (int j = -2; j <= 2; ++j) {
        if (i > -2 && i < 2 && j == -1) {
          j = 2;
        }

        if (rand.nextInt(16) == 0) {
          for (int k = 0; k <= 1; ++k) {
            if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
              break;
            }
            worldIn.spawnParticle(
                EnumParticleTypes.ENCHANTMENT_TABLE,
                (double) pos.getX() + 0.5D,
                (double) pos.getY() + 2.0D,
                (double) pos.getZ() + 0.5D,
                (double) ((float) i + rand.nextFloat()) - 0.5D,
                ((float) k - rand.nextFloat() - 1.0F),
                (double) ((float) j + rand.nextFloat()) - 0.5D);
          }
        }
      }
    }
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityEnchantment();
  }

  @Override
  public boolean onBlockActivated(
      World worldIn,
      BlockPos pos,
      IBlockState state,
      EntityPlayer playerIn,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ) {
    if (!worldIn.isRemote) {
      if (ModConfig.functionControl.enableEnchant) {
        playerIn.openGui(
            McHelper.INSTANCE,
            ModHelperGuiHandler.ID_ENCHANTMENT_UI,
            worldIn,
            pos.getX(),
            pos.getY(),
            pos.getZ());
      }
    }
    return Boolean.TRUE;
  }
}
