package sonar.fluxnetworks.common.util;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.access.FluxPlayer;
import sonar.fluxnetworks.register.Messages;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Collection;

public class FluxCommands {

    public static void register(@Nonnull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(FluxNetworks.MODID).requires(s -> s.hasPermission(2))
                .then(Commands.literal("superadmin").requires(s -> s.hasPermission(FluxConfig.superAdminRequiredPermission))
                        .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                                .then(Commands.argument("enable", BoolArgumentType.bool())
                                        .executes(s -> superAdmin(s.getSource(),
                                                GameProfileArgument.getGameProfiles(s, "targets"),
                                                BoolArgumentType.getBool(s, "enable"))
                                        )
                                )
                        )
                ).then(Commands.literal("dump-config").requires(s -> s.hasPermission(2))
                        .executes(s -> {
                            s.getSource().sendSystemMessage(Component.literal("FluxConfig DUMP"));
                            s.getSource().sendSystemMessage(Component.literal("--------BEGIN--------"));
                            for (Field f : FluxConfig.class.getFields()) {
                                try {
                                    s.getSource().sendSystemMessage(Component.literal(f.getType() + " " + f.getName() + " " + f.get(null).toString()));
                                } catch (IllegalAccessException e) {
                                    s.getSource().sendSystemMessage(Component.literal(f.getType() + " " + f.getName() + " ERROR " + e.getMessage()));
                                }
                            }
                            s.getSource().sendSystemMessage(Component.literal("---------END---------"));
                            return 1;
                        })
                )
        );
    }

    private static int superAdmin(@Nonnull CommandSourceStack source,
                                  @Nonnull Collection<GameProfile> profiles, boolean enable) {
        PlayerList playerList = source.getServer().getPlayerList();
        int success = 0;

        for (GameProfile profile : profiles) {
            ServerPlayer player = playerList.getPlayer(profile.getId());
            if (player != null) {
                final FluxPlayer fp = FluxUtils.getFluxPlayer(player);
                if (fp != null &&
                        (((fp.isSuperAdmin() || FluxConfig.enableSuperAdmin) && source.hasPermission(3)) ||
                                (player == source.getEntity() && (fp.isSuperAdmin() || FluxPlayer.canActivateSuperAdmin(player)))) &&
                        fp.setSuperAdmin(enable)) {
                    Messages.syncCapability(player);
                    player.sendSystemMessage(Component.translatable(enable ?
                            "gui.fluxnetworks.superadmin.on" : "gui.fluxnetworks.superadmin.off"));
                    success++;
                }
            }
        }

        return success;
    }
}
