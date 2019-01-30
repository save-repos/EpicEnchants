package com.songoda.epicenchants.menus;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.utils.FastInv;
import com.songoda.epicenchants.utils.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.Experience.changeExp;
import static com.songoda.epicenchants.utils.Experience.getExp;
import static com.songoda.epicenchants.utils.GeneralUtils.color;

public class EnchanterMenu extends FastInv {
    public EnchanterMenu(EpicEnchants instance, FileConfiguration config, Player player) {
        super(config.getInt("rows") * 9, color(config.getString("title")));

        if (config.isConfigurationSection("fill")) {
            fill(new ItemBuilder(config.getConfigurationSection("fill")).build());
        }

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .forEach(section -> {
                    double expCost = section.getDouble("exp-cost");
                    double ecoCost = section.getDouble("eco-cost");
                    double xpLeft = expCost - player.getLevel() < 0 ? 0 : expCost - player.getLevel();
                    double ecoLeft = ecoCost - instance.getEconomy().getBalance(player) < 0 ? 0 : ecoCost - instance.getEconomy().getBalance(player);
                    Group group = instance.getGroupManager().getGroup(section.getString("group").toUpperCase())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid group set in enchanter: " + section.getString("group")));
                    ItemStack itemStack = new ItemBuilder(section,
                            of("exp_cost", expCost),
                            of("eco_cost", ecoCost),
                            of("exp_left", xpLeft),
                            of("eco_left", ecoLeft)).build();

                    addItem(section.getInt("slot"), itemStack, event -> {

                        if (!instance.getEconomy().has((player), ecoCost) || getExp(player) < expCost) {
                            player.sendMessage(instance.getLocale().getPrefix() + instance.getLocale().getMessage("event.purchase.cannotafford"));
                            return;
                        }

                        Optional<Enchant> enchant = instance.getEnchantManager().getRandomEnchant(group);

                        if (!enchant.isPresent()) {
                            player.sendMessage(instance.getLocale().getPrefix() + instance.getLocale().getMessage("event.purchase.noenchant"));
                            return;
                        }

                        instance.getEconomy().withdrawPlayer(player, ecoCost);
                        changeExp(player, (int) -expCost);
                        player.getInventory().addItem(enchant.get().getBookItem().get(enchant.get()));
                        player.sendMessage(instance.getLocale().getMessageWithPrefix("event.purchase.success", of("group-name", group.getName())));
                    });
                });
    }
}

