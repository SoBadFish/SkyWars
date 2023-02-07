package org.sobadfish.skywars.room.world;


import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import org.sobadfish.skywars.entity.GameFloatText;
import org.sobadfish.skywars.player.PlayerInfo;
import org.sobadfish.skywars.room.GameRoom;
import org.sobadfish.skywars.room.config.WorldInfoConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图的实例化方法，当房间启动后，这个方法也随之启动
 * @author Sobadfish
 * @date 2022/9/9
 */
public class WorldInfo {

    private GameRoom room;

    private boolean isClose;

    public int resetTime;

    public boolean isStart;

    private WorldInfoConfig config;

    public List<Position> spawnBlock = new ArrayList<>();

    public LinkedHashMap<Position,Long> clickChest = new LinkedHashMap<>();

    public List<Block> placeBlock = new ArrayList<>();

    private LinkedHashMap<Position,GameFloatText> resetChestFloat = new LinkedHashMap<>();

    public WorldInfo(GameRoom room,WorldInfoConfig config){
        this.config = config;
        this.room = room;
        this.resetTime = 0;

    }

    public WorldInfoConfig getConfig() {
        return config;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public void onChangeBlock(Block block, boolean isPlace){

        if(isPlace){
            placeBlock.add(block);
        }else{
            placeBlock.remove(block);
        }
    }

    public List<Block> getPlaceBlock() {
        return placeBlock;
    }

    public void onUpdate() {
        if(room.getType() == GameRoom.GameType.START) {
            if (resetTime < room.roomConfig.resetTime) {
                resetTime++;
            } else {
                resetTime = 0;
                clickChest.clear();
                for(GameFloatText gameFloatText: resetChestFloat.values()){
                    gameFloatText.toClose();
                }
                resetChestFloat.clear();
                room.sendMessage("&e所有箱子已刷新");
            }

            for (Map.Entry<Position,Long> chest:clickChest.entrySet()) {
                if(chest.getValue() + room.getRoomConfig().resetTime * 1000L <= System.currentTimeMillis()){
                    if(resetChestFloat.containsKey(chest.getKey())){
                        GameFloatText floatText = resetChestFloat.remove(chest.getKey());
                        floatText.toClose();
                    }
                    clickChest.remove(chest.getKey());
                }else{
                    if(resetChestFloat.containsKey(chest.getKey())){
                        int time = (int) ((chest.getValue() + room.getRoomConfig().resetTime * 1000L
                                - System.currentTimeMillis()) / 1000);
                        GameFloatText gameFloatText = resetChestFloat.get(chest.getKey());
                        gameFloatText.setText("&7[&a"+PlayerInfo.formatTime1(time)+"&7]");
                    }
                }
            }
        }

        ///////////////////DO Something////////////
    }

    /**
     * 增加浮空字刷新
     * */
    public void clickChest(Position position){
        clickChest.put(position,System.currentTimeMillis());
        long time = clickChest.get(position) + (room.getRoomConfig().resetTime * 1000L);
        String t =  PlayerInfo.formatTime1((int) ((time - System.currentTimeMillis()) / 1000));
        resetChestFloat.put(position, GameFloatText.showFloatText(
                WorldInfoConfig.positionToString(position),
                position.add(0.5,1.25,0.5),"&7[&a"+t+"&7]"
                )
        );
    }
}
