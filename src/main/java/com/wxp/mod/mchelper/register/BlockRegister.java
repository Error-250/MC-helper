package com.wxp.mod.mchelper.register;

import com.wxp.mod.mchelper.block.CommonBlock;
import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.manager.BlockManager;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/** @author wxp */
@Mod.EventBusSubscriber(modid = ModConfig.MOD_ID)
public class BlockRegister {
  @SubscribeEvent
  public static void registerBlock(RegistryEvent.Register<Block> event) {
    for (CommonBlock commonBlock : BlockManager.getInitializedBlock()) {
      event.getRegistry().register(commonBlock);
      if (commonBlock.hasTileEntity(null)) {
        GameRegistry.registerTileEntity(
            commonBlock.getTileEntityClass(),
            new ResourceLocation(ModConfig.MOD_ID, commonBlock.getTileEntityClass().getName()));
      }
    }
  }
}
