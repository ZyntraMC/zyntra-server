package mc.zyntra.general.account;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.config.Language;
import mc.zyntra.general.account.fake.Fake;
import mc.zyntra.general.account.medal.PlayerMedalHandler;
import mc.zyntra.general.account.pass.BattlePass;
import mc.zyntra.general.account.skin.Skin;
import mc.zyntra.general.account.config.AccountConfiguration;
import mc.zyntra.general.account.punishment.PunishmentHistoric;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.group.GroupAttribute;
import mc.zyntra.general.account.group.GroupData;
import lombok.Getter;
import lombok.Setter;
import mc.zyntra.general.account.tag.PlayerTagHandler;
import mc.zyntra.general.account.tag.enums.Tag;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class ZyntraPlayer {

    private final UUID uniqueId;
    private String name, disguise;

    private Fake fake;

    private final List<GroupData> groupDataList = new ArrayList<>();

    private final PlayerTagHandler tagHandler = new PlayerTagHandler();
    private final PlayerMedalHandler medalHandler = new PlayerMedalHandler();

    private final AccountConfiguration configuration = new AccountConfiguration();
    private final PunishmentHistoric punishmentHistoric = new PunishmentHistoric();
    private final Map<String, Long> cooldown = new HashMap<>();
    private Skin skin;

    private List<String> kits = new ArrayList<>();
    private List<String> perks = new ArrayList<>();

    private AccountType accountType = AccountType.NONE;

    private int coins = 0;

    private int level = 0;
    private int exp = 0;

    private int cash = 0;

    private String password;
    private boolean logged = false, online = false;

    private long firstLogin = System.currentTimeMillis(), lastLogin;

    private transient String lastTell;

    private BattlePass battlePass;

    private Language language;

    private final Set<UUID> friends = new HashSet<>();

    public ZyntraPlayer(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.disguise = name;
        this.skin = new Skin(uniqueId, name, name);
        this.battlePass = new BattlePass(uniqueId);
        this.language = null;

        fake = new Fake(name, Tag.MEMBRO);

        this.groupDataList.add(new GroupData(Group.DEFAULT, GroupAttribute.AUTO, System.currentTimeMillis(), -1L, "CONSOLE"));
    }

    public GroupData getPrimaryGroupData() {
        if (groupDataList.isEmpty()) {
            return new GroupData(Group.DEFAULT, GroupAttribute.AUTO, System.currentTimeMillis(), -1L, "CONSOLE");
        }
        return groupDataList.stream()
                .min(Comparator.comparingInt(gd -> gd.getGroup().ordinal()))
                .orElse(groupDataList.get(0));
    }

    public GroupData getGroupData(Group group) {
        if (group == null || groupDataList == null) return null;

        return groupDataList.stream()
                .filter(gd -> gd.getGroup() == group)
                .findFirst()
                .orElse(null);
    }


    public boolean hasGroup(Group group) {
        return groupDataList.stream().anyMatch(gd -> gd.getGroup() == group);
    }

    public void addGroup(Group group, GroupAttribute attribute, long duration, String addedBy) {
        groupDataList.removeIf(gd -> gd.getGroup() == group);
        groupDataList.add(new GroupData(group, attribute, System.currentTimeMillis(), duration, addedBy));
    }

    public boolean removeGroup(Group group) {
        return groupDataList.removeIf(gd -> gd.getGroup() == group);
    }

    public boolean hasGroupPermission(Group group) {
        GroupData primary = getPrimaryGroupData();
        return primary.getGroup().ordinal() <= group.ordinal();
    }

    public void save() {
        Core.getDataPlayer().save(this);
    }

    public void update(String field) {
        Core.getDataPlayer().update(this, field);
    }

    public boolean hasCooldown(String cooldownId) {
        if (cooldown.containsKey(cooldownId)) {
            if (cooldown.get(cooldownId) > System.currentTimeMillis())
                return true;

            cooldown.remove(cooldownId);
        }
        return false;
    }

    public long getCooldown(String cooldownId) {
        return cooldown.getOrDefault(cooldownId, 0L);
    }

    public void addCooldown(String cooldownId, int seconds) {
        cooldown.put(cooldownId, System.currentTimeMillis() + (1000L * seconds));
    }

    public void addCooldown(String cooldownId, long time) {
        cooldown.put(cooldownId, time);
    }

    public void removeCooldown(String cooldownId) {
        cooldown.remove(cooldownId);
    }

    public void sendMessage(String message) {
        Core.getPlatform().sendMessage(uniqueId, message);
    }

    public void sendMessage(BaseComponent component) {
        Core.getPlatform().sendMessage(uniqueId, component);
    }

    public void sendMessage(BaseComponent... component) {
        Core.getPlatform().sendMessage(uniqueId, component);
    }

    public Player toPlayer() {
        return Core.getPlatform().getPlayerByUniqueId(uniqueId, Player.class);
    }

    public ProxiedPlayer toProxiedPlayer() {
        return Core.getPlatform().getPlayerByUniqueId(uniqueId, ProxiedPlayer.class);
    }

    public boolean hasDisguise() {
        return disguise != null && !disguise.equals(name);
    }

    public boolean hasSkin() {
        return skin != null && !skin.getName().equals(name);
    }

    public boolean isRegistered() {
        return password != null;
    }

    public void setClan(Object o) {
    }

    public enum AccountType {
        NONE,
        CRACKED,
        PREMIUM
    }
}
