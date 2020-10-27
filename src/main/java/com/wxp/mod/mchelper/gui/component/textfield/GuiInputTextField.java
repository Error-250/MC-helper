package com.wxp.mod.mchelper.gui.component.textfield;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

/** @author wxp 自定义输入框, 支持placeholder */
public class GuiInputTextField extends GuiTextField {
  private String placeholder = "";
  private String defaultText = "";
  private int backgroundColor;
  private int borderColor;

  public GuiInputTextField(
      int componentId,
      FontRenderer fontRendererObj,
      int x,
      int y,
      int componentWidth,
      int componentHeight) {
    super(componentId, fontRendererObj, x, y, componentWidth, componentHeight);
  }

  @Override
  public void drawTextBox() {
    super.drawTextBox();
    if (this.getEnableBackgroundDrawing()) {
      drawRect(
          this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, borderColor);
      drawRect(this.x, this.y, this.x + this.width, this.y + this.height, backgroundColor);
    }
  }

  @Override
  public void setFocused(boolean isFocusedIn) {
    super.setFocused(isFocusedIn);
    if (isFocusedIn) {
      if (!"".equalsIgnoreCase(placeholder) && super.getText().equalsIgnoreCase(placeholder)) {
        setText("");
      }
    } else {
      if ("".equalsIgnoreCase(getText())) {
        setText(placeholder);
      }
    }
  }

  @Override
  public String getText() {
    if (!"".equalsIgnoreCase(placeholder) && super.getText().equalsIgnoreCase(placeholder)) {
      return defaultText;
    }
    return super.getText();
  }

  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void setBorderColor(int borderColor) {
    this.borderColor = borderColor;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
    if (!isFocused()) {
      setText(placeholder);
    }
  }

  public void setDefaultText(String defaultText) {
    this.defaultText = defaultText;
  }
}
