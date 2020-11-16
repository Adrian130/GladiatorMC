package xyz.velium.uhc.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.util.ItemStackBuilder;

public enum Scenarios {

    TIMEBOMB(false, new ItemStackBuilder().setMaterial(Material.TNT).setName("Timebomb").build(),
            new String[]{"When you kill a player, a chest appears", "containing their drops. After 30 ", "seconds this chest will explode."}),
    BOWLESS(false, (new ItemStackBuilder().setMaterial(Material.BOW)).setName("Bowless").build(),
            new String[] {"You can not use bows" }),
    RODLESS(false, (new ItemStackBuilder().setMaterial(Material.FISHING_ROD)).setName("Rodless").build(),
            new String[] {"You can not use Fishing Rods" }),
    ABSORPOTIONLESS(false, (new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE)).setName("Absorptionless").build(),
            new String[] {"You will not receive the Absorption effect" }),
    FIRELESS(false, new ItemStackBuilder().setMaterial(Material.FLINT_AND_STEEL).setName("Fireless").build(),
            new String[]{"You take no fire damage."}),
    NOFALL(false, new ItemStackBuilder().setMaterial(Material.FEATHER).setName("NoFall").build(),
            new String[]{"You take no fall damage."}),
    NOCLEAN(false, new ItemStackBuilder().setMaterial(Material.DIAMOND_SWORD).setName("NoClean").build(),
            new String[]{"When you kill a player you get 20", " seconds of invincibility."}),
    GOLDENRETRIEVER(false, new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE).setName("Golden Retriever").build(),
            new String[]{"Players drop 1 golden Apple after dies"}),
    BACKPACK(false, new ItemStackBuilder().setMaterial(Material.CHEST).setName("BackPack").build(),
            new String[]{"Food and ores are pre-smelted, removing", "the needs of furnaces."}),
    SOUP(false, new ItemStackBuilder().setMaterial(Material.MUSHROOM_SOUP).setName("Soup").build(),
            new String[]{"When you right click a soup, you regain", "3.5 hearts."}),
    HORSELESS(false, new ItemStackBuilder().setMaterial(Material.SADDLE).setName("Horseless").build(),
            new String[]{"You cannot ride horses."}),
    LUCKYLEAVES(false, new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE).setName("LuckyLeaves").build(),
            new String[]{"There's a 0.5% chance of golden apples, ", "dropping from decaying leaves"}),
    HASTEYBOYS(false, new ItemStackBuilder().setMaterial(Material.WOOD_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 3).setName("HasteyBoys").build(),
            new String[]{"Tools automatically enchant", "with efficiency 3."}),
    TIMBER(false, new ItemStackBuilder().setMaterial(Material.SAPLING).setName("Timber").build(),
            new String[]{"The whole tree drops when you mine a log."}),
    CUTCLEAN(false, new ItemStackBuilder().setMaterial(Material.IRON_INGOT).setName("CutClean").build(),
            new String[]{"Ores and animal drops are automatically smelted,", "no furnaces needed."});

    private boolean enabled;
    private ItemStack itemStack;
    private String[] description;

    public static final Scenarios[] VALUES = values();

    Scenarios(boolean enabled, ItemStack itemStack, String[] description) {
        this.enabled = enabled;
        this.itemStack = itemStack;
        this.description = description;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String[] getDescription() {
        return description;
    }

    public String getScenarioName() {
        return itemStack.getItemMeta().getDisplayName();
    }

    public void enableScenario() {
        setEnabled(true);
    }

    private UHC uhc;

    Scenarios(UHC uhc) {
        this.uhc = uhc;
    }

    public void disableScenario() {
        setEnabled(false);
    }
    // ----- UTILITY ----- //

    public static Scenarios getScenarioFromName(String string) {
        for (Scenarios scenario : VALUES) {
            if (scenario.getItemStack().hasItemMeta()) {
                if (scenario.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(string)) {
                    return scenario;
                }
            }
        }
        return null;
    }

    public String getScenarios() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < VALUES.length; i++) {
            Scenarios scenarios = VALUES[i];
            if (scenarios.isEnabled()) {
                stringBuilder.append(ChatColor.GREEN);
                stringBuilder.append(scenarios.getScenarioName());

                stringBuilder.append(ChatColor.RESET);
                if ((i + 1) < VALUES.length)
                    stringBuilder.append(", ChatColor.GRAY + ");
            }
        }
        return stringBuilder.toString();
    }
}
