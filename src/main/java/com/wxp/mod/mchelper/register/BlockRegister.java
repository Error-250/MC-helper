package com.wxp.mod.mchelper.register;

import com.wxp.mod.mchelper.block.CommonBlockI;
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
    for (CommonBlockI commonBlockI : BlockManager.getInitializedBlock()) {
      event.getRegistry().register(commonBlockI.getSelf());
      if (commonBlockI.hasTileEntity(null)) {
        GameRegistry.registerTileEntity(
            commonBlockI.getTileEntityClass(),
            new ResourceLocation(ModConfig.MOD_ID, commonBlockI.getTileEntityClass().getName()));
      }
    }
  }
}
