package com.wxp.mod.mchelper.manager;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** @author wxp */
public class FakePlayerManager {
  private static final String namePrefix = "mc-helper";
  private static final String uuidPrefix = "mc-helper fake player";
  private static Map<String, WeakReference<EntityPlayerMP>> fakePlayerMap;

  public static void initFakePlayer() {
    fakePlayerMap = new HashMap<>(1);
  }

  public static WeakReference<EntityPlayerMP> getFakePlayer(WorldServer server, String name) {
    String actualName = String.format("%s-%s", namePrefix, name);
    if (fakePlayerMap.containsKey(actualName)) {
      fakePlayerMap.get(actualName).get().setWorld(server);
      return fakePlayerMap.get(actualName);
    } else {
      GameProfile gameProfile =
          new GameProfile(
              UUID.nameUUIDFromBytes(String.format("%s-%s", uuidPrefix, actualName).getBytes()),
              actualName);
      WeakReference<EntityPlayerMP> fakePlayer =
          new WeakReference<>(FakePlayerFactory.get(server, gameProfile));
      fakePlayerMap.put(actualName, fakePlayer);
      return fakePlayer;
    }
  }
}
