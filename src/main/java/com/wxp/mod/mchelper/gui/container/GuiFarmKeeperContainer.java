package com.wxp.mod.mchelper.gui.container;

import com.wxp.mod.mchelper.block.tileentity.TileEntityFarmKeeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.stream.Stream;

/** @author wxp */
public class GuiFarmKeeperContainer extends AbstractGuiAllPlayerInventoryContainer {
  private TileEntityFarmKeeper tileEntityFarmKeeper;

  public GuiFarmKeeperContainer(
      EntityPlayer entityPlayer, TileEntityFarmKeeper tileEntityFarmKeeper) {
    super(entityPlayer);
    this.tileEntityFarmKeeper = tileEntityFarmKeeper;

    IItemHandler seedHandler =
        tileEntityFarmKeeper.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    Stream.iterate(0, (i -> i + 1))
        .limit(3)
        .forEach(
            num -> {
              this.addSlotToContainer(new SlotItemHandler(seedHandler, num, 45 + num * 18, 35));
            });

    IItemHandler manureHandler =
        tileEntityFarmKeeper.getCapability(
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
    Stream.iterate(0, (i -> i + 1))
        .limit(9)
        .forEach(
            num -> {
              this.addSlotToContainer(
                  new SlotItemHandler(
                      manureHandler, num, 116 + (num % 3) * 18, 17 + (num / 3) * 18));
            });

    IItemHandler toolHandler =
        tileEntityFarmKeeper.getCapability(
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.EAST);
    Stream.iterate(0, (i -> i + 1))
        .limit(3)
        .forEach(
            num -> {
              this.addSlotToContainer(new SlotItemHandler(toolHandler, num, 45 + num * 18, 53));
            });
  }

  @Override
  int getGuiSlotSize() {
    return 10;
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    // TODO 指定玩家
    return Boolean.TRUE;
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (IContainerListener listener : this.listeners) {
      int data =
          (tileEntityFarmKeeper.getFarmComplete() ? 2 : 0)
              | (tileEntityFarmKeeper.getFarmKeeperSwitch() ? 1 : 0);
      listener.sendWindowProperty(this, 0, data);
    }
  }

  @Override
  public void updateProgressBar(int id, int data) {
    super.updateProgressBar(id, data);
    if (id == 0) {
      tileEntityFarmKeeper.setFarmComplete((data & 2) == 2);
      tileEntityFarmKeeper.setFarmKeeperSwitch((data & 1) == 1);
    }
  }

  public TileEntityFarmKeeper getTileEntityFarmKeeper() {
    return tileEntityFarmKeeper;
  }
}
