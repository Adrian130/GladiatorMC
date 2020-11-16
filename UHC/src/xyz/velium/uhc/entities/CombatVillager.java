package xyz.velium.uhc.entities;

import java.lang.reflect.Field;
import net.minecraft.server.v1_7_R4.EntityVillager;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;

public class CombatVillager extends EntityVillager {

    public CombatVillager(World world) {
        super(world);
        try {
            Field bField = net.minecraft.server.v1_7_R4.PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = net.minecraft.server.v1_7_R4.PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(this.goalSelector, new UnsafeList());
            bField.set(this.targetSelector, new UnsafeList());
            cField.set(this.goalSelector, new UnsafeList());
            cField.set(this.targetSelector, new UnsafeList());

            this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, net.minecraft.server.v1_7_R4.EntityHuman.class, 8.0F));
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        this.fireProof = true;
    }
}
