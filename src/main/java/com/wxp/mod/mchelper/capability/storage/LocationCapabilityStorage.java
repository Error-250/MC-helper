package com.wxp.mod.mchelper.capability.storage;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.domain.Location;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wxp
 */
public class LocationCapabilityStorage implements Capability.IStorage<LocationCapability> {
  private static final String KEY_LOCATION_CAP = "location_saved";
  private static final String KEY_ALLOW_NEAREST_LOCATION = "allow_nearest_location";
  private static final String KEY_LIMIT_DISTANCE = "limit_distance";
  private static final String KEY_WORLD_TAG = "world";
  private static final String KEY_LOCATION_TAG = "locations";
  private static final String KEY_LOCATION_ALIAS = "alias";
  private static final String KEY_LOCATION_DESC = "desc";
  private static final String KEY_LOCATION_X = "x";
  private static final String KEY_LOCATION_Y = "y";
  private static final String KEY_LOCATION_Z = "z";

  @Nullable
  @Override
  public NBTBase writeNBT(Capability<LocationCapability> capability, LocationCapability instance, EnumFacing side) {
    NBTTagCompound base = new NBTTagCompound();
    NBTTagList mapList = new NBTTagList();
    for (Map.Entry<String, List<Location>> entry : instance.getPositionMap().entrySet()) {
      NBTTagCompound worldTag = new NBTTagCompound();
      worldTag.setString(KEY_WORLD_TAG, entry.getKey());
      NBTTagList list = new NBTTagList();
      for (Location location : entry.getValue()) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString(KEY_LOCATION_ALIAS, location.getAlias());
        compound.setString(KEY_LOCATION_DESC, location.getDesc());
        compound.setDouble(KEY_LOCATION_X, location.getPosition().x);
        compound.setDouble(KEY_LOCATION_Y, location.getPosition().y);
        compound.setDouble(KEY_LOCATION_Z, location.getPosition().z);
        list.appendTag(compound);
      }
      worldTag.setTag(KEY_LOCATION_TAG, list);
      mapList.appendTag(worldTag);
    }
    base.setTag(KEY_LOCATION_CAP, mapList);
    base.setBoolean(KEY_ALLOW_NEAREST_LOCATION, instance.isAllowNearestLocation());
    base.setInteger(KEY_LIMIT_DISTANCE, instance.getLimitDistance());
    return base;
  }

  @Override
  public void readNBT(Capability<LocationCapability> capability, LocationCapability instance, EnumFacing side, NBTBase nbt) {
    Map<String, List<Location>> locationMap = new HashMap<>();
    NBTTagCompound base = (NBTTagCompound) nbt;
    NBTTagList list = (NBTTagList) base.getTag(KEY_LOCATION_CAP);
    for (NBTBase sub : list) {
      NBTTagCompound worldTag = (NBTTagCompound) sub;
      String worldName = worldTag.getString(KEY_WORLD_TAG);
      NBTTagList locationList = (NBTTagList) worldTag.getTag(KEY_LOCATION_TAG);
      List<Location> locations = new ArrayList<>();
      for (NBTBase locationBase : locationList) {
        NBTTagCompound locationNbt = (NBTTagCompound) locationBase;
        Location location = new Location();
        location.setAlias(locationNbt.getString(KEY_LOCATION_ALIAS));
        location.setDesc(locationNbt.getString(KEY_LOCATION_DESC));
        location.setPosition(new Vec3d(locationNbt.getDouble(KEY_LOCATION_X), locationNbt.getDouble(KEY_LOCATION_Y), locationNbt.getDouble(KEY_LOCATION_Z)));
        locations.add(location);
      }
      locationMap.put(worldName, locations);
    }
    instance.setPositionMap(locationMap);
    instance.setAllowNearestLocation(base.getBoolean(KEY_ALLOW_NEAREST_LOCATION));
    instance.setLimitDistance(base.getInteger(KEY_LIMIT_DISTANCE));
  }
}
