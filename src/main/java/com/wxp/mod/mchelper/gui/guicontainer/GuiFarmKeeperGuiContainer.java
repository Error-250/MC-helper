package com.wxp.mod.mchelper.gui.guicontainer;

import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.domain.FarmKeeperSwitchUpdateData;
import com.wxp.mod.mchelper.gui.container.GuiFarmKeeperContainer;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.network.BlockStateSwitchMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/** @author wxp */
public class GuiFarmKeeperGuiContainer extends AbstractGuiContainer {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(ModConfig.MOD_ID, "textures/gui/container/farm_keeper.png");
  private GuiFarmKeeperContainer container;
  private GuiLabel switchLabel;
  private GuiLabel seedLabel;
  private GuiLabel manureLabel;
  private GuiLabel toolLabel;
  private GuiButtonToggle switchButton;

  public GuiFarmKeeperGuiContainer(GuiFarmKeeperContainer inventorySlotsIn) {
    super(inventorySlotsIn);
    this.container = inventorySlotsIn;
  }

  @Override
  ResourceLocation getBackgroundResource() {
    return TEXTURE;
  }

  @Override
  public void initGui() {
    super.initGui();
    int middleOffsetX = (this.width - this.xSize) / 2;
    int middleOffsetY = (this.height - this.ySize) / 2;

    switchLabel =
        new GuiLabel(this.fontRenderer, 1, middleOffsetX + 6, middleOffsetY + 7, 10, 17, -1);
    switchLabel.addLine("启动:");
    switchButton =
        new GuiButtonToggle(
            2,
            middleOffsetX + 27,
            middleOffsetY + 7,
            35,
            17,
            container.getTileEntityFarmKeeper().getFarmKeeperSwitch());
    switchButton.initTextureValues(46, 158, -42, 0, TEXTURE);
    switchButton.enabled = container.getTileEntityFarmKeeper().getFarmComplete();
    manureLabel =
        new GuiLabel(this.fontRenderer, 3, middleOffsetX + 88, middleOffsetY + 18, 10, 17, -1);
    manureLabel.addLine("肥料:");
    seedLabel =
        new GuiLabel(this.fontRenderer, 4, middleOffsetX + 10, middleOffsetY + 35, 10, 17, -1);
    seedLabel.addLine("种子:");
    toolLabel =
        new GuiLabel(this.fontRenderer, 5, middleOffsetX + 10, middleOffsetY + 53, 10, 17, -1);
    toolLabel.addLine("工具:");

    this.labelList.add(switchLabel);
    this.labelList.add(manureLabel);
    this.labelList.add(seedLabel);
    this.labelList.add(toolLabel);

    this.addButton(switchButton);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == switchButton.id) {
      boolean result = !switchButton.isStateTriggered();
      switchButton.setStateTriggered(result);
      this.container.getTileEntityFarmKeeper().setFarmKeeperSwitch(result);
      FarmKeeperSwitchUpdateData farmKeeperSwitchUpdateData = new FarmKeeperSwitchUpdateData();
      farmKeeperSwitchUpdateData.setFarmSwitch(result);
      farmKeeperSwitchUpdateData.setPosition(this.container.getTileEntityFarmKeeper().getPos());
      BlockStateSwitchMessage blockStateSwitchMessage =
          new BlockStateSwitchMessage(farmKeeperSwitchUpdateData);
      NetworkManager.sendToServer(blockStateSwitchMessage);
    }
  }
}
