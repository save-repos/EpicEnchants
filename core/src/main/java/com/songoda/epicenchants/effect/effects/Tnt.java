package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Tnt extends EffectExecutor {
    public Tnt(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        consume(player -> player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT), wearer, opponent);
    }
}