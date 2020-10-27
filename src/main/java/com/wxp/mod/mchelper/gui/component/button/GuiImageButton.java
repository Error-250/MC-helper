package com.wxp.mod.mchelper.gui.component.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/** @author wxp 自定义图片按钮,支持素材缩放.以及各种素材变化 */
public class GuiImageButton extends GuiButton {
  private ResourceLocation background;
  private int enableTextureX;
  private int enableTextureY;
  private int hoverTextureX;
  private int hoverTextureY;
  private int disEnableTextureX;
  private int disEnableTextureY;
  private int textureWidth;
  private int textureHeight;

  public GuiImageButton(
      int buttonId,
      int x,
      int y,
      int with,
      int height,
      int textureX,
      int textureY,
      ResourceLocation backgroundImage) {
    this(buttonId, x, y, with, height, "", textureX, textureY, with, height, backgroundImage);
  }

  public GuiImageButton(
      int buttonId,
      int x,
      int y,
      int with,
      int height,
      String text,
      int textureX,
      int textureY,
      int textureWidth,
      int textureHeight,
      ResourceLocation backgroundImage) {
    super(buttonId, x, y, with, height, text);
    this.width = with;
    this.height = height;
    this.background = backgroundImage;
    this.enableTextureX = textureX;
    this.enableTextureY = textureY;
    this.hoverTextureX = textureX;
    this.hoverTextureY = textureY;
    this.disEnableTextureX = textureX;
    this.disEnableTextureY = textureY;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
  }

  public void setEnable(boolean enable) {
    this.enabled = enable;
  }

  public void setDisEnableTextureOffset(int x, int y) {
    this.disEnableTextureX = x;
    this.disEnableTextureY = y;
  }

  public void setHoverTextureOffset(int x, int y) {
    this.hoverTextureX = x;
    this.hoverTextureY = y;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    if (this.visible) {
      GlStateManager.color(1.0F, 1.0F, 1.0F);

      mc.getTextureManager().bindTexture(background);
      this.hovered =
          mouseX >= this.x
              && mouseY >= this.y
              && mouseX < this.x + this.width
              && mouseY < this.y + this.height;

      float scaleX = (float) (this.width * 1.0 / this.textureWidth);
      float scaleY = (float) (this.height * 1.0 / this.textureHeight);
      if (this.enabled && this.hovered) {
        Gui.drawModalRectWithCustomSizedTexture(
            this.x,
            this.y,
            hoverTextureX,
            hoverTextureY,
            this.width,
            this.height,
            256 * scaleX,
            256 * scaleY);
      } else if (this.enabled) {
        Gui.drawScaledCustomSizeModalRect(
            this.x,
            this.y,
            enableTextureX,
            enableTextureY,
            this.width,
            this.height,
            this.width,
            this.height,
            256 * scaleX,
            256 * scaleY);
      } else {
        Gui.drawScaledCustomSizeModalRect(
            this.x,
            this.y,
            disEnableTextureX,
            disEnableTextureY,
            this.width,
            this.height,
            this.width,
            this.height,
            256 * scaleX,
            256 * scaleY);
      }
      // 14737632 = e0e0e0
      this.drawCenteredString(
          mc.fontRenderer,
          this.displayString,
          this.x + this.width / 2,
          this.y + (this.height - 8) / 2,
          14737632);
    }
  }
}
