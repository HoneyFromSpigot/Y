package io.github.thewebcode.yplugin.player;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.location.PreTeleportLocation;
import io.github.thewebcode.yplugin.location.PreTeleportType;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.Skip;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MinecraftPlayer extends User {
    private static YPlugin yPlugin = YPlugin.getInstance();

    @Path("last-online")
    private long lastOnline = 0L;

    @Path("is-premium")
    private boolean isPremium = false;

    @Path("debug-mode")
    private boolean debugMode = false;

    @Path("hiding-other-players")
    private boolean hidingOtherPlayers = false;

    @Skip
    private boolean viewingRecipe = false;

    @Path("walk-speed")
    private double walkSpeed = 0.22;

    @Path("fly-speed")
    private double flySpeed = 0.1;

    @Skip
    public static final double DEFAULT_WALK_SPEED = 0.22;

    @Skip
    public static final double DEFAULT_FLY_SPEED = 0.1;

    @Path("god-mode")
    private boolean godMode = false;

    @Skip
    private boolean forceRespawn = false;

    private long reloadEnd = 0;

    private PreTeleportLocation preTeleportLocation;

    private ItemStack equippedArrow;

    private TeleportRequest teleportRequest;

    @Deprecated
    public MinecraftPlayer(String playerName) {
        setName(playerName);
        initWrapper();
    }

    public MinecraftPlayer(UUID id) {
        setId(id);
        setName(Players.getPlayer(id).getName());
        initWrapper();
    }

    public void dispose() {
    }

    private void initWrapper() {
        lastOnline = System.currentTimeMillis();
        setId(Players.getPlayer(getName()).getUniqueId());
    }

    public boolean hasCustomWalkSpeed() {
        return walkSpeed != DEFAULT_WALK_SPEED;
    }

    public boolean hasCustomFlySpeed() {
        return flySpeed != DEFAULT_FLY_SPEED;
    }

    public double getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(double walkSpeed) {
        this.walkSpeed = walkSpeed;
        getPlayer().setWalkSpeed((float) walkSpeed);
    }

    public double getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(double flySpeed) {
        this.flySpeed = flySpeed;
        getPlayer().setFlySpeed((float) flySpeed);
    }

    public PreTeleportLocation getPreTeleportLocation() {
        return preTeleportLocation;
    }

    public void setPreTeleportLocation(Location loc, PreTeleportType teleportType) {
        this.preTeleportLocation = new PreTeleportLocation(loc, teleportType);
    }

    public boolean isInDebugMode() {
        return debugMode;
    }

    public void setInDebugMode(boolean value) {
        debugMode = value;
    }


    public boolean isHidingOtherPlayers() {
        return hidingOtherPlayers;
    }

    public void setHidingOtherPlayers(boolean hidingOtherPlayers) {
        this.hidingOtherPlayers = hidingOtherPlayers;
    }

    public boolean isReloading() {
        return reloadEnd > System.currentTimeMillis();
    }

    public void setReloading(int durationSeconds) {
        this.reloadEnd = System.currentTimeMillis() + TimeHandler.getTimeInMilles(durationSeconds, TimeType.SECOND);
    }

    public boolean hasGodMode() {
        return godMode;
    }

    public void setGodMode(boolean godMode) {
        this.godMode = godMode;
    }

    public void setForceRespawn(boolean force) {
        this.forceRespawn = force;
    }

    public boolean hasForceRespawn() {
        return this.forceRespawn;
    }

    public void setTeleportRequest(TeleportRequest request) {
        this.teleportRequest = request;
        yPlugin.debug("Teleport request for " + getName() + " has been set; Requester = " + request.requesterName);
    }

    public void requestTeleportTo(Player target) {
        MinecraftPlayer mcTarget = YPlugin.getInstance().getPlayerHandler().getData(target);
        Player player = getPlayer();

        mcTarget.setTeleportRequest(new TeleportRequest(TeleportRequest.TeleportRequestType.TELEPORT_TO, player, target));
        Chat.message(player, "&eYour teleport request was sent to &e" + target.getName());
        Chat.message(target, "&eYou've received a &6teleport request &efrom &a" + player.getName() + " &eto teleport to you.", "&aAccept &eor &cdeny&e the request with &a/tpaccept &eor &c/tpdeny");
    }

    public void requestTeleportHere(Player target) {
        MinecraftPlayer mcTarget = YPlugin.getInstance().getPlayerHandler().getData(target);
        Player player = getPlayer();

        mcTarget.setTeleportRequest(new TeleportRequest(TeleportRequest.TeleportRequestType.TELEPORT_HERE, player, target));
        Chat.message(player, "&eYour teleport request was sent to &e" + target.getName());
        Chat.message(target, "&eYou've received a &6teleport request &efrom &a" + player.getName() + " &efor you to teleport to them.", "&aAccept &eor &cdeny &ethe request with &a/tpaccept &eor &c/tpdeny");
    }

    public boolean hasTeleportRequest() {
        if (teleportRequest == null) {
            return false;
        }

        return !teleportRequest.hasExpired();
    }

    public TeleportRequest getTeleportRequest() {
        return teleportRequest;
    }

    public void acceptTeleport() {
        teleportRequest.accept(getPlayer());
        teleportRequest = null;
    }

    public void denyTeleport() {
        teleportRequest.deny(getPlayer());
        teleportRequest = null;
    }

    @ToString(of = {"filled", "requesterName", "requestedName", "requester", "receiver", "type"})
    public static class TeleportRequest {

        private static enum TeleportRequestType {
            TELEPORT_TO,
            TELEPORT_HERE
        }

        public static final boolean ONLY_REQUESTED_CAN_ACCEPT = true;
        private static final long TIME_UNTIL_EXPIRY = TimeHandler.getTimeInMilles(30, TimeType.SECOND);

        private long expire;
        private boolean filled = false;

        private UUID requester;
        private UUID receiver;

        public final String requesterName;

        public final String requestedName;
        private TeleportRequestType type;

        public TeleportRequest(TeleportRequestType type, Player playerRequesting, Player playerReceiving) {
            this.type = type;
            expire = Long.sum(System.currentTimeMillis(), TIME_UNTIL_EXPIRY);
            this.requester = playerRequesting.getUniqueId();
            this.receiver = playerReceiving.getUniqueId();
            requestedName = playerReceiving.getName();
            requesterName = playerRequesting.getName();

            yPlugin.debug(this.toString());
        }

        public void accept(Player accepting) {
            switch (type) {
                case TELEPORT_TO:
                    Player toRequested = Players.getPlayer(requester);
                    Players.teleport(toRequested, accepting);
                    Chat.message(toRequested, Messages.playerTeleportedToPlayer(toRequested.getName()));
                    Chat.message(accepting, Messages.playerTeleportedToYou(accepting.getName()));
                    filled = true;
                    break;
                case TELEPORT_HERE:
                    Player hereRequested = Players.getPlayer(requester);
                    Players.teleport(accepting, hereRequested);
                    Chat.message(hereRequested, Messages.playerTeleportedToPlayer(accepting.getName()));
                    Chat.message(accepting, Messages.playerTeleportedToYou(hereRequested.getName()));
                    filled = true;
                    break;
            }
        }

        public void deny(Player denier) {
            Player sender = Players.getPlayer(requester);
            Chat.message(denier, String.format("&cYou denied the teleport request from &e%s", sender.getName()));
            Chat.message(sender, String.format("&e%s&c denied your teleport request", denier.getName()));
            filled = true;
        }

        public boolean hasExpired() {
            if (filled) {
                return true;
            }

            return System.currentTimeMillis() > expire;
        }

    }
}
