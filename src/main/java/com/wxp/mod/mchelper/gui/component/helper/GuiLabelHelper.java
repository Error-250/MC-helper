package com.wxp.mod.mchelper.gui.component.helper;

import com.wxp.mod.mchelper.McHelper;
import net.minecraft.client.gui.GuiLabel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/** @author wxp */
public class GuiLabelHelper {

  public static void setText(GuiLabel label, String text) {
    try {
      Field field =
          Arrays.stream(GuiLabel.class.getDeclaredFields())
              .filter(findField -> findField.getType().isAssignableFrom(List.class))
              .findFirst()
              .orElse(null);
      if (field == null) {
        return;
      }
      field.setAccessible(true);
      List<String> labels = (List<String>) field.get(label);
      labels.clear();
      labels.add(text);
    } catch (IllegalAccessException e) {
      McHelper.getLogger().warn("Set label fail ", e);
    }
  }
}
