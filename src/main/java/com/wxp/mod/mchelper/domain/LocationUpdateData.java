package com.wxp.mod.mchelper.domain;

import lombok.Data;

/**
 * @author wxp
 */
@Data
public class LocationUpdateData {
  private OperateEnum operate;
  private SupportUpdateField field;
  private Boolean allowNearest;
  private Location location;

  public enum SupportUpdateField {
    ALLOW_NEAREST,
    LOCATION
  }
}
