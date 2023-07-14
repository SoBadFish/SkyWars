package org.sobadfish.skywars.item.nbt;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemAxeGold;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.skywars.manager.NbtItemManager;

/**
 * @author Sobadfish
 * @date 2023/7/14
 */
public class KillerAxe implements INbtItem{
    @Override
    public String getName() {
        return "秒人斧";
    }

    @Override
    public boolean onClick(Item item, Player player) {
        return false;
    }

    @Override
    public Item getItem() {
        ItemAxeGold item = new ItemAxeGold();
        item.setDamage(32);
        item.setCustomName(TextFormat.colorize('&',"&r&l&c秒人斧"));
        return NbtItemManager.asNbtItem(item,getName());
    }
}
