package com.wxp.mod.mchelper.gui.guicontainer;

import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.domain.Location;
import com.wxp.mod.mchelper.domain.LocationUpdateData;
import com.wxp.mod.mchelper.domain.OperateEnum;
import com.wxp.mod.mchelper.gui.component.button.GuiButtonList;
import com.wxp.mod.mchelper.gui.component.button.GuiImageButton;
import com.wxp.mod.mchelper.gui.component.helper.GuiLabelHelper;
import com.wxp.mod.mchelper.gui.component.textfield.GuiInputTextField;
import com.wxp.mod.mchelper.gui.container.GuiEmptyContainer;
import com.wxp.mod.mchelper.manager.NetworkManager;
import com.wxp.mod.mchelper.network.LocationUpdateMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.stream.Collectors;

/** @author wxp location客户端container */
public class GuiEmptyGuiContainer extends AbstractGuiContainer {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(ModConfig.MOD_ID, "textures/gui/container/location_background.png");
  private GuiImageButton leftPageButton;
  private GuiInputTextField pageInputField;
  private GuiImageButton pageJumpButton;
  private GuiImageButton rightPageButton;
  private GuiButtonToggle allowNearestButton;
  private GuiImageButton newLocationButton;
  private GuiButtonList buttonList;
  private GuiLabel allowNearestLabel;
  private GuiImageButton backButton;
  private GuiInputTextField nameInputField;
  private GuiLabel nameLabel;
  private GuiInputTextField descInputField;
  private GuiLabel descLabel;
  private GuiLabel xLabel;
  private GuiLabel yLabel;
  private GuiLabel zLabel;
  private GuiImageButton saveButton;
  private GuiLabel distanceLabel;
  private GuiImageButton editButton;
  private GuiImageButton jumpButton;
  private GuiImageButton deleteButton;
  private GuiEmptyContainer container;
  private int currentPageSize = 1;
  private final int mainUi = 0;
  private final int createUi = 1;
  private final int detailUi = 2;
  private int currentUi = 0;
  private Location selectLocation;

  public GuiEmptyGuiContainer(GuiEmptyContainer inventorySlotsIn) {
    super(inventorySlotsIn);
    this.container = inventorySlotsIn;
  }

  @Override
  ResourceLocation getBackgroundResource() {
    return TEXTURE;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    if (this.currentUi == mainUi) {
      this.pageInputField.drawTextBox();
      // 规避label + button的bug
      this.allowNearestLabel.drawLabel(this.mc, mouseX, mouseY);
    } else if (this.currentUi == createUi || this.currentUi == detailUi) {
      nameInputField.drawTextBox();
      descInputField.drawTextBox();
    }
    showUi();
  }

  @Override
  public void initGui() {
    super.initGui();
    int middleOffsetX = (this.width - this.xSize) / 2;
    int middleOffsetY = (this.height - this.ySize) / 2;
    drawHead(middleOffsetX, middleOffsetY);
    drawMiddle(middleOffsetX, middleOffsetY);
    drawBottom(middleOffsetX, middleOffsetY);
  }

  private void drawBottom(int middleOffsetX, int middleOffsetY) {
    int controlWidth = (int) (this.xSize * 0.7);
    int startX = (this.xSize - controlWidth) / 2;
    leftPageButton =
        new GuiImageButton(
            1, middleOffsetX + startX, middleOffsetY + 135, 11, 17, 15, 176, TEXTURE);
    leftPageButton.setDisEnableTextureOffset(15, 158);
    int inputFieldWidth = controlWidth - 52;
    pageInputField =
        new GuiInputTextField(
            2,
            this.fontRenderer,
            middleOffsetX + startX + 13,
            middleOffsetY + 135,
            inputFieldWidth,
            17);
    pageInputField.setBackgroundColor(0);
    pageInputField.setBorderColor(0);
    pageInputField.setDefaultText("1");
    pageJumpButton =
        new GuiImageButton(
            3,
            middleOffsetX + startX + inputFieldWidth + 15,
            middleOffsetY + 135,
            30,
            17,
            "跳转",
            1,
            168,
            200,
            20,
            TEXTURE);
    rightPageButton =
        new GuiImageButton(
            4,
            middleOffsetX + startX + inputFieldWidth + 46,
            middleOffsetY + 135,
            11,
            17,
            2,
            176,
            TEXTURE);
    rightPageButton.setDisEnableTextureOffset(2, 158);
    saveButton =
        new GuiImageButton(
            17,
            middleOffsetX + startX,
            middleOffsetY + 135,
            controlWidth,
            17,
            "保存",
            1,
            168,
            200,
            20,
            TEXTURE);
    editButton =
        new GuiImageButton(
            19,
            middleOffsetX + startX,
            middleOffsetY + 135,
            (controlWidth - 2) / 3,
            17,
            "保存编辑",
            1,
            168,
            200,
            20,
            TEXTURE);
    jumpButton =
        new GuiImageButton(
            20,
            middleOffsetX + startX + 1 + (controlWidth - 2) / 3,
            middleOffsetY + 135,
            (controlWidth - 2) / 3,
            17,
            "传送",
            1,
            168,
            200,
            20,
            TEXTURE);
    deleteButton =
        new GuiImageButton(
            21,
            middleOffsetX + startX + controlWidth - (controlWidth - 2) / 3,
            middleOffsetY + 135,
            (controlWidth - 2) / 3,
            17,
            "删除",
            1,
            168,
            200,
            20,
            TEXTURE);
    setPageControlButtonEnable();

    this.addButton(leftPageButton);
    this.addButton(pageJumpButton);
    this.addButton(rightPageButton);
    this.addButton(saveButton);
    this.addButton(editButton);
    this.addButton(jumpButton);
    this.addButton(deleteButton);
  }

