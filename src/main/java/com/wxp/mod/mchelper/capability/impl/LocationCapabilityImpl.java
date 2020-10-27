package com.wxp.mod.mchelper.capability.impl;

import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.domain.Location;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

/** @author wxp */
public class LocationCapabilityImpl implements LocationCapability {
  private Map<String, List<Location>> positions = new HashMap<>();
  private int pageSize = 8;
  private boolean isAllowNearestLocation = false;
  private int limitDistance = 36;

  @Override
  public List<Location> listSavedLocations(World world, int page) {
    if (page <= 0) {
      page = 1;
    }
    return getSavedLocationInWorld(world).stream()
        .skip((page - 1) * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
  }

  @Override
  public int locationPageSize(World world) {
    List<Location> locations =
        positions.getOrDefault(
            String.valueOf(world.provider.getDimension()), Collections.emptyList());
    if (locations.size() % pageSize == 0) {
      return locations.size() / pageSize;
    }
    return locations.size() / pageSize + 1;
  }

  @Override
  public Location getLocationByAlias(World world, String alias) {
    return positions
        .getOrDefault(String.valueOf(world.provider.getDimension()), Collections.emptyList())
        .stream()
        .filter(location -> location.getAlias().equalsIgnoreCase(alias))
        .findAny()
        .orElse(null);
  }

  @Override
  public String saveLocation(World world, Location location) {
    if (world == null) {
      return null;
    }
    if (location == null) {
      return "Location is missing.";
    }
    if (StringUtils.isNullOrEmpty(location.getAlias())
        || location.getDesc() == null
        || location.getPosition() == null) {
      return "param is missing.";
    }
    Location matchLocation =
        getSavedLocationInWorld(world).stream()
            .filter(
                savedLocation ->
                    savedLocation.getAlias().equalsIgnoreCase(location.getAlias())
                        || isNearest(savedLocation, location))
            .findAny()
            .orElse(null);
    if (matchLocation == null) {
      if (!positions.containsKey(String.valueOf(world.provider.getDimension()))) {
        positions.put(String.valueOf(world.provider.getDimension()), new ArrayList<>());
      }
      positions.get(String.valueOf(world.provider.getDimension())).add(location);
      return null;
    } else {
      return String.format("location is nearest %s", matchLocation);
    }
  }

  @Override
  public boolean deleteLocationByAlias(World world, String alias) {
    return getSavedLocationInWorld(world)
        .removeIf(location -> location.getAlias().equalsIgnoreCase(alias));
  }

  @Override
  public void clearLocation(World world) {
    positions.remove(String.valueOf(world.provider.getDimension()));
  }

  @Override
  public Map<String, List<Location>> getPositionMap() {
    return positions;
  }

  @Override
  public void setPositionMap(Map<String, List<Location>> positionMap) {
    this.positions = positionMap;
  }

  @Override
  public void setAllowNearestLocation(boolean allowNearestLocation) {
    isAllowNearestLocation = allowNearestLocation;
  }

  @Override
  public boolean isAllowNearestLocation() {
    return isAllowNearestLocation;
  }

  @Override
  public void setLimitDistance(int distance) {
    limitDistance = distance;
  }

  @Override
  public int getLimitDistance() {
    return limitDistance;
  }

  @Override
  public void setPageSize(int size) {
    pageSize = size;
  }

  private List<Location> getSavedLocationInWorld(World world) {
    return positions.getOrDefault(
        String.valueOf(world.provider.getDimension()), Collections.emptyList());
  }

  private boolean isNearest(Location existed, Location saving) {
    if (isAllowNearestLocation) {
      return false;
    }
    return existed.getPosition().distanceTo(saving.getPosition()) < limitDistance;
  }
}
