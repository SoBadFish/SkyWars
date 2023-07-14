package org.sobadfish.skywars.manager;



import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import org.sobadfish.skywars.item.nbt.FireBall;
import org.sobadfish.skywars.item.nbt.INbtItem;
import org.sobadfish.skywars.item.nbt.KillerAxe;
import org.sobadfish.skywars.item.nbt.PointPlayer;


import java.util.LinkedHashMap;


/**
 * @author SoBadFish
 * 2022/1/5
 */
public class NbtItemManager {

    public static String TAG = "SKY_WAR_ITEM_TAG";

    public static LinkedHashMap<String, INbtItem> NBT_MANAGER = new LinkedHashMap<>();

    public static void init(){

        NBT_MANAGER.put("火球",new FireBall());
        NBT_MANAGER.put("秒人斧",new KillerAxe());
        NBT_MANAGER.put("指南针",new PointPlayer());

    }

    public static Item asNbtItem(Item item,String name){
        CompoundTag tag = item.getNamedTag();
        if(tag == null){
            tag = new CompoundTag();
        }
        tag.putString(TAG,name);
        return item;
    }

}