  private void drawMiddle(int middleOffsetX, int middleOffsetY) {
    this.container.getLocationCapability().setPageSize(6);
    buttonList = new GuiButtonList(3, 25, 169, 107, 6, 22);
    buttonList.initGui(middleOffsetX, middleOffsetY);
    buttonList.setTexture(1, 168, 200, 20, TEXTURE);
    buttonList.getButtons().forEach(this::addButton);
    setCurrentPageTexts();

    nameLabel =
        new GuiLabel(this.fontRenderer, 10, middleOffsetX + 10, middleOffsetY + 29, 30, 17, -1);
    nameLabel.addLine("名称:");
    nameInputField =
        new GuiInputTextField(
            11, this.fontRenderer, middleOffsetX + 40, middleOffsetY + 29, 100, 17);
    nameInputField.setBackgroundColor(0);
    nameInputField.setBorderColor(0);
    descLabel =
        new GuiLabel(this.fontRenderer, 12, middleOffsetX + 10, middleOffsetY + 47, 30, 17, -1);
    descLabel.addLine("描述:");
    descInputField =
        new GuiInputTextField(
            13, this.fontRenderer, middleOffsetX + 40, middleOffsetY + 47, 100, 17);
    descInputField.setBackgroundColor(0);
    descInputField.setBorderColor(0);

    xLabel =
        new GuiLabel(this.fontRenderer, 14, middleOffsetX + 10, middleOffsetY + 65, 30, 17, -1);
    yLabel =
        new GuiLabel(this.fontRenderer, 15, middleOffsetX + 10, middleOffsetY + 83, 30, 17, -1);
    zLabel =
        new GuiLabel(this.fontRenderer, 16, middleOffsetX + 10, middleOffsetY + 101, 30, 17, -1);
    distanceLabel =
        new GuiLabel(this.fontRenderer, 18, middleOffsetX + 10, middleOffsetY + 119, 30, 17, -1);
    this.labelList.add(nameLabel);
    this.labelList.add(descLabel);
    this.labelList.add(xLabel);
    this.labelList.add(yLabel);
    this.labelList.add(zLabel);
    this.labelList.add(distanceLabel);
  }

  private void drawHead(int middleOffsetX, int middleOffsetY) {
    allowNearestLabel =
        new GuiLabel(this.fontRenderer, 7, middleOffsetX + 5, middleOffsetY + 5, 50, 17, -1);
    allowNearestLabel.addLine("是否允许添加附近位置:");
    allowNearestButton =
        new GuiButtonToggle(
            5,
            middleOffsetX + 98,
            middleOffsetY + 5,
            35,
            17,
            this.container.getLocationCapability().isAllowNearestLocation());
    allowNearestButton.initTextureValues(72, 158, -42, 0, TEXTURE);
    newLocationButton =
        new GuiImageButton(
            6, middleOffsetX + 135, middleOffsetY + 5, 35, 17, "新建", 1, 168, 200, 20, TEXTURE);
    this.addButton(allowNearestButton);
    this.addButton(newLocationButton);

    backButton =
        new GuiImageButton(8, middleOffsetX + 6, middleOffsetY + 6, 20, 17, 30, 177, TEXTURE);
    this.addButton(backButton);
  }

  private void showUi() {
    if (this.currentUi == mainUi) {
      hideCreateUi();
      hideDetailUi();
      showMainUi();
    } else if (this.currentUi == createUi) {
      hideMainUi();
      hideDetailUi();
      showCreateUi();
    } else if (this.currentUi == detailUi) {
      hideMainUi();
      hideCreateUi();
      showDetailUi();
    }
  }

  private void hideMainUi() {
    allowNearestLabel.visible = false;
    allowNearestButton.visible = false;
    newLocationButton.visible = false;
    buttonList.setVisible(false);
    leftPageButton.visible = false;
    pageInputField.setVisible(false);
    pageJumpButton.visible = false;
    rightPageButton.visible = false;
  }

