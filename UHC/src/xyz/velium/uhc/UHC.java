package xyz.velium.uhc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.velium.uhc.commands.*;
import xyz.velium.uhc.commands.staff.LocationCommand;
import xyz.velium.uhc.commands.staff.*;
import xyz.velium.uhc.commands.team.BackPackCommand;
import xyz.velium.uhc.commands.team.TeamCommand;
import xyz.velium.uhc.commands.team.TeamListCommand;
import xyz.velium.uhc.entities.CombatVillager;
import xyz.velium.uhc.entities.MobUtil;
import xyz.velium.uhc.entities.PlaceHolder;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.listener.*;
import xyz.velium.uhc.listener.gamestates.GameListener;
import xyz.velium.uhc.listener.gamestates.LobbyListener;
import xyz.velium.uhc.listener.gamestates.ScatterListener;
import xyz.velium.uhc.listener.scenarios.AbsorptionlessListener;
import xyz.velium.uhc.listener.playerstates.PracticeListener;
import xyz.velium.uhc.listener.playerstates.SpectatorListener;
import xyz.velium.uhc.listener.scenarios.*;
import xyz.velium.uhc.manager.*;
import xyz.velium.uhc.manager.ConfigManager;
import xyz.velium.uhc.manager.scoreboard.SimpleBoardManager;
import xyz.velium.uhc.manager.scoreboard.UHCBoardProvider;
import xyz.velium.uhc.tasks.CombatLoggerTask;
import xyz.velium.uhc.tasks.GameTask;
import xyz.velium.uhc.tasks.StartTask;
import xyz.velium.uhc.util.Inventories;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

@Getter @Setter
public class UHC extends JavaPlugin {

    private static UHC instance;

    private String PREFIX = ChatColor.GRAY + ChatColor.BOLD.toString() + "(" + ChatColor.GOLD + ChatColor.BOLD.toString() +
            "UHC" + ChatColor.GRAY + ChatColor.BOLD.toString() + ") " + ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "» " + ChatColor.RESET;

    //Enums
    private GameStates gameStates;

    //Classes
    private PlayerManager playerManager;
    private TeamManager teamManager;
    private LocationManager locationManager;
    private Inventories inventories;
    private WorldManager worldManager;
    private ScenarioManager scenarioManager;
    private GameManager gameManager;
    private BorderManager borderManager;
    private ConfigManager configManager;
    private SimpleBoardManager simpleBoardManager;

    //Tasks
    private StartTask startTask;
    private GameTask gameTask;
    private CombatLoggerTask combatLoggerTask;

    private File file;
    private FileConfiguration fileConfiguration;

    public void onEnable() {
        this.instance = this;

        this.saveDefaultConfig();
        this.file = new File("plugins/UHC", "config.yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);

        MobUtil.getInstance().registerEntity(PlaceHolder.class, 100);
        MobUtil.getInstance().registerEntityEdit(CombatVillager.class,  120);

        this.playerManager = new PlayerManager(this);
        this.teamManager = new TeamManager(this);
        this.locationManager = new LocationManager(this);
        this.worldManager = new WorldManager(this);
        this.inventories = new Inventories(this);
        this.gameManager = new GameManager(this);
        this.borderManager = new BorderManager(this);
        this.configManager = new ConfigManager(this);
        this.scenarioManager = new ScenarioManager(this);
        this.simpleBoardManager = new SimpleBoardManager(this, new UHCBoardProvider(this));

        this.startTask = new StartTask(this);
        this.gameTask = new GameTask(this);
        this.combatLoggerTask = new CombatLoggerTask(this);

        this.gameManager.setReset(this.getConfig().getBoolean("reset"));
        this.gameManager.prepareGame();

        this.registerGoldenHead();
        this.loadListener();
        this.loadCommands();
    }

    public void onDisable() {
        this.instance = null;
    }

    private void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new InventoryClickListener(this), this);
        pluginManager.registerEvents(new PlayerLoginListener(this), this);
        pluginManager.registerEvents(new GlassBorderListener(this), this);
        pluginManager.registerEvents(new ASyncPlayerChatListener(this), this);
        pluginManager.registerEvents(new WorldBorderFillFinishListener(this), this);
        pluginManager.registerEvents(this.simpleBoardManager, this);

