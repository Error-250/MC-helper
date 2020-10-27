package com.wxp.mod.mchelper.capability;

import com.wxp.mod.mchelper.domain.Location;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

/** @author wxp */
public interface LocationCapability {
  /**
   * 列出储存的位置信息
   *
   * @param world 世界
   * @param page 第几页
   * @return 位置信息列表
   */
  List<Location> listSavedLocations(World world, int page);

  /**
   * 获取在该世界存储的位置信息总共的页数
   *
   * @param world 世界
   * @return 页数
   */
  int locationPageSize(World world);

  /**
   * 通过别名查询位置
   *
   * @param world 世界
   * @param alias 别名
   * @return 位置信息
   */
  Location getLocationByAlias(World world, String alias);

  /**
   * 保存位置信息
   *
   * @param world 世界
   * @param location 位置信息
   * @return 错误信息
   */
  String saveLocation(World world, Location location);

  /**
   * 通过别名删除保存的位置信息
   *
   * @param world 世界
   * @param alias 别名
   * @return 是否删除
   */
  boolean deleteLocationByAlias(World world, String alias);

  /**
   * 清理保存的位置信息
   *
   * @param world 世界
   */
  void clearLocation(World world);

  /**
   * 获取完整的map
   *
   * @return 完整的map
   */
  Map<String, List<Location>> getPositionMap();

  /**
   * 设置完整的map
   *
   * @param positionMap 完整map
   */
  void setPositionMap(Map<String, List<Location>> positionMap);

  /**
   * 设置是否允许保存与已存坐标较近的坐标
   *
   * @param allowNearestLocation 是否
   */
  void setAllowNearestLocation(boolean allowNearestLocation);

  /**
   * 是否允许保存与已存坐标较近的坐标
   *
   * @return 是否
   */
  boolean isAllowNearestLocation();

  /**
   * 设置距离多远认为是较近的距离, > 0
   *
   * @param distance 距离
   */
  void setLimitDistance(int distance);

  /**
   * 获取距离多远认为是较近的距离
   *
   * @return 距离
   */
  int getLimitDistance();

  /**
   * 设置页大小
   *
   * @param size 页大小
   */
  void setPageSize(int size);
}
