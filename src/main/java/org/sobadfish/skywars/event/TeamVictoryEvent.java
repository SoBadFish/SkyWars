package org.sobadfish.skywars.event;

import cn.nukkit.plugin.Plugin;
import org.sobadfish.skywars.player.team.TeamInfo;
import org.sobadfish.skywars.room.GameRoom;


/**
 * 队伍胜利事件
 * @author SoBadFish
 * 2022/1/15
 */
public class TeamVictoryEvent extends GameRoomEvent{

    private final TeamInfo teamInfo;

    public TeamVictoryEvent(TeamInfo teamInfo, GameRoom room, Plugin plugin) {
        super(room, plugin);
        this.teamInfo = teamInfo;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }
}