        //GameStates
        pluginManager.registerEvents(new LobbyListener(this), this);
        pluginManager.registerEvents(new ScatterListener(this), this);
        pluginManager.registerEvents(new GameListener(this), this);

        //PlayerStates
        pluginManager.registerEvents(new PracticeListener(this), this);
        pluginManager.registerEvents(new SpectatorListener(this), this);

        //Scenarios
        pluginManager.registerEvents(new CutCleanListener(this), this);
        pluginManager.registerEvents(new FirelessListener(this), this);
        pluginManager.registerEvents(new GoldenRetriverListener(this), this);
        pluginManager.registerEvents(new HasteyBoysListener(this), this);
        pluginManager.registerEvents(new HorselessListener(this), this);
        pluginManager.registerEvents(new LuckyLeavesListener(this), this);
        pluginManager.registerEvents(new NoCleanListener(this), this);
        pluginManager.registerEvents(new NoFallListener(this), this);
        pluginManager.registerEvents(new SoupListener(this), this);
        pluginManager.registerEvents(new TimberListener(this), this);
        pluginManager.registerEvents(new AbsorptionlessListener(this), this);
        pluginManager.registerEvents(new BowlessListener(this), this);
        pluginManager.registerEvents(new RodlessListener(this), this);
        pluginManager.registerEvents(new TimeBombListener(this), this);
    }

    private void loadCommands() {
        this.getCommand("teleport").setExecutor(new TeleportCommand(this));
        this.getCommand("respawn").setExecutor(new RespawnCommand(this));
        this.getCommand("backpack").setExecutor(new BackPackCommand(this));
        this.getCommand("staffnotifications").setExecutor(new ToggleStaffNotificationsCommand(this));
        this.getCommand("helpop").setExecutor(new HelpOPCommand(this));
        this.getCommand("team").setExecutor(new TeamCommand(this));
        this.getCommand("teamlist").setExecutor(new TeamListCommand(this));
        this.getCommand("practice").setExecutor(new PracticeCommand(this));
        this.getCommand("setlocation").setExecutor(new LocationCommand(this));
        this.getCommand("config").setExecutor(new ConfigCommand(this));
        this.getCommand("host").setExecutor(new HostCommand(this));
        this.getCommand("moderator").setExecutor(new ModeratorCommand(this));
        this.getCommand("whitelist").setExecutor(new WhitelistCommand(this));
        this.getCommand("start").setExecutor(new StartCommand(this));
        this.getCommand("scenarios").setExecutor(new ScenariosCommand(this));
        this.getCommand("scenarioeditor").setExecutor(new ScenarioEditorCommand(this));
        this.getCommand("health").setExecutor(new HealthCommand(this));
        this.getCommand("game").setExecutor(new GameCommand(this));
        this.getCommand("killcount").setExecutor(new KillCountCommand(this));
        this.getCommand("killtop").setExecutor(new KillTopCommand(this));
        this.getCommand("list").setExecutor(new ListCommand(this));
        this.getCommand("forcepvp").setExecutor(new ForcePvPCommand( this));
        this.getCommand("debug").setExecutor(new DebugCommand( this));
        this.getCommand("latescatter").setExecutor(new LatescatterCommand( this));
    }

    private void registerGoldenHead() {
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Golden Head");
        itemStack.setItemMeta(itemMeta);
        ShapedRecipe shapedRecipe = new ShapedRecipe(itemStack);
        shapedRecipe.shape("&&&", "&/&", "&&&");
        shapedRecipe.setIngredient('&', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('/', Material.SKULL_ITEM, 3);
        Bukkit.getServer().addRecipe(shapedRecipe);
    }

    public Collection<? extends Player> getOnlinePlayers() {
        Collection<Player> players = new HashSet<>();
        for (Player player : getServer().getOnlinePlayers()) {
            players.add(player);
        }
        return players;
    }

    public static UHC getInstance() {
        return instance;
    }

    public static void setInstance(UHC instance) {
        UHC.instance = instance;
    }
}