  private void showMainUi() {
    allowNearestLabel.visible = true;
    allowNearestButton.visible = true;
    newLocationButton.visible = true;
    buttonList.setVisible(true);
    leftPageButton.visible = true;
    pageInputField.setVisible(true);
    pageJumpButton.visible = true;
    rightPageButton.visible = true;
  }

  private void showCreateUi() {
    backButton.visible = true;
    nameLabel.visible = true;
    nameInputField.setVisible(true);
    descLabel.visible = true;
    descInputField.setVisible(true);
    xLabel.visible = true;
    yLabel.visible = true;
    zLabel.visible = true;
    saveButton.visible = true;
    GuiLabelHelper.setText(
        xLabel, String.format("X坐标:%s", this.container.getPlayer().getPosition().getX()));
    GuiLabelHelper.setText(
        yLabel, String.format("Y坐标:%s", this.container.getPlayer().getPosition().getY()));
    GuiLabelHelper.setText(
        zLabel, String.format("Z坐标:%s", this.container.getPlayer().getPosition().getZ()));
  }

  private void hideCreateUi() {
    backButton.visible = false;
    nameLabel.visible = false;
    nameInputField.setVisible(false);
    descLabel.visible = false;
    descInputField.setVisible(false);
    xLabel.visible = false;
    yLabel.visible = false;
    zLabel.visible = false;
    saveButton.visible = false;
  }

  private void showDetailUi() {
    backButton.visible = true;
    nameLabel.visible = true;
    nameInputField.setVisible(true);
    nameInputField.setEnabled(false);
    descLabel.visible = true;
    descInputField.setVisible(true);
    xLabel.visible = true;
    yLabel.visible = true;
    zLabel.visible = true;
    distanceLabel.visible = true;
    editButton.visible = true;
    jumpButton.visible = true;
    deleteButton.visible = true;
  }

