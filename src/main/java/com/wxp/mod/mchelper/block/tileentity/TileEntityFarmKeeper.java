package com.wxp.mod.mchelper.block.tileentity;

import com.google.common.collect.Lists;
import com.wxp.mod.mchelper.block.FarmKeeperBlock;
import com.wxp.mod.mchelper.helper.OreDictionaryHelper;
import com.wxp.mod.mchelper.manager.FakePlayerManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** @author wxp */
public class TileEntityFarmKeeper extends TileEntity implements ITickable {
  private Boolean farmComplete = false;
  private Boolean farmKeeperSwitch = false;
  private BlockPos startPos;
  private BlockPos endPos;
  private int state = 0; // 0 空挡 -> 1 施肥 -> 2 收获 -> 3 耕地
  private ItemStackHandler seedInventory =
      new ItemStackHandler(3) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
          return isSeed(stack);
        }
      };
  private ItemStackHandler manureInventory =
      new ItemStackHandler(9) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
          return Items.DYE.equals(stack.getItem());
        }
      };
  private ItemStackHandler toolInventory =
      new ItemStackHandler(3) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
          return Lists.newArrayList(
                  Items.WOODEN_HOE,
                  Items.STONE_HOE,
                  Items.IRON_HOE,
                  Items.GOLDEN_HOE,
                  Items.DIAMOND_HOE)
              .contains(stack.getItem());
        }
      };
  private long ticks = 0;
  private final String keyFarmComplete = "farm_complete";
  private final String keyFarmKeeperSwitch = "farm_switch";
  private final String keySeedInventory = "seed_inventory";
  private final String keyManureInventory = "manure_inventory";
  private final String keyToolInventory = "tool_inventory";
  private final String keyStartX = "start_x";
  private final String keyStartY = "start_Y";
  private final String keyStartZ = "start_Z";
  private final String keyEndX = "end_x";
  private final String keyEndY = "end_y";
  private final String keyEndZ = "end_z";
  private final String keyState = "state";

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)) {
      if (facing == null) {
        return (T) seedInventory;
      }
      switch (facing) {
        case DOWN:
          return (T) manureInventory;
        case EAST:
          return (T) toolInventory;
        default:
          return (T) seedInventory;
      }
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public boolean shouldRefresh(
      World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return oldState.getBlock() != newSate.getBlock();
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    seedInventory.deserializeNBT(compound.getCompoundTag(keySeedInventory));
    manureInventory.deserializeNBT(compound.getCompoundTag(keyManureInventory));
    toolInventory.deserializeNBT(compound.getCompoundTag(keyToolInventory));
    this.farmComplete = compound.getBoolean(keyFarmComplete);
    this.farmKeeperSwitch = compound.getBoolean(keyFarmKeeperSwitch);
    this.startPos =
        new BlockPos(
            compound.getInteger(keyStartX),
            compound.getInteger(keyStartY),
            compound.getInteger(keyStartZ));
    this.endPos =
        new BlockPos(
            compound.getInteger(keyEndX),
            compound.getInteger(keyEndY),
            compound.getInteger(keyEndZ));
    this.state = compound.getInteger(keyState);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag(keySeedInventory, seedInventory.serializeNBT());
    compound.setTag(keyManureInventory, manureInventory.serializeNBT());
    compound.setTag(keyToolInventory, toolInventory.serializeNBT());
    compound.setBoolean(keyFarmComplete, this.farmComplete);
    compound.setBoolean(keyFarmKeeperSwitch, this.farmKeeperSwitch);
    if (startPos != null) {
      compound.setInteger(keyStartX, startPos.getX());
      compound.setInteger(keyStartY, startPos.getY());
      compound.setInteger(keyStartZ, startPos.getZ());
    }
    if (endPos != null) {
      compound.setInteger(keyEndX, endPos.getX());
      compound.setInteger(keyEndY, endPos.getY());
      compound.setInteger(keyEndZ, endPos.getZ());
    }
    compound.setInteger(keyState, this.state);
    super.writeToNBT(compound);
    return compound;
  }

  @Override
  public void update() {
    if (this.world.isRemote) {
      return;
    }
    ticks++;
    if (ticks % 20 == 0) {
      // 每秒处理任务
      if (!farmComplete) {
        farmComplete = checkFarmComplete();
      } else if (farmKeeperSwitch) {
        // 检查耕地
        state = 3;
      }
    } else if (ticks % 4 == 0 && farmComplete && farmKeeperSwitch) {
      if (state == 0) {
        if (hasManure() && loopAllFarmArea(this::isNeedManure, true)) {
          // 有肥料可以用, 进入施肥状态
          state = 1;
        } else {
          // 没有肥料尝试收获
          state = 2;
        }
      }
      boolean result = false;
      if (state == 1) {
        // 施肥
        if (hasManure()) {
          result = loopAllFarmArea(this::tryManure, true);
        } else {
          // 没有肥料了
          state = 0;
        }
      }
      if (state == 2) {
        // 尝试收获
        result = loopAllFarmArea(this::tryPlantAndHarvest, true);
      }
      if (state == 3) {
        result = loopAllFarmArea(this::confirmCanFarm, true);
      }
      if (!result) {
        state = 0;
      }
    }
  }

  public void dropAllItem() {
    Stream.iterate(0, (i -> i + 1))
        .limit(3)
        .forEach(
            num -> {
              ItemStack allItemStack = seedInventory.getStackInSlot(num);
              if (!allItemStack.isEmpty()) {
                ItemStack upDropItem =
                    seedInventory.extractItem(num, allItemStack.getCount(), false);
                Block.spawnAsEntity(this.world, pos, upDropItem);
              }
            });
    Stream.iterate(0, (i -> i + 1))
        .limit(3)
        .forEach(
            num -> {
              ItemStack allItemStack = toolInventory.getStackInSlot(num);
              if (!allItemStack.isEmpty()) {
                ItemStack upDropItem =
                    toolInventory.extractItem(num, allItemStack.getCount(), false);
                Block.spawnAsEntity(this.world, pos, upDropItem);
              }
            });
    Stream.iterate(0, (i -> i + 1))
        .limit(9)
        .forEach(
            num -> {
              ItemStack itemStack = manureInventory.getStackInSlot(1);
              if (!itemStack.isEmpty()) {
                ItemStack upDropItem = manureInventory.extractItem(1, itemStack.getCount(), false);
                Block.spawnAsEntity(this.world, pos, upDropItem);
              }
            });
  }

  public void setFarmComplete(Boolean farmComplete) {
    this.farmComplete = farmComplete;
  }

  public Boolean getFarmComplete() {
    return farmComplete;
  }

  public void setFarmKeeperSwitch(Boolean farmKeeperSwitch) {
    this.farmKeeperSwitch = farmKeeperSwitch;
  }

  public Boolean getFarmKeeperSwitch() {
    return farmKeeperSwitch;
  }

  private boolean checkFarmComplete() {
    Map<EnumFacing, BlockPos> map = new HashMap<>();
    BlockPos currentPos = this.getPos().down();
    for (EnumFacing enumFacing : EnumFacing.HORIZONTALS) {
      BlockPos temp = visitLastDistanceWood(currentPos, enumFacing);
      if (!currentPos.equals(temp)) {
        map.put(enumFacing, temp);
      }
    }
    List<EnumFacing> keySet = new ArrayList<>(map.keySet());
    BlockPos commonTarget = visitLastDistanceWood(map.get(keySet.get(0)), keySet.get(1));
    BlockPos tempTarget = visitLastDistanceWood(map.get(keySet.get(1)), keySet.get(0));
    if (commonTarget.equals(tempTarget)) {
      this.startPos =
          new BlockPos(
              Math.min(map.get(keySet.get(0)).getX(), map.get(keySet.get(1)).getX()),
              map.get(keySet.get(0)).getY(),
              Math.min(map.get(keySet.get(0)).getZ(), map.get(keySet.get(1)).getZ()));
      this.endPos =
          new BlockPos(
              Math.max(map.get(keySet.get(0)).getX(), map.get(keySet.get(1)).getX()),
              map.get(keySet.get(0)).getY(),
              Math.max(map.get(keySet.get(0)).getZ(), map.get(keySet.get(1)).getZ()));
      return checkAreaComplete();
    } else {
      return false;
    }
  }

  private BlockPos visitLastDistanceWood(BlockPos startPos, EnumFacing enumFacing) {
    BlockPos targetPos;
    switch (enumFacing) {
      case EAST:
        targetPos = startPos.east();
        break;
      case SOUTH:
        targetPos = startPos.south();
        break;
      case NORTH:
        targetPos = startPos.north();
        break;
      case WEST:
        targetPos = startPos.west();
        break;
      default:
        return null;
    }
    if (OreDictionaryHelper.isOriginWood(this.world.getBlockState(targetPos).getBlock())) {
      return visitLastDistanceWood(targetPos, enumFacing);
    }
    return startPos;
  }

  private boolean checkAreaComplete() {
    if (this.startPos == null || this.endPos == null) {
      return false;
    }
    boolean hasWater = false;
    for (int tempX = startPos.getX(); tempX <= endPos.getX(); tempX++) {
      for (int tempZ = startPos.getZ(); tempZ <= endPos.getZ(); tempZ++) {
        Block block =
            this.world.getBlockState(new BlockPos(tempX, startPos.getY(), tempZ)).getBlock();
        if (Blocks.WATER.equals(block)) {
          hasWater = true;
        } else if (Blocks.DIRT.equals(block)
            || Blocks.GRASS.equals(block)
            || Blocks.FARMLAND.equals(block)
            || OreDictionaryHelper.isOriginWood(block)) {
        } else {
          return false;
        }
      }
    }
    return hasWater;
  }

  private boolean loopAllFarmArea(Predicate<BlockPos> function) {
    return loopAllFarmArea(function, false);
  }

  private boolean loopAllFarmArea(Predicate<BlockPos> function, boolean expect) {
    if (this.startPos == null || this.endPos == null) {
      return false;
    }
    boolean isGetExpectResult =
        Stream.iterate(startPos.getX(), (i -> i + 1))
            .limit(endPos.getX() - startPos.getX() + 1)
            .anyMatch(
                tempX ->
                    Stream.iterate(startPos.getZ(), (j -> j + 1))
                        .limit(endPos.getZ() - startPos.getZ() + 1)
                        .anyMatch(
                            tempZ -> {
                              if (expect) {
                                return function.test(new BlockPos(tempX, startPos.getY(), tempZ));
                              } else {
                                return !function.test(new BlockPos(tempX, startPos.getY(), tempZ));
                              }
                            }));
    if (isGetExpectResult) {
      return expect;
    } else {
      return !expect;
    }
  }

  private boolean confirmCanFarm(BlockPos currentPos) {
    Block block = this.world.getBlockState(currentPos).getBlock();
    if (Blocks.DIRT.equals(block) || Blocks.GRASS.equals(block)) {
      // 耕地
      ItemStack tool = getTool();
      if (tool.isEmpty()) {
        return false;
      }
      EntityPlayerMP playerMP =
          FakePlayerManager.getFakePlayer((WorldServer) this.world, "farmer").get();
      playerMP.setHeldItem(EnumHand.MAIN_HAND, tool);
      tool.onItemUse(
          playerMP,
          this.world,
          currentPos,
          EnumHand.MAIN_HAND,
          playerMP.getHorizontalFacing(),
          currentPos.getX(),
          currentPos.getY(),
          currentPos.getZ());
      playerMP.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      return true;
    }
    return false;
  }

  private ItemStack getTool() {
    for (int i = 0; i < toolInventory.getSlots(); i++) {
      if (!toolInventory.getStackInSlot(i).isEmpty()) {
        return toolInventory.getStackInSlot(i);
      }
    }
    return ItemStack.EMPTY;
  }

  private ItemStack getSeed() {
    for (int i = 0; i < seedInventory.getSlots(); i++) {
      if (!seedInventory.getStackInSlot(i).isEmpty()) {
        return seedInventory.getStackInSlot(i);
      }
    }
    return ItemStack.EMPTY;
  }

  private boolean hasManure() {
    return Stream.iterate(0, (i -> i + 1))
        .limit(9)
        .anyMatch(num -> !this.manureInventory.getStackInSlot(num).isEmpty());
  }

  private ItemStack getManure() {
    for (int i = 0; i < manureInventory.getSlots(); i++) {
      if (!manureInventory.getStackInSlot(i).isEmpty()) {
        return manureInventory.getStackInSlot(i);
      }
    }
    return ItemStack.EMPTY;
  }

  private boolean isNeedManure(BlockPos pos) {
    Block currentBlock = this.world.getBlockState(pos).getBlock();
    if (!Blocks.FARMLAND.equals(currentBlock)) {
      return false;
    }
    Block block = this.world.getBlockState(pos.up()).getBlock();
    if (block instanceof BlockCrops) {
      return !((BlockCrops) block).isMaxAge(this.world.getBlockState(pos.up()));
    }
    return false;
  }

  private boolean tryManure(BlockPos pos) {
    Block currentBlock = this.world.getBlockState(pos).getBlock();
    if (!Blocks.FARMLAND.equals(currentBlock)) {
      return false;
    }
    IBlockState blockState = this.world.getBlockState(pos.up());
    Block block = blockState.getBlock();
    if (block instanceof BlockCrops) {
      BlockCrops blockCrops = (BlockCrops) block;
      if (!blockCrops.isMaxAge(blockState)) {
        ItemStack manure = getManure();
        EntityPlayerMP playerMP =
            FakePlayerManager.getFakePlayer((WorldServer) this.world, "farmer").get();
        while (!blockCrops.isMaxAge(this.world.getBlockState(pos.up()))) {
          if (manure.isEmpty()) {
            if (hasManure()) {
              manure = getManure();
            } else {
              // 肥料用光
              playerMP.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
              return true;
            }
          }
          playerMP.setHeldItem(EnumHand.MAIN_HAND, manure);
          manure.onItemUse(
              playerMP,
              this.world,
              pos.up(),
              EnumHand.MAIN_HAND,
              EnumFacing.UP,
              pos.up().getX(),
              pos.up().getY(),
              pos.up().getZ());
        }
        playerMP.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        return true;
      }
    }
    return false;
  }

  private boolean tryPlantAndHarvest(BlockPos pos) {
    Block currentBlock = this.world.getBlockState(pos).getBlock();
    if (!Blocks.FARMLAND.equals(currentBlock)) {
      return false;
    }
    BlockPos cropPos = pos.up();
    IBlockState blockState = this.world.getBlockState(cropPos);
    Block block = blockState.getBlock();
    if (block instanceof BlockCrops) {
      BlockCrops blockCrops = (BlockCrops) block;
      if (blockCrops.isMaxAge(blockState)) {
        // 收获
        EntityPlayerMP playerMP =
            FakePlayerManager.getFakePlayer((WorldServer) this.world, "farmer").get();
        List<ItemStack> drops = getFarmDrops(cropPos, playerMP);
        BlockPos itemDownPos =
            this.getPos()
                .down()
                .offset(
                    this.world
                        .getBlockState(this.getPos())
                        .getValue(FarmKeeperBlock.FACING)
                        .getOpposite());
        drops.forEach(
            dropItem -> {
              if (Lists.newArrayList(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT)
                  .contains(dropItem.getItem())) {
                // 种子
                for (int i = 0; i < seedInventory.getSlots(); i++) {
                  seedInventory.insertItem(i, dropItem, false);
                }
                if (!dropItem.isEmpty()) {
                  // 满了
                  Block.spawnAsEntity(this.world, itemDownPos, dropItem);
                }
              } else {
                // crop
                Block.spawnAsEntity(this.world, itemDownPos, dropItem);
              }
            });
        block.removedByPlayer(blockState, world, pos.up(), playerMP, true);
        return true;
      }
    }
    if (block.isAir(blockState, this.world, pos)) {
      // 种植
      ItemStack seed = getSeed();
      if (seed.isEmpty()) {
        return false;
      }
      EntityPlayerMP playerMP =
          FakePlayerManager.getFakePlayer((WorldServer) this.world, "farmer").get();
      playerMP.setHeldItem(EnumHand.MAIN_HAND, seed);
      seed.onItemUse(
          playerMP,
          this.world,
          pos,
          EnumHand.MAIN_HAND,
          EnumFacing.UP,
          pos.getX(),
          pos.getY(),
          pos.getZ());
      playerMP.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      return true;
    }
    return false;
  }

  private List<ItemStack> getFarmDrops(BlockPos blockPos, EntityPlayerMP playerMP) {
    NonNullList<ItemStack> dropItems = NonNullList.create();
    IBlockState blockState = this.world.getBlockState(blockPos);
    BlockCrops blockCrops = (BlockCrops) blockState.getBlock();
    blockCrops.getDrops(dropItems, this.world, this.getPos(), blockState, 0);
    float chance =
        ForgeEventFactory.fireBlockHarvesting(
            dropItems, this.world, this.getPos(), blockState, 0, 1.0f, false, playerMP);
    return dropItems.stream()
        .filter(itemStack -> this.world.rand.nextFloat() <= chance)
        .collect(Collectors.toList());
  }

  private boolean isSeed(ItemStack stack) {
    return Lists.newArrayList(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT)
        .contains(stack.getItem());
  }
}
