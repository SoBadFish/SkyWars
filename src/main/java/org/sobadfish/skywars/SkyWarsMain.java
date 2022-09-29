package org.sobadfish.skywars;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.skywars.command.GameAdminCommand;
import org.sobadfish.skywars.command.GameCommand;
import org.sobadfish.skywars.command.GameSpeakCommand;
import org.sobadfish.skywars.manager.TotalManager;

/**

 * @author Sobadfish
 * 13:07
 */
public class SkyWarsMain extends PluginBase {



    @Override
    public void onEnable() {


        this.getLogger().info(TextFormat.colorize('&',"&e ___ _      __      __           "));
        this.getLogger().info(TextFormat.colorize('&',"&e/ __| |___  \\ \\    / /_ _ _ _ ___"));
        this.getLogger().info(TextFormat.colorize('&',"&e\\__ \\ / / || \\ \\/\\/ / _` | '_(_-<"));
        this.getLogger().info(TextFormat.colorize('&',"&e|___/_\\_\\\\_, |\\_/\\_/\\__,_|_| /__/"));
        this.getLogger().info(TextFormat.colorize('&',"&e         |__/                    "));
        this.getLogger().info(TextFormat.colorize('&',"&e正在加载"+TotalManager.GAME_NAME+" 插件 本版本为&av"+this.getDescription().getVersion()));
        this.getLogger().info(TextFormat.colorize('&',"&a插件加载完成，祝您使用愉快"));

        TotalManager.init(this);
        this.getServer().getCommandMap().register(TotalManager.GAME_NAME,new GameAdminCommand(TotalManager.COMMAND_ADMIN_NAME));
        this.getServer().getCommandMap().register(TotalManager.GAME_NAME,new GameCommand(TotalManager.COMMAND_NAME));
        this.getServer().getCommandMap().register(TotalManager.GAME_NAME,new GameSpeakCommand(TotalManager.COMMAND_MESSAGE_NAME));

        this.getLogger().info(TextFormat.colorize('&',"&a插件加载完成，祝您使用愉快"));

    }

    @Override
    public void onDisable() {
       TotalManager.onDisable();
    }


//    public enum UiType{
//        /**
//         * auto: 自动
//         *
//         * packet: GUI界面
//         *
//         * ui: 箱子界面
//         * */
//        AUTO,PACKET,UI
//    }
}
