package com.wxp.mod.mchelper.register;

import com.wxp.mod.mchelper.config.ModConfig;
import com.wxp.mod.mchelper.manager.ItemManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

/** @author wxp */
@Mod.EventBusSubscriber(modid = ModConfig.MOD_ID)
public class ItemRegister {
  @SubscribeEvent
  public static void registerItem(RegistryEvent.Register<Item> event) {
    for (Item item : ItemManager.getInitializedItem()) {
      event.getRegistry().register(item);
    }
    registerItemModel();
  }

  private static void registerItemModel() {
    if (Side.CLIENT.equals(FMLCommonHandler.instance().getSide())) {
      for (Item item : ItemManager.getInitializedItem()) {
        ModelLoader.setCustomModelResourceLocation(
            item,
            0,
            new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
      }
    }
  }
}
