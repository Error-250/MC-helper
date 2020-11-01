package com.wxp.mod.mchelper.manager;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author wxp */
public class ItemManager {
  public static List<Item> items = new ArrayList<>();

  public static void initItem() {
    BlockManager.getInitializedBlock()
        .forEach(
            commonBlockI -> {
              if (commonBlockI.getItemBlock() != null) {
                items.add(commonBlockI.getItemBlock());
              }
            });
  }

  public static Collection<Item> getInitializedItem() {
    return items;
  }
}
