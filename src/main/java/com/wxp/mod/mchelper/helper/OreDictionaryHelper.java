package com.wxp.mod.mchelper.helper;

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

/** @author wxp 矿物字典辅助 */
public class OreDictionaryHelper {
  private static final String WOOD = "logWood";
  private static final String LEAVES = "treeLeaves";
  private static final String ORE_GOLD = "oreGold";
  private static final String ORE_IRON = "oreIron";
  private static final String ORE_LAPIS = "oreLapis";
  private static final String ORE_DIAMOND = "oreDiamond";
  private static final String ORE_RED_STONE = "oreRedstone";
  private static final String ORE_EMERALD = "oreEmerald";
  private static final String ORE_COAL = "oreCoal";

  public static boolean isTree(Block block) {
    return containsBlock(WOOD, block) || containsBlock(LEAVES, block);
  }

  public static boolean isOriginWood(Block block) {
    return containsBlock(WOOD, block);
  }

  public static boolean isOre(Block block) {
    return containsBlock(ORE_GOLD, block)
        || containsBlock(ORE_IRON, block)
        || containsBlock(ORE_LAPIS, block)
        || containsBlock(ORE_DIAMOND, block)
        || containsBlock(ORE_RED_STONE, block)
        || containsBlock(ORE_EMERALD, block)
        || containsBlock(ORE_COAL, block);
  }

  private static boolean containsBlock(String indexName, Block block) {
    return OreDictionary.getOres(indexName).stream()
        .anyMatch(itemStack -> Block.getBlockFromItem(itemStack.getItem()).equals(block));
  }
}
