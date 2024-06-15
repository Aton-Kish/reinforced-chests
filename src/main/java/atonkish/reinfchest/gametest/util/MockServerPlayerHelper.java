package atonkish.reinfchest.gametest.util;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.authlib.GameProfile;

import io.netty.channel.embedded.EmbeddedChannel;

public class MockServerPlayerHelper {
    private static AtomicInteger playerId = new AtomicInteger(1);

    public static ServerPlayerEntity spawn(TestContext context, GameMode gameMode, Vec3d pos) {
        ConnectedClientData data = ConnectedClientData
                .createDefault(
                        new GameProfile(UUID.randomUUID(), String.format("player-%d", playerId.getAndIncrement())),
                        false);
        ServerPlayerEntity player = new ServerPlayerEntity(context.getWorld().getServer(), context.getWorld(),
                data.gameProfile(), data.syncedOptions());
        ClientConnection connection = new ClientConnection(NetworkSide.SERVERBOUND);
        new EmbeddedChannel(connection);
        context.getWorld().getServer().getPlayerManager().onPlayerConnect(connection, player, data);

        player.changeGameMode(gameMode);
        player.setPosition(context.getAbsolute(pos));
        player.setOnGround(true);

        return player;
    }

    public static void destroy(TestContext context, ServerPlayerEntity player) {
        player.discard();
        player.getInventory().clear();
        player.networkHandler.disconnect(Text.of(String.format("%s (%s) left the game",
                player.getGameProfile().getName(), player.getUuidAsString())));
    }
}
