package com.wxp.mod.mchelper.gui.guicontainer;

import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.gui.component.button.GuiButtonList;
import com.wxp.mod.mchelper.gui.component.helper.GuiLabelHelper;
import com.wxp.mod.mchelper.gui.container.GuiEnchantmentContainer;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.network.EnchantmentMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/** @author wxp */
public class GuiEnchantmentGuiContainer extends AbstractGuiContainer {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(ModConfig.MOD_ID, "textures/gui/container/enchantment_table.png");
  private GuiEnchantmentContainer guiEnchantmentContainer;
  private GuiLabel titleLabel;
  private GuiButtonList guiButtonList;

  public GuiEnchantmentGuiContainer(GuiEnchantmentContainer inventorySlotsIn) {
    super(inventorySlotsIn, 176, 166);
    this.guiEnchantmentContainer = inventorySlotsIn;
  }

  @Override
  ResourceLocation getBackgroundResource() {
    return TEXTURE;
  }

  @Override
  public void initGui() {
    int middleOffsetX = (this.width - this.xSize) / 2;
    int middleOffsetY = (this.height - this.ySize) / 2;
    // 初始化按钮
    titleLabel =
        new GuiLabel(this.fontRenderer, 1, middleOffsetX + 5, middleOffsetY + 3, 10, 17, -1);
    titleLabel.addLine(
        String.format(
            "附魔台  当前等级:%s/%s",
            guiEnchantmentContainer.getTileEntityEnchantment().getEnchantmentPowerLevel(),
            guiEnchantmentContainer.getTileEntityEnchantment().getMaxPowerLevel()));

    guiButtonList = new GuiButtonList(60, 13, 108, 57, 3, 2);
    guiButtonList.initGui(middleOffsetX, middleOffsetY);
    guiButtonList.setTexture(0, 166, 108, 18, TEXTURE);
    guiButtonList.setDisableTexture(0, 18);
    guiButtonList.getButtons().forEach(this::addButton);

    this.labelList.add(titleLabel);
    super.initGui();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    String title =
        String.format(
            "附魔台  当前等级:%s/%s",
            guiEnchantmentContainer.getTileEntityEnchantment().getEnchantmentPowerLevel(),
            guiEnchantmentContainer.getTileEntityEnchantment().getMaxPowerLevel());
    GuiLabelHelper.setText(titleLabel, title);

    List<String> showTexts =
        guiEnchantmentContainer.getTileEntityEnchantment().getEnchantmentDataList().stream()
            .map(
                enchantmentData ->
                    I18n.format(
                        "container.enchant.clue",
                        enchantmentData.enchantment.getTranslatedName(
                            enchantmentData.enchantmentLevel)))
            .collect(Collectors.toList());
    List<Boolean> batchSets =
        guiEnchantmentContainer.getTileEntityEnchantment().getEnchantmentDataList().stream()
            .map(
                enchantmentData ->
                    guiEnchantmentContainer.getEntityPlayer().isCreative()
                        || guiEnchantmentContainer
                                .getTileEntityEnchantment()
                                .getEnchantmentHandler()
                                .getStackInSlot(1)
                                .getCount()
                            >= enchantmentData.enchantmentLevel)
            .collect(Collectors.toList());
    guiButtonList.setTexts(showTexts);
    guiButtonList.batchSetEnable(batchSets);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    int index = guiButtonList.actionPerformed(button.id);
    if (index == -1) {
      return;
    }
    EnchantmentData enchantmentData =
        guiEnchantmentContainer.getTileEntityEnchantment().getEnchantmentDataList().get(index);
    NetworkManager.sendToServer(new EnchantmentMessage(enchantmentData));
  }
}
