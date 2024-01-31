package sonar.fluxnetworks.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FluxTranslate {
    public static final int SPLIT_LENGTH = 200;

    public static final FluxTranslate
            FLUX_DUST_TOOLTIP = new FluxTranslate("tooltip.fluxnetworks.flux_dust"),
            FLUX_CONTROLLER_TOOLTIP = new FluxTranslate("tooltip.fluxnetworks.flux_controller"),
            FLUX_PLUG_TOOLTIP = new FluxTranslate("tooltip.fluxnetworks.flux_plug"),
            FLUX_POINT_TOOLTIP = new FluxTranslate("tooltip.fluxnetworks.flux_point"),
            FLUX_STORAGE_TOOLTIP = new FluxTranslate("tooltip.fluxnetworks.flux_storage"),
            FLUX_STORAGE_TOOLTIP_2 = new FluxTranslate("tooltip.fluxnetworks.flux_storage_2");

    public static final FluxTranslate
            TAB_HOME = new FluxTranslate("gui.fluxnetworks.tab.home"),
            TAB_SELECTION = new FluxTranslate("gui.fluxnetworks.tab.selection"),
            TAB_WIRELESS = new FluxTranslate("gui.fluxnetworks.tab.wireless"),
            TAB_CONNECTION = new FluxTranslate("gui.fluxnetworks.tab.connection"),
            TAB_STATISTICS = new FluxTranslate("gui.fluxnetworks.tab.statistics"),
            TAB_MEMBER = new FluxTranslate("gui.fluxnetworks.tab.member"),
            TAB_SETTING = new FluxTranslate("gui.fluxnetworks.tab.setting"),
            TAB_CREATE = new FluxTranslate("gui.fluxnetworks.tab.create");

    public static final FluxTranslate
            NETWORK_NAME = new FluxTranslate("gui.fluxnetworks.network.name"),
            NETWORK_FULL_NAME = new FluxTranslate("gui.fluxnetworks.network.fullname"),
            NETWORK_SECURITY = new FluxTranslate("gui.fluxnetworks.network.security"),
            NETWORK_PASSWORD = new FluxTranslate("gui.fluxnetworks.network.password"),
            NETWORK_ENERGY = new FluxTranslate("gui.fluxnetworks.network.energy"),
            NETWORK_COLOR = new FluxTranslate("gui.fluxnetworks.network.color");

    public static final FluxTranslate
            ERROR_NO_SELECTED = new FluxTranslate("gui.fluxnetworks.error.noselected"),
            ERROR_NO_NETWORK = new FluxTranslate("gui.fluxnetworks.error.nonetwork");

    public static final FluxTranslate
            NAME = new FluxTranslate("gui.fluxnetworks.flux.name"),
            PRIORITY = new FluxTranslate("gui.fluxnetworks.flux.priority"),
            SURGE = new FluxTranslate("gui.fluxnetworks.flux.surge"),
            SURGE_MODE = new FluxTranslate("gui.fluxnetworks.flux.surgemode"),
            TRANSFER_LIMIT = new FluxTranslate("gui.fluxnetworks.flux.transferlimit"),
            DISABLE_LIMIT = new FluxTranslate("gui.fluxnetworks.flux.disablelimit"),
            CHUNK_LOADING = new FluxTranslate("gui.fluxnetworks.flux.chunkloading"),
            ENABLE_WIRELESS = new FluxTranslate("gui.fluxnetworks.flux.wireless"),
            WIRELESS_CHARGING_ON = new FluxTranslate("gui.fluxnetworks.flux.wirelessnetwork"),
            WIRELESS_CHARGING_OFF = new FluxTranslate("gui.fluxnetworks.flux.nonwirelessnetwork"),
            WIRELESS_CHARGING_NO_CONTROLLER = new FluxTranslate("gui.fluxnetworks.flux.wirelessnetwork.nocontroller"),
            ENERGY = new FluxTranslate("gui.fluxnetworks.flux.energy"),
            ENERGY_STORED = new FluxTranslate("gui.fluxnetworks.flux.energystored"),
            BUFFER = new FluxTranslate("gui.fluxnetworks.flux.buffer"),
            INTERNAL_BUFFER = new FluxTranslate("gui.fluxnetworks.flux.internalbuffer"),
            UNLIMITED = new FluxTranslate("gui.fluxnetworks.flux.unlimited"),
            FORCED_LOADING = new FluxTranslate("gui.fluxnetworks.flux.forcedloading"),
            CHUNK_UNLOADED = new FluxTranslate("gui.fluxnetworks.flux.chunkunloaded"),
            INPUT = new FluxTranslate("gui.fluxnetworks.flux.input"),
            OUTPUT = new FluxTranslate("gui.fluxnetworks.flux.output"),
            CHANGE = new FluxTranslate("gui.fluxnetworks.flux.change"),
            AVERAGE_TICK = new FluxTranslate("gui.fluxnetworks.flux.averagetick");

    public static final FluxTranslate
            SORT_BY = new FluxTranslate("gui.fluxnetworks.label.sortby"),
            SELECTED = new FluxTranslate("gui.fluxnetworks.label.selected"),
            SINGLE_EDIT = new FluxTranslate("gui.fluxnetworks.label.singleedit"),
            BATCH_EDIT = new FluxTranslate("gui.fluxnetworks.label.batchedit"),
            EDITING_CONNECTIONS = new FluxTranslate("gui.fluxnetworks.label.editingconnections"),
            CONNECTIONS = new FluxTranslate("gui.fluxnetworks.label.connections"),
            CUSTOM_COLOR = new FluxTranslate("gui.fluxnetworks.label.customcolor"),
            CUSTOM_COLOR_EDIT = new FluxTranslate("gui.fluxnetworks.label.customcolor.edit"),
            CONNECTING_TO = new FluxTranslate("gui.fluxnetworks.label.connectingto"),
            TOTAL = new FluxTranslate("gui.fluxnetworks.label.total"),
            DELETE_NETWORK = new FluxTranslate("gui.fluxnetworks.label.deletenetwork"),
            DOUBLE_SHIFT = new FluxTranslate("gui.fluxnetworks.label.doubleshift"),
            TRANSFER_OWNERSHIP = new FluxTranslate("gui.fluxnetworks.label.transferownership"),
            SET_USER = new FluxTranslate("gui.fluxnetworks.label.setuser"),
            SET_ADMIN = new FluxTranslate("gui.fluxnetworks.label.setadmin"),
            USERNAME = new FluxTranslate("gui.fluxnetworks.label.playername"),
            ACCESS = new FluxTranslate("gui.fluxnetworks.label.playeraccess"),
            CANCEL_MEMBERSHIP = new FluxTranslate("gui.fluxnetworks.label.cancelmembership"),
            YOU = new FluxTranslate("gui.fluxnetworks.label.you"),
            DETAILED_VIEW = new FluxTranslate("gui.fluxnetworks.label.detailedview"),
            CLICK_ABOVE = new FluxTranslate("gui.fluxnetworks.label.clickabove"),
            PLAYERS_NETWORK = new FluxTranslate("gui.fluxnetworks.label.playersnetwork");

    public static final FluxTranslate
            PUBLIC = new FluxTranslate("gui.fluxnetworks.security.public"),
            ENCRYPTED = new FluxTranslate("gui.fluxnetworks.security.encrypted"),
            PRIVATE = new FluxTranslate("gui.fluxnetworks.security.private");

    public static final FluxTranslate
            SORTING_SMART = new FluxTranslate("gui.fluxnetworks.label.sort.smart"),
            SORTING_ID = new FluxTranslate("gui.fluxnetworks.label.sort.id"),
            SORTING_NAME = new FluxTranslate("gui.fluxnetworks.label.sort.name");

    public static final FluxTranslate
            BATCH_SELECT_BUTTON = new FluxTranslate("gui.fluxnetworks.button.batchselect"),
            BATCH_CLEAR_BUTTON = new FluxTranslate("gui.fluxnetworks.button.batchclear"),
            BATCH_EDIT_BUTTON = new FluxTranslate("gui.fluxnetworks.button.batchedit"),
            BATCH_DISCONNECT_BUTTON = new FluxTranslate("gui.fluxnetworks.button.batchdisconnect"),
            APPLY = new FluxTranslate("gui.fluxnetworks.button.apply"),
            CANCEL = new FluxTranslate("gui.fluxnetworks.button.cancel"),
            CREATE = new FluxTranslate("gui.fluxnetworks.button.create"),
            CONNECT = new FluxTranslate("gui.fluxnetworks.button.connect"),
            DELETE = new FluxTranslate("gui.fluxnetworks.button.delete"),

    INVENTORY = new FluxTranslate("gui.fluxnetworks.slot.main"),
            HOT_BAR = new FluxTranslate("gui.fluxnetworks.slot.hotbar"),
            MAIN_HAND = new FluxTranslate("gui.fluxnetworks.slot.mainhand"),
            OFF_HAND = new FluxTranslate("gui.fluxnetworks.slot.offhand"),
            ARMOR = new FluxTranslate("gui.fluxnetworks.slot.armor"),
            TRINKETS = new FluxTranslate("gui.fluxnetworks.slot.trinkets"),

    PLUGS = new FluxTranslate("gui.fluxnetworks.stat.plug"),
            POINTS = new FluxTranslate("gui.fluxnetworks.stat.point"),
            CONTROLLERS = new FluxTranslate("gui.fluxnetworks.stat.controller"),
            STORAGES = new FluxTranslate("gui.fluxnetworks.stat.storage"),

    REMOVAL_DENIED = new FluxTranslate("gui.fluxnetworks.denied.removal"),
            SA_ON = new FluxTranslate("gui.fluxnetworks.superadmin.on"),
            SA_OFF = new FluxTranslate("gui.fluxnetworks.superadmin.off");

    public static final FluxTranslate
            REJECT = new FluxTranslate("gui.fluxnetworks.response.reject"),
            NO_OWNER = new FluxTranslate("gui.fluxnetworks.response.noowner"),
            NO_ADMIN = new FluxTranslate("gui.fluxnetworks.response.noadmin"),
            NO_SPACE = new FluxTranslate("gui.fluxnetworks.response.nospace"),
            HAS_CONTROLLER = new FluxTranslate("gui.fluxnetworks.response.hascontroller"),
            INVALID_USER = new FluxTranslate("gui.fluxnetworks.response.invaliduser"),
            INVALID_PASSWORD = new FluxTranslate("gui.fluxnetworks.response.invalidpassword"),
            BANNED_LOADING = new FluxTranslate("gui.fluxnetworks.response.bannedloading"),

    OWNER = new FluxTranslate("gui.fluxnetworks.access.owner"),
            ADMIN = new FluxTranslate("gui.fluxnetworks.access.admin"),
            USER = new FluxTranslate("gui.fluxnetworks.access.user"),
            BLOCKED = new FluxTranslate("gui.fluxnetworks.access.blocked"),
            SUPER_ADMIN = new FluxTranslate("gui.fluxnetworks.access.superadmin"),

    JEI_CREATING_FLUX = new FluxTranslate("gui.fluxnetworks.jei.creatingfluxrecipe"),
            JEI_LEFT_CLICK = new FluxTranslate("gui.fluxnetworks.jei.leftclickhelp");

    private final String mKey;
    private final Component mComponent;

    public FluxTranslate(String key) {
        mKey = key;
        mComponent = Component.translatable(mKey);
    }

    @Nullable
    public static FluxTranslate fromResponseCode(int code) {
        return switch (code) {
            case FluxConstants.RESPONSE_REJECT -> REJECT;
            case FluxConstants.RESPONSE_NO_OWNER -> NO_OWNER;
            case FluxConstants.RESPONSE_NO_ADMIN -> NO_ADMIN;
            case FluxConstants.RESPONSE_NO_SPACE -> NO_SPACE;
            case FluxConstants.RESPONSE_HAS_CONTROLLER -> HAS_CONTROLLER;
            case FluxConstants.RESPONSE_INVALID_USER -> INVALID_USER;
            case FluxConstants.RESPONSE_INVALID_PASSWORD -> INVALID_PASSWORD;
            case FluxConstants.RESPONSE_BANNED_LOADING -> BANNED_LOADING;
            default -> null;
        };
    }

    @Nonnull
    public static String getTranslation(String key) {
        return Language.getInstance().getOrDefault(key);
    }

    private static final ChatFormatting[] ERROR_STYLE = {ChatFormatting.BOLD, ChatFormatting.DARK_RED};

    public static final Component
            ACCESS_DENIED = Component.translatable("gui.fluxnetworks.denied_access").withStyle(ERROR_STYLE),
            ACCESS_OCCUPY = Component.translatable("gui.fluxnetworks.denied_occupy").withStyle(ERROR_STYLE);
    public static final Component
            CONFIG_COPIED = Component.translatable("gui.fluxnetworks.config_copied"),
            CONFIG_PASTED = Component.translatable("gui.fluxnetworks.config_pasted");

    @Nonnull
    public String get() {
        return getTranslation(mKey);
    }

    @Nonnull
    public List<String> getSplit(int width) {
        return Minecraft.getInstance().font.getSplitter().splitLines(get(), width, Style.EMPTY).stream().map(FormattedText::getString).toList();
    }

    @Nonnull
    @Environment(EnvType.CLIENT)
    public String format() {
        return get();
    }

    @Nonnull
    @Environment(EnvType.CLIENT)
    public String format(Object... args) {
        return I18n.get(mKey, args);
    }

    @Nonnull
    public Component getComponent() {
        return mComponent;
    }

    @Nonnull
    public MutableComponent makeComponent() {
        return Component.translatable(mKey);
    }

    @Nonnull
    public MutableComponent makeComponent(Object... args) {
        return Component.translatable(mKey, args);
    }

    @Nonnull
    @Override
    public String toString() {
        // also work on server side
        return get();
    }
}
