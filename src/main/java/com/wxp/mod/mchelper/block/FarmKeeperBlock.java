package com.wxp.mod.mchelper.block;

import com.wxp.mod.mchelper.McHelper;
import com.wxp.mod.mchelper.block.tileentity.TileEntityFarmKeeper;
import com.wxp.mod.mchelper.gui.ModHelperGuiHandler;
import com.wxp.mod.mchelper.helper.OreDictionaryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

/** @author wxp */
public class FarmKeeperBlock extends CommonBlock {
  private final String name = "farm_keeper";
  public static final PropertyDirection FACING =
      PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

  public FarmKeeperBlock() {
    super(Material.IRON);
    setRegistryName(name);
    setUnlocalizedName(name);
    this.setDefaultState(
        this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return Boolean.TRUE;
  }

  @Override
  public Class<? extends TileEntity> getTileEntityClass() {
    return TileEntityFarmKeeper.class;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityFarmKeeper();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing facing = EnumFacing.getHorizontal(meta);
    return getDefaultState().withProperty(FACING, facing);
  }

  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return OreDictionaryHelper.isOriginWood(worldIn.getBlockState(pos.down()).getBlock());
  }

  @Override
  public void onBlockPlacedBy(
      World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.setBlockState(
        pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
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
      playerIn.openGui(
          McHelper.INSTANCE,
          ModHelperGuiHandler.ID_FARM_KEEPER_UI,
          worldIn,
          pos.getX(),
          pos.getY(),
          pos.getZ());
    }
    return Boolean.TRUE;
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntityFarmKeeper tileEntityFarmKeeper = (TileEntityFarmKeeper) worldIn.getTileEntity(pos);
    if (tileEntityFarmKeeper != null) {
      tileEntityFarmKeeper.dropAllItem();
    }
    super.breakBlock(worldIn, pos, state);
  }
}
