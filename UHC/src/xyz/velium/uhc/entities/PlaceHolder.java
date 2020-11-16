package xyz.velium.uhc.entities;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.World;

public class PlaceHolder extends EntityHorse {

    public PlaceHolder(World world) {
        super(world);
        setInvisible(true);
    }

    public void makeSound(String string, float f, float f1) {}

    public void e() {}
}
