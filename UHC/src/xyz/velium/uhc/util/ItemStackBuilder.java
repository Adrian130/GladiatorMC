package xyz.velium.uhc.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackBuilder {

    private Material material;
    private int amount;
    private byte durability;
    private List<String> lore;
    private String name;
    private Map<Enchantment, Integer> enchantments;
    private boolean unbreakable;

    public ItemStackBuilder() {
        // -- default stuff so we don't get errors when creating the item --
        this.material = Material.STONE;
        this.amount = 1;
        this.durability = (short) 0;
        this.lore = new ArrayList<>();
        this.name = null;
        this.enchantments = new HashMap<>();
        this.unbreakable = false;
    }

    public ItemStackBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStackBuilder setDurability(byte durability) {
        this.durability = durability;
        return this;
    }

    public ItemStackBuilder addLore(String lore) {
        this.lore.add(lore);
        return this;
    }

    public ItemStackBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStackBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStackBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemStack build() {
        // -- create ItemStack --
        ItemStack itemStack = new ItemStack(material, amount, durability);
        ItemMeta itemMeta = itemStack.getItemMeta();

        // -- set meta --
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(name);
        itemMeta.spigot().setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        itemStack.addEnchantments(enchantments);

        return itemStack;
    }

    public ItemStack buildCustomHead(Player owner) {
        return buildCustomHead(owner.getName());
    }

    public ItemStack buildCustomHead(String owner) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) itemStack.getItemMeta();

        skull.setDisplayName(name);
        skull.setOwner(owner);
        skull.setLore(lore);
        itemStack.setItemMeta(skull);
        itemStack.addEnchantments(enchantments);

        return itemStack;
    }
}