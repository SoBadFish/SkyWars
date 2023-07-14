package org.sobadfish.skywars.item.nbt;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import org.sobadfish.skywars.manager.TotalManager;
import org.sobadfish.skywars.player.PlayerInfo;
import org.sobadfish.skywars.room.GameRoom;

import java.util.*;

/**
 * 指向玩家
 * @author SoBadFish
 * 2022/1/6
 */
public class PointPlayer implements INbtItem{
    @Override
    public String getName() {
        return "指南针";
    }

    @Override
    public boolean onClick(Item item, Player player) {
        PlayerInfo info = TotalManager.getRoomManager().getPlayerInfo(player);
        if(info == null){
            return true;
        }
        GameRoom room = info.getGameRoom();
        PlayerInfo target;
        LinkedHashMap<PlayerInfo,Double> dis = new LinkedHashMap<>();
        for(PlayerInfo info1: room.getLivePlayers()){
            if(room.roomConfig.teamConfigs.size() > 0){
                if(!info1.getTeamInfo().equals(info.getTeamInfo())){
                    dis.put(info1,player.distance(info1.getPlayer()));
                }
            }else{
                dis.put(info1,player.distance(info1.getPlayer()));
            }

        }
        List<Map.Entry<PlayerInfo, Double>> list = new ArrayList<>(dis.entrySet());
        list.sort(Comparator.comparingInt(o -> o.getValue().intValue()));
        if(list.size() > 0) {
            target = list.get(0).getKey();

            if (target != null) {
                info.sendTip("&a找到" + target + "\n" + "&c距离: &r" + String.format("%.2f", list.get(0).getValue()) + " 米");

            }
        }
        player.getInventory().removeItem(item);
        return true;

    }

    @Override
    public Item getItem() {
        return null;
    }


}
