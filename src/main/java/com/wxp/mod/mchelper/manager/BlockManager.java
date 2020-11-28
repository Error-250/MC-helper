package com.wxp.mod.mchelper.manager;

import com.wxp.mod.mchelper.block.CommonBlock;
import com.wxp.mod.mchelper.block.EnchantmentBlock;
import com.wxp.mod.mchelper.block.FarmKeeperBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author wxp */
public class BlockManager {
  public static FarmKeeperBlock farmKeeperBlock = new FarmKeeperBlock();
  public static EnchantmentBlock enchantmentBlock = new EnchantmentBlock();
  public static List<CommonBlock> commonBlocks = new ArrayList<>();

  public static void initBlock() {
    commonBlocks.add(farmKeeperBlock);
    commonBlocks.add(enchantmentBlock);
  }

  public static Collection<CommonBlock> getInitializedBlock() {
    return commonBlocks;
  }
}
