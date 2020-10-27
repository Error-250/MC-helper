package com.wxp.mod.mchelper.domain;

import lombok.Data;

/** @author wxp 主要用于客户端向服务端汇报location的变更数据. */
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
