package org.sobadfish.skywars.room.config;

import cn.nukkit.item.Item;

import java.util.List;

/**
 * @author Sobadfish
 * @date 2022/9/28
 */
public class ItemConfig {

    public String block;

    public String name;

    public int updateTime;

    public List<Item> items;

    public ItemConfig(String block, String name,int updateTime, List<Item> items){
        this.block = block;
        this.name = name;
        this.updateTime = updateTime;
        this.items = items;
    }

}
