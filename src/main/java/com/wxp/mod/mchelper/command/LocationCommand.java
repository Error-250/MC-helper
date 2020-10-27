package com.wxp.mod.mchelper.command;

import com.google.common.collect.Lists;
import com.wxp.mod.mchelper.capability.LocationCapability;
import com.wxp.mod.mchelper.domain.Location;
import com.wxp.mod.mchelper.helper.LocationHelper;
import com.wxp.mod.mchelper.manager.CapabilityManager;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** @author wxp 主要用于，游戏中系统命令。支持通过命令使用location. */
public class LocationCommand extends CommandBase {
  private static final String LIST_COMMAND_STR = "list";
  private static final String SAVE_COMMAND_STR = "save";
  private static final String JUMP_COMMAND_STR = "jump";
  private static final String DELETE_COMMAND_STR = "delete";

  @Override
  public String getName() {
    return "location";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "commands.location.usage";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    EntityPlayerMP entityPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);
    LocationCapability locationCapability =
        entityPlayerMP.getCapability(CapabilityManager.locationCapability, null);
    if (locationCapability == null) {
      entityPlayerMP.sendMessage(new TextComponentString("Command cannot us on this player."));
      return;
    }
    switch (args.length) {
      case 1:
        if (LIST_COMMAND_STR.equalsIgnoreCase(args[0])) {
          handleListCommand(entityPlayerMP, locationCapability, args);
          break;
        } else {
          throw new WrongUsageException("commands.location.usage");
        }
      case 2:
        if (LIST_COMMAND_STR.equalsIgnoreCase(args[0])) {
          handleListCommand(entityPlayerMP, locationCapability, args);
        } else if (SAVE_COMMAND_STR.equalsIgnoreCase(args[0])) {
          handleSaveCommand(entityPlayerMP, locationCapability, args);
          break;
        } else if (JUMP_COMMAND_STR.equalsIgnoreCase(args[0])) {
          handleJumpCommand(entityPlayerMP, locationCapability, args);
          break;
        } else if (DELETE_COMMAND_STR.equalsIgnoreCase(args[0])) {
          handleDeleteCommand(entityPlayerMP, locationCapability, args);
          break;
        } else {
          throw new WrongUsageException("commands.location.usage");
        }
      case 3:
        if (SAVE_COMMAND_STR.equalsIgnoreCase(args[0])) {
          handleSaveCommand(entityPlayerMP, locationCapability, args);
          break;
        } else {
          throw new WrongUsageException("commands.location.usage");
        }
      default:
        throw new WrongUsageException("commands.location.usage");
    }
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    EntityPlayerMP entityPlayerMP = null;
    try {
      entityPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);
    } catch (PlayerNotFoundException e) {
      return Collections.emptyList();
    }
    LocationCapability locationCapability =
        entityPlayerMP.getCapability(CapabilityManager.locationCapability, null);
    if (locationCapability == null) {
      return Collections.emptyList();
    }
    switch (args.length) {
      case 1:
        if (StringUtils.isNullOrEmpty(args[0])) {
          return Lists.newArrayList(
              LIST_COMMAND_STR, SAVE_COMMAND_STR, JUMP_COMMAND_STR, DELETE_COMMAND_STR);
        }
        return Lists.newArrayList(
                LIST_COMMAND_STR, SAVE_COMMAND_STR, JUMP_COMMAND_STR, DELETE_COMMAND_STR)
            .stream()
            .filter(str -> str.startsWith(args[0]))
            .collect(Collectors.toList());
      case 2:
        if (JUMP_COMMAND_STR.equalsIgnoreCase(args[0])
            || DELETE_COMMAND_STR.equalsIgnoreCase(args[0])) {
          if (StringUtils.isNullOrEmpty(args[1])) {
            return locationCapability.listSavedLocations(entityPlayerMP.world, 1).stream()
                .map(Location::getAlias)
                .collect(Collectors.toList());
          }
          return locationCapability.listSavedLocations(entityPlayerMP.world, 1).stream()
              .map(Location::getAlias)
              .filter(str -> str.startsWith(args[1]))
              .collect(Collectors.toList());
        }
      default:
    }
    return super.getTabCompletions(server, sender, args, targetPos);
  }

  private void handleListCommand(
      EntityPlayerMP entityPlayerMP, LocationCapability locationCapability, String[] args) {
    StringBuilder stringBuilder = new StringBuilder();
    int page = 1;
    if (args.length == 2) {
      page = Integer.parseInt(args[1]);
    }
    for (Location location : locationCapability.listSavedLocations(entityPlayerMP.world, page)) {
      stringBuilder.append(
          String.format(
              "%s: %s, %s\n", location.getAlias(), location.getPosition(), location.getDesc()));
    }
    stringBuilder.append(
        String.format(
            "All page:%s current is:%s",
            locationCapability.locationPageSize(entityPlayerMP.world), page));
    entityPlayerMP.sendMessage(new TextComponentString(stringBuilder.toString()));
  }

  private void handleSaveCommand(
      EntityPlayerMP entityPlayerMP, LocationCapability locationCapability, String[] args) {
    Location location = new Location();
    location.setAlias(args[1]);
    location.setPosition(entityPlayerMP.getPositionVector());
    if (args.length == 3) {
      location.setDesc(args[2]);
    } else {
      location.setDesc("");
    }
    LocationHelper.saveLocation(entityPlayerMP, locationCapability, location);
  }

  private void handleJumpCommand(
      EntityPlayerMP entityPlayerMP, LocationCapability locationCapability, String[] args) {
    LocationHelper.jumpToLocation(entityPlayerMP, locationCapability, args[0]);
  }

  private void handleDeleteCommand(
      EntityPlayerMP entityPlayer, LocationCapability locationCapability, String[] args) {
    LocationHelper.deleteLocation(entityPlayer, locationCapability, args[0]);
  }
}
