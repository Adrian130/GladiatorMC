package xyz.velium.uhc.manager;

import lombok.Getter;
import lombok.Setter;
import xyz.velium.uhc.UHC;

import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public class ConfigManager {

    private UHC uhc;

    public ConfigManager(UHC uhc) {
        this.uhc = uhc;
    }

    private boolean reset = false;

    private boolean whitelist = true;
    private ArrayList<UUID> whitelistedPlayers = new ArrayList<>();

    private int pvpTime = 20;
    private int healTime = 10;
    private int borderTime = 45;
    private int maxPlayers = 100;
    private int starterFood = 10;

    private boolean nether = false;
    private boolean godApples = false;
    private boolean pvp = false;
    private boolean enderpearlDamage = false;

    private boolean speed1 = true;
    private boolean speed2 = true;
    private boolean strenght1 = false;
    private boolean strenght2 = false;
}
