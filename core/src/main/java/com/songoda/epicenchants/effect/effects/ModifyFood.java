package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EffectType;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModifyFood extends EffectExecutor {
    public ModifyFood(ConfigurationSection section, EffectType... allowedEffects) {
        super(section, allowedEffects);
    }

    @Override
    public void execute(@NotNull Player wearer, @Nullable LivingEntity opponent, int level, EventType eventType) {
        consume(entity -> {
            if (entity instanceof Player) {
                ((Player) entity).setFoodLevel((int) (((Player) entity).getFoodLevel() + getAmount().get(level, 0)));
            }
        }, wearer, opponent);
    }
}
