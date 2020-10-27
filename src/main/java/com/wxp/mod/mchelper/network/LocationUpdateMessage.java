package com.wxp.mod.mchelper.network;

import com.wxp.mod.mchelper.domain.Location;
import com.wxp.mod.mchelper.domain.LocationUpdateData;
import com.wxp.mod.mchelper.domain.OperateEnum;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

/** @author wxp */
@NoArgsConstructor
public class LocationUpdateMessage extends AbstractNbtMessage {
  public LocationUpdateMessage(LocationUpdateData updateData) {
    this.nbt = serialize(updateData);
    this.isReady = true;
  }

  public LocationUpdateData getUpdateDate() {
    return deserialize(this.nbt);
  }

  private NBTTagCompound serialize(LocationUpdateData updateData) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("operate", updateData.getOperate().name());
    tag.setString("field", updateData.getField().name());
    switch (updateData.getField()) {
      case ALLOW_NEAREST:
        tag.setBoolean("allowNearest", updateData.getAllowNearest());
        break;
      case LOCATION:
        tag.setString("name", updateData.getLocation().getAlias());
        tag.setString("desc", updateData.getLocation().getDesc());
        if (updateData.getLocation().getPosition() != null) {
          tag.setDouble("x", updateData.getLocation().getPosition().x);
          tag.setDouble("y", updateData.getLocation().getPosition().y);
          tag.setDouble("z", updateData.getLocation().getPosition().z);
        }
        break;
      default:
    }
    return tag;
  }

  private LocationUpdateData deserialize(NBTTagCompound tag) {
    LocationUpdateData updateData = new LocationUpdateData();
    updateData.setOperate(OperateEnum.valueOf(tag.getString("operate")));
    updateData.setField(LocationUpdateData.SupportUpdateField.valueOf(tag.getString("field")));
    switch (updateData.getField()) {
      case ALLOW_NEAREST:
        updateData.setAllowNearest(tag.getBoolean("allowNearest"));
        break;
      case LOCATION:
        Location location = new Location();
        location.setAlias(tag.getString("name"));
        location.setDesc(tag.getString("desc"));
        Vec3d vec3d = new Vec3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        location.setPosition(vec3d);
        updateData.setLocation(location);
        break;
      default:
    }
    return updateData;
  }
}
