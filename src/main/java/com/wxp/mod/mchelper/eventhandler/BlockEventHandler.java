package com.wxp.mod.mchelper.eventhandler;

import com.wxp.mod.mchelper.McHelper;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.helper.OreDictionaryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

/**
 * @author wxp
 */
@Mod.EventBusSubscriber(modid = ModConfig.MOD_ID)
public class BlockEventHandler {
  @SubscribeEvent
  public static void onHarvestDrop(BlockEvent.HarvestDropsEvent event) {
    if (event.getHarvester() == null) {
      return;
    }
    Block targetBlock = event.getState().getBlock();
    if (OreDictionaryHelper.isTree(targetBlock)) {
      Arrays.stream(EnumFacing.values()).forEach(facing -> autoCutDownTree(event.getPos(), event.getWorld(), event.getHarvester(), facing));
    } else if (OreDictionaryHelper.isOre(targetBlock)) {
      Arrays.stream(EnumFacing.values()).forEach(facing -> autoHarvestOre(event.getPos(), event.getWorld(), event.getHarvester(), facing, targetBlock));
    } else if (Blocks.OBSIDIAN.equals(targetBlock)) {
      EnumFacing playerFacing = event.getHarvester().getHorizontalFacing();
      Arrays.stream(EnumFacing.HORIZONTALS).filter(facing -> !facing.equals(playerFacing)).forEach(facing -> {
        autoHarvestObsidian(event.getPos(), event.getWorld(), event.getHarvester(), facing);
      });
    }
  }

  private static void autoCutDownTree(BlockPos startPos, World world, EntityPlayer entityPlayer, EnumFacing facing) {
    BlockPos targetPos = nextPos(startPos, facing);
    if (targetPos == null) {
      return;
    }
    IBlockState blockState = world.getBlockState(targetPos);
    Block block = blockState.getBlock();
    if (OreDictionaryHelper.isTree(block) && block.canHarvestBlock(world, targetPos, entityPlayer)) {
      block.removedByPlayer(blockState, world, targetPos, entityPlayer, true);
      block.harvestBlock(world, entityPlayer, targetPos, blockState, null, entityPlayer.getHeldItemMainhand());
      if (!ItemStack.EMPTY.equals(entityPlayer.getHeldItemMainhand())) {
        entityPlayer.getHeldItemMainhand().damageItem(1, entityPlayer);
      }
    }
  }

  private static void autoHarvestOre(BlockPos startPos, World world, EntityPlayer entityPlayer, EnumFacing facing, Block oreBlock) {
    BlockPos targetPos = nextPos(startPos, facing);
    if (targetPos == null) {
      return;
    }
    IBlockState blockState = world.getBlockState(targetPos);
    Block block = blockState.getBlock();
    if (block.equals(oreBlock) && block.canHarvestBlock(world, targetPos, entityPlayer)) {
      block.removedByPlayer(blockState, world, targetPos, entityPlayer, true);
      block.harvestBlock(world, entityPlayer, targetPos, blockState, null, entityPlayer.getHeldItemMainhand());
      entityPlayer.getHeldItemMainhand().damageItem(1, entityPlayer);
    }
  }

  private static void autoHarvestObsidian(BlockPos startPos, World world, EntityPlayer entityPlayer, EnumFacing facing) {
    BlockPos targetPos = nextPos(startPos, facing);
    if (targetPos == null) {
      return;
    }
    BlockPos targetDownPos = targetPos.down();
    IBlockState blockState = world.getBlockState(targetPos);
    Block block = blockState.getBlock();
    IBlockState downBlockState = world.getBlockState(targetDownPos);
    Block downBlock = downBlockState.getBlock();
    if (Blocks.OBSIDIAN.equals(block) && !Blocks.LAVA.equals(downBlock) && block.canHarvestBlock(world, targetPos, entityPlayer)) {
      block.removedByPlayer(blockState, world, targetPos, entityPlayer, true);
      block.harvestBlock(world, entityPlayer, targetPos, blockState, null, entityPlayer.getHeldItemMainhand());
      entityPlayer.getHeldItemMainhand().damageItem(1, entityPlayer);
    }
  }

  private static BlockPos nextPos(BlockPos startPos, EnumFacing facing) {
    BlockPos targetPos = null;
    switch (facing) {
      case EAST:
        targetPos = startPos.east();
        break;
      case WEST:
        targetPos = startPos.west();
        break;
      case NORTH:
        targetPos = startPos.north();
        break;
      case SOUTH:
        targetPos = startPos.south();
        break;
      case UP:
        targetPos = startPos.up();
        break;
      case DOWN:
        targetPos = startPos.down();
        break;
      default:
    }
    return targetPos;
  }
}
