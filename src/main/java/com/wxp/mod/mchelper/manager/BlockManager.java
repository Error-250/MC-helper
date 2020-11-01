package com.wxp.mod.mchelper.manager;

import com.wxp.mod.mchelper.block.CommonBlockI;
import com.wxp.mod.mchelper.block.FarmKeeperBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author wxp */
public class BlockManager {
  public static FarmKeeperBlock farmKeeperBlock = new FarmKeeperBlock();
  public static List<CommonBlockI> commonBlocks = new ArrayList<>();

  public static void initBlock() {
    commonBlocks.add(farmKeeperBlock);
  }

  public static Collection<CommonBlockI> getInitializedBlock() {
    return commonBlocks;
  }
}