  private void hideDetailUi() {
    backButton.visible = false;
    nameLabel.visible = false;
    nameInputField.setVisible(false);
    nameInputField.setEnabled(true);
    descLabel.visible = false;
    descInputField.setVisible(false);
    xLabel.visible = false;
    yLabel.visible = false;
    zLabel.visible = false;
    distanceLabel.visible = false;
    editButton.visible = false;
    jumpButton.visible = false;
    deleteButton.visible = false;
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    if (pageInputField.x <= mouseX
        && pageInputField.x + pageInputField.width >= mouseX
        && pageInputField.y <= mouseY
        && pageInputField.y + pageInputField.height >= mouseY) {
      pageInputField.setFocused(true);
    } else {
      pageInputField.setFocused(false);
    }
    if (nameInputField.x <= mouseX
        && nameInputField.x + nameInputField.width >= mouseX
        && nameInputField.y <= mouseY
        && nameInputField.y + nameInputField.height >= mouseY) {
      nameInputField.setFocused(true);
    } else {
      nameInputField.setFocused(false);
    }
    if (descInputField.x <= mouseX
        && descInputField.x + descInputField.width >= mouseX
        && descInputField.y <= mouseY
        && descInputField.y + descInputField.height >= mouseY) {
      descInputField.setFocused(true);
    } else {
      descInputField.setFocused(false);
    }
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (pageInputField.isFocused()
        && (Character.isDigit(typedChar) || Keyboard.KEY_BACK == keyCode)) {
      pageInputField.textboxKeyTyped(typedChar, keyCode);
    } else if (nameInputField.isFocused()) {
      nameInputField.textboxKeyTyped(typedChar, keyCode);
    } else if (descInputField.isFocused()) {
      descInputField.textboxKeyTyped(typedChar, keyCode);
    } else {
      super.keyTyped(typedChar, keyCode);
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == allowNearestButton.id) {
      boolean result = !allowNearestButton.isStateTriggered();
      allowNearestButton.setStateTriggered(result);
      LocationUpdateData locationUpdateData = new LocationUpdateData();
      locationUpdateData.setOperate(OperateEnum.UPDATE);
      locationUpdateData.setAllowNearest(result);
      locationUpdateData.setField(LocationUpdateData.SupportUpdateField.ALLOW_NEAREST);
      LocationUpdateMessage locationUpdateMessage = new LocationUpdateMessage(locationUpdateData);
      NetworkManager.sendToServer(locationUpdateMessage);
    } else if (button.id == newLocationButton.id) {
      // 新建
      nameInputField.setText("");
      descInputField.setText("");
      this.currentUi = createUi;
    } else if (button.id == leftPageButton.id) {
      this.currentPageSize--;
      if (this.currentPageSize < 1) {
        this.currentPageSize = 1;
      }
      setPageControlButtonEnable();
      setCurrentPageTexts();
    } else if (button.id == rightPageButton.id) {
      this.currentPageSize++;
      int pageSize =
          this.container.getLocationCapability().locationPageSize(this.container.getPlayer().world);
      if (this.currentPageSize > pageSize) {
        this.currentPageSize = pageSize;
      }
      setPageControlButtonEnable();
      setCurrentPageTexts();
    } else if (button.id == pageJumpButton.id) {
      int pageSize =
          this.container.getLocationCapability().locationPageSize(this.container.getPlayer().world);
      String text = pageInputField.getText();
      Integer inputPage = Integer.valueOf(text);
      if (inputPage < 1) {
        inputPage = 1;
      }
      if (inputPage > pageSize) {
        inputPage = pageSize;
      }
      this.currentPageSize = inputPage;
      setPageControlButtonEnable();
      setCurrentPageTexts();
    } else if (button.id == backButton.id) {
      this.currentUi = mainUi;
    } else if (button.id == saveButton.id) {
      Location location = new Location();
      location.setAlias(nameInputField.getText());
      location.setDesc(descInputField.getText());
      location.setPosition(this.container.getPlayer().getPositionVector());
      nameInputField.setText("");
      descInputField.setText("");
      LocationUpdateData locationUpdateData = new LocationUpdateData();
      locationUpdateData.setOperate(OperateEnum.ADD);
      locationUpdateData.setField(LocationUpdateData.SupportUpdateField.LOCATION);
      locationUpdateData.setLocation(location);
      NetworkManager.sendToServer(new LocationUpdateMessage(locationUpdateData));
      this.container.getPlayer().closeScreen();
    } else if (button.id == editButton.id) {
      String desc = descInputField.getText();
      if (!desc.equalsIgnoreCase(this.selectLocation.getDesc())) {
        Location location = new Location();
        location.setAlias(this.selectLocation.getAlias());
        location.setDesc(desc);
        LocationUpdateData updateData = new LocationUpdateData();
        updateData.setOperate(OperateEnum.UPDATE);
        updateData.setField(LocationUpdateData.SupportUpdateField.LOCATION);
        updateData.setLocation(location);
        NetworkManager.sendToServer(new LocationUpdateMessage(updateData));
        this.currentUi = mainUi;
      }
    } else if (button.id == jumpButton.id) {
      LocationUpdateData updateData = new LocationUpdateData();
      updateData.setOperate(OperateEnum.JUMP);
      updateData.setField(LocationUpdateData.SupportUpdateField.LOCATION);
      updateData.setLocation(this.selectLocation);
      NetworkManager.sendToServer(new LocationUpdateMessage(updateData));
      this.container.getPlayer().closeScreen();
    } else if (button.id == deleteButton.id) {
      LocationUpdateData updateData = new LocationUpdateData();
      updateData.setOperate(OperateEnum.DELETE);
      updateData.setField(LocationUpdateData.SupportUpdateField.LOCATION);
      updateData.setLocation(this.selectLocation);
      NetworkManager.sendToServer(new LocationUpdateMessage(updateData));
      this.container.getPlayer().closeScreen();
    } else {
      String matchName = buttonList.actionPerformed(button.id);
      if (!StringUtils.isNullOrEmpty(matchName)) {
        // 初始化内容
        Location matchLocation =
            this.container
                .getLocationCapability()
                .getLocationByAlias(this.container.getPlayer().world, matchName);
        if (matchLocation == null) {
          return;
        }
        this.selectLocation = matchLocation;
        nameInputField.setText(matchLocation.getAlias());
        descInputField.setText(matchLocation.getDesc());
        GuiLabelHelper.setText(xLabel, String.format("X坐标:%s", matchLocation.getPosition().x));
        GuiLabelHelper.setText(yLabel, String.format("Y坐标:%s", matchLocation.getPosition().y));
        GuiLabelHelper.setText(zLabel, String.format("Z坐标:%s", matchLocation.getPosition().z));
        GuiLabelHelper.setText(
            distanceLabel,
            String.format(
                "距离:%s",
                matchLocation
                    .getPosition()
                    .distanceTo(this.container.getPlayer().getPositionVector())));
        this.currentUi = detailUi;
      }
    }
  }

  private void setPageControlButtonEnable() {
    int pageSize =
        this.container.getLocationCapability().locationPageSize(this.container.getPlayer().world);
    leftPageButton.setEnable(currentPageSize != 1);
    rightPageButton.setEnable(currentPageSize != pageSize);
    pageInputField.setPlaceholder(String.format("%s/%s", this.currentPageSize, pageSize));
  }

  private void setCurrentPageTexts() {
    buttonList.setTexts(
        this.container.getLocationCapability()
            .listSavedLocations(this.container.getPlayer().world, this.currentPageSize).stream()
            .map(Location::getAlias)
            .collect(Collectors.toList()));
  }
}
