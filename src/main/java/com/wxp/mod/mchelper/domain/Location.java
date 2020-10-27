package com.wxp.mod.mchelper.domain;

import lombok.Data;
import net.minecraft.util.math.Vec3d;

/** @author wxp location */
@Data
public class Location {
  private String alias;
  private String desc;
  private Vec3d position;
}
