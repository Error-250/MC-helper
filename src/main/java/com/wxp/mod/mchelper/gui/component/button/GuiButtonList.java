package com.wxp.mod.mchelper.gui.component.button;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/** @author wxp 一组按钮, 给定一个宽高,生成一组按钮. */
public class GuiButtonList {
  private int x;
  private int y;
  private int width;
  private int height;
  private int buttonSize;
  private int startId;
  private List<GuiButton> buttons;
  private List<String> texts;

  private boolean setTexture = false;
  private int textureX;
  private int textureY;
  private int textureWidth;
  private int textureHeight;
  private ResourceLocation buttonImage;

  private int buttonDefaultHeight = 17;

  public GuiButtonList(int x, int y, int width, int height, int buttonSize, int startId) {
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
    this.startId = startId;
    this.buttonSize = Math.min(buttonSize, getMaxButtonSize(height));
    buttons = new ArrayList<>(this.buttonSize);
  }

  public void setTexture(
      int textureX, int textureY, int textureWidth, int textureHeight, ResourceLocation texture) {
    setTexture = true;
    this.textureX = textureX;
    this.textureY = textureY;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
    this.buttonImage = texture;
  }

  public void initGui(int middleX, int middleY) {
    if (texts == null) {
      texts = Collections.emptyList();
    }
    Stream.iterate(0, (i -> i + 1))
        .limit(this.buttonSize)
        .forEach(
            num -> {
              String text = "";
              if (num < texts.size()) {
                text = texts.get(num);
              }
              if (setTexture) {
                GuiImageButton guiImageButton =
                    new GuiImageButton(
                        startId + num,
                        middleX + x,
                        middleY + y + (num * 17) + num,
                        width,
                        height / buttonSize,
                        text,
                        textureX,
                        textureY,
                        textureWidth,
                        textureHeight,
                        buttonImage);
                if (StringUtils.isNullOrEmpty(text)) {
                  guiImageButton.visible = false;
                }
                buttons.add(guiImageButton);
              } else {
                GuiButton guiButton =
                    new GuiButton(
                        startId + num,
                        middleX + x,
                        middleY + y + (num * 17) + num,
                        width,
                        height / buttonSize,
                        text);
                if (StringUtils.isNullOrEmpty(text)) {
                  guiButton.visible = false;
                }
                buttons.add(guiButton);
              }
            });
  }

  public List<GuiButton> getButtons() {
    return buttons;
  }

  public void setVisible(boolean visible) {
    if (visible) {
      setTexts(texts);
    } else {
      buttons.forEach(button -> button.visible = visible);
    }
  }

  public String actionPerformed(int buttonId) {
    GuiButton matchButton =
        buttons.stream().filter(button -> button.id == buttonId).findFirst().orElse(null);
    if (matchButton != null) {
      return texts.get(matchButton.id - startId);
    }
    return null;
  }

  public void setTexts(List<String> texts) {
    this.texts = texts;
    Stream.iterate(0, (i -> i + 1))
        .limit(buttonSize)
        .forEach(
            num -> {
              if (num < texts.size()) {
                buttons.get(num).displayString = texts.get(num);
                buttons.get(num).visible = true;
              } else {
                buttons.get(num).visible = false;
              }
            });
  }

  private int getMaxButtonSize(int height) {
    return height / buttonDefaultHeight;
  }
}
