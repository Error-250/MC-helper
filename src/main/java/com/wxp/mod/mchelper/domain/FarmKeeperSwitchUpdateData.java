package com.wxp.mod.mchelper.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wxp
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FarmKeeperSwitchUpdateData extends CommonBlockStateUpdateData {
  private boolean farmSwitch;
}
