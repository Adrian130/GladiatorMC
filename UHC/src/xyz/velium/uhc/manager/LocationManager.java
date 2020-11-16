package xyz.velium.uhc.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.velium.uhc.UHC;

import java.io.File;
import java.io.IOException;

public class LocationManager {

    private UHC uhc;

    public LocationManager(UHC uhc) {
        this.uhc = uhc;
    }

    private File locationfile = new File("plugins/UHC/locations.yml");
    private FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(locationfile);

    public void saveLocationFile() {
        try {
            this.fileConfiguration.save(this.locationfile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setLocation(String name, Location loc) {
        this.fileConfiguration.set(name + ".World", loc.getWorld().getName());
        this.fileConfiguration.set(name + ".X", Double.valueOf(loc.getX()));
        this.fileConfiguration.set(name + ".Y", Double.valueOf(loc.getY()));
        this.fileConfiguration.set(name + ".Z", Double.valueOf(loc.getZ()));
        this.fileConfiguration.set(name + ".Yaw", Float.valueOf(loc.getYaw()));
        this.fileConfiguration.set(name + ".Pitch", Float.valueOf(loc.getPitch()));

        this.saveLocationFile();

        if (!this.locationfile.exists()) {
            try {
                this.locationfile.createNewFile();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public Location getLocation(String name) {
        World world = Bukkit.getWorld(this.fileConfiguration.getString(name + ".World"));

        double x = this.fileConfiguration.getDouble(name + ".X");
        double y = this.fileConfiguration.getDouble(name + ".Y");
        double z = this.fileConfiguration.getDouble(name + ".Z");

        Location location = new Location(world, x, y, z);
        location.setYaw(this.fileConfiguration.getInt(name + ".Yaw"));
        location.setPitch(this.fileConfiguration.getInt(name + ".Pitch"));
        return location;
    }
}
