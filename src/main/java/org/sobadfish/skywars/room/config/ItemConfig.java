package org.sobadfish.skywars.room.config;

import cn.nukkit.item.Item;

import java.util.List;

/**
 * @author Sobadfish
 * @date 2022/9/23
 */
public class ItemConfig {

    public String block;

    public String name;

    public int time;

    public List<Item> items;

    public ItemConfig(String block, String name,int time, List<Item> items){
        this.block = block;
        this.time = time;
        this.name = name;
        this.items = items;
    }


}
