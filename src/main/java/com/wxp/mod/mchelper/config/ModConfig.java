package com.wxp.mod.mchelper.config;

import net.minecraftforge.common.config.Config;

/** @author wxp 主要记录一些全局配置. */
@Config(modid = ModConfig.MOD_ID)
public class ModConfig {
  @Config.Ignore public static final String MOD_ID = "mc-helper";
  @Config.Ignore public static final String NAME = "Mc Helper";
  @Config.Ignore public static final String VERSION = "1.0";

  public static FunctionControl functionControl = new FunctionControl();

  public static class FunctionControl {
    @Config.Comment("是否啟用一鍵擼樹")
    public boolean enableAutoHarvestTree = false;

    @Config.Comment("是否啟用一鍵挖礦")
    public boolean enableAutoHarvestOre = false;

    @Config.Comment("是否啟用一鍵挖黑曜石")
    public boolean enableAutoHarvestObsidian = false;

    @Config.Comment("是否啟用位置功能")
    public boolean enableLocation = false;
  }
}
