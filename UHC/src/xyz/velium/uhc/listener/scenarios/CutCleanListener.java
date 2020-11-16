package xyz.velium.uhc.listener.scenarios;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.util.JavaUtils;

import java.util.Random;

public class CutCleanListener implements Listener {

    private UHC uhc;
    private Random random;

    public CutCleanListener(UHC uhc) {
        this.uhc = uhc;
        this.random = new Random();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if(Scenarios.CUTCLEAN.isEnabled()) {
            if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                if (event.isCancelled()) return;
                switch (event.getBlock().getType()) {
                    case IRON_ORE:
                        if ((player.getItemInHand().getType() != Material.DIAMOND_PICKAXE) && (player.getItemInHand().getType() != Material.IRON_PICKAXE) &&
                                (player.getItemInHand().getType() != Material.STONE_PICKAXE)) {
                            event.getBlock().setType(Material.AIR);
                            return;
                        }
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
                        event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(2);
                        break;
                    case GOLD_ORE:
                        if ((player.getItemInHand().getType() != Material.DIAMOND_PICKAXE) && (player.getItemInHand().getType() != Material.IRON_PICKAXE)) {
                            event.getBlock().setType(Material.AIR);
                            return;
                        }
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
                        event.getBlock().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class).setExperience(4);
                        break;
                    case GRAVEL:
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.FLINT));
                        break;
                    case LEAVES:
                    case LEAVES_2:
                        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().getMaxDurability() > 0) {
                            short dura = event.getPlayer().getItemInHand().getDurability();
                            if (++dura >= event.getPlayer().getItemInHand().getType().getMaxDurability()) {
                                player.setItemInHand(null);
                                player.updateInventory();
                            } else {
                                player.getItemInHand().setDurability(dura);
                            }
                        }
                        // 2 % chance to get apples
                        if (random.nextInt(100) <= 2) {
                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
                        }
                        if (event.getPlayer().getItemInHand().getType() == Material.SHEARS) {
                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.LEAVES));
                        }
                        event.getBlock().breakNaturally();
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        if(Scenarios.CUTCLEAN.isEnabled()) {
            if (random.nextInt(100) <= 1) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(Scenarios.CUTCLEAN.isEnabled()) {
            switch (event.getEntityType()) {
                case SHEEP:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.WOOL, JavaUtils.getRandomIntInRange(1, 2)));
                    event.getDrops().add(new ItemStack(Material.COOKED_BEEF, JavaUtils.getRandomIntInRange(1, 2)));
                    break;
                case COW:
                case MUSHROOM_COW:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.LEATHER, JavaUtils.getRandomIntInRange(1, 2)));
                    event.getDrops().add(new ItemStack(Material.COOKED_BEEF, JavaUtils.getRandomIntInRange(1, 3)));
                    break;
                case PIG:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.GRILLED_PORK, JavaUtils.getRandomIntInRange(1, 3)));
                    break;
                case CHICKEN:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 1));
                    event.getDrops().add(new ItemStack(Material.FEATHER, JavaUtils.getRandomIntInRange(1, 2)));
                    break;
                case HORSE:
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Material.LEATHER, 1));
                    break;
            }
        }
    }
}
