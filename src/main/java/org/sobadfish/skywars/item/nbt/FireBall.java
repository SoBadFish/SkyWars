package org.sobadfish.skywars.item.nbt;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import net.catrainbow.sakura.SakuraAPIAB;
import org.sobadfish.skywars.entity.EntityFireBall;
import org.sobadfish.skywars.manager.NbtItemManager;
import org.sobadfish.skywars.manager.TotalManager;
import org.sobadfish.skywars.player.PlayerInfo;

import java.util.LinkedHashMap;


/**
 * todo 加个冷却机制
 * @author SoBadFish
 * 2022/1/8
 */
public class FireBall implements INbtItem{


    private LinkedHashMap<PlayerInfo,Long> clickTime = new LinkedHashMap<>();


    @Override
    public String getName() {
        return "火球";
    }

    @Override
    public boolean onClick(Item item, Player player) {
        PlayerInfo playerInfo = TotalManager.getRoomManager().getPlayerInfo(player);
        if(playerInfo != null) {
            try {
                Class c = Class.forName("net.catrainbow.sakura.SakuraAPI");
                SakuraAPIAB sakuraAPI = (SakuraAPIAB) c.newInstance();
//                            net.catrainbow.sakura.SakuraAPIAB sakuraAPI = new net.catrainbow.sakura.SakuraAPI();
                sakuraAPI.addBypassTime((Player) playerInfo.getPlayer(), "KillAura", 2);
            } catch (Throwable ignore) {
            }
            if(!clickTime.containsKey(playerInfo)){
                clickTime.put(playerInfo,System.currentTimeMillis());
            }
            if(System.currentTimeMillis() - clickTime.get(playerInfo) < 1500){
                playerInfo.sendMessage("&c使用太频繁了 请过一会再试吧");
                return true;
            }else{
                clickTime.put(playerInfo,System.currentTimeMillis());
            }

            double f = 1.8D;
            double yaw = player.yaw;
            double pitch = player.pitch;
            playerInfo.isSpawnFire = true;
            Location pos = new Location(player.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 1.5D, player.y + (double) player.getEyeHeight(), player.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 1.5D, yaw, pitch, player.level);
            EntityFireBall fireBall = new EntityFireBall(player.chunk, Entity.getDefaultNBT(pos));
            fireBall.setExplode(true);
            fireBall.setMaster(playerInfo);
            fireBall.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f, Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));
            fireBall.spawnToAll();
            player.getInventory().removeItem(item);
            playerInfo.isSpawnFire = false;
        }
        return true;
    }

    @Override
    public Item getItem() {
        Item item = Item.get(385);
        item.setCustomName(TextFormat.colorize('&',"&r&l&e烈焰弹"));

        return NbtItemManager.asNbtItem(item,getName());

    }
}
