package xyz.velium.uhc.listener.scenarios;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.Scenarios;

import java.util.HashSet;
import java.util.Set;

public class HasteyBoysListener implements Listener {

    private UHC uhc;
    private Set<Material> materials;

    public HasteyBoysListener(UHC uhc) {
        this.uhc = uhc;
        this.materials = new HashSet<>();

        this.materials.add(Material.WOOD_SPADE);
        this.materials.add(Material.WOOD_AXE);
        this.materials.add(Material.WOOD_PICKAXE);

        this.materials.add(Material.STONE_SPADE);
        this.materials.add(Material.STONE_AXE);
        this.materials.add(Material.STONE_PICKAXE);

        this.materials.add(Material.IRON_SPADE);
        this.materials.add(Material.IRON_AXE);
        this.materials.add(Material.IRON_PICKAXE);

        this.materials.add(Material.DIAMOND_SPADE);
        this.materials.add(Material.DIAMOND_AXE);
        this.materials.add(Material.DIAMOND_PICKAXE);

        this.materials.add(Material.GOLD_SPADE);
        this.materials.add(Material.GOLD_AXE);
        this.materials.add(Material.GOLD_PICKAXE);

    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        if(uhc.getGameStates() == GameStates.INGAME) {
            if(Scenarios.HASTEYBOYS.isEnabled()) {
                if (materials.contains(event.getRecipe().getResult().getType())) {
                    ItemStack item = new ItemStack(event.getRecipe().getResult().getType());
                    item.addEnchantment(Enchantment.DIG_SPEED, 3);
                    item.addEnchantment(Enchantment.DURABILITY, 3);
                    event.getInventory().setResult(item);
                }
            }
        }
    }
}
