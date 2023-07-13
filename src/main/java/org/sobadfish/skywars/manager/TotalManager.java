package org.sobadfish.skywars.manager;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.skywars.manager.data.PlayerDataManager;
import org.sobadfish.skywars.manager.data.PlayerTopManager;
import org.sobadfish.skywars.manager.data.TagItemDataManager;
import org.sobadfish.skywars.room.config.GameRoomConfig;
import org.sobadfish.skywars.variable.GameNpcVariable;
import org.sobadfish.skywars.variable.GameTipVariable;

import java.io.File;

/**
 * 插件启动控制器
 * @author Sobadfish
 * @date 2022/9/12
 */
public class TotalManager {

    /**
     * 当前运行核心是否为PM1E*/
    public static boolean IS_PM1E = false;
    /**
     * 游戏指令
     * */
    public static final String COMMAND_NAME = "sw";

    /**
     * 游戏管理员指令
     * */
    public static final String COMMAND_ADMIN_NAME = "swa";

    /**
     * 游戏内喊话指令
     * */
    public static final String COMMAND_MESSAGE_NAME = "sws";

    /**
     * 小游戏名称
     * */
    public static final String GAME_NAME = "SkyWars";
    

    public static PluginBase plugin = null;



    private static PlayerDataManager dataManager;

    private static TagItemDataManager tagItemDataManager;

    private static PlayerTopManager topManager;


    public static int upExp;

    public static void init(PluginBase pluginBase){
        TotalManager.plugin = pluginBase;

        checkServer();
        loadConfig();
        loadVariable();
        ThreadManager.init();
    }

    private static void loadVariable() {
        try{
            Class.forName("com.smallaswater.npc.variable.BaseVariableV2");
            GameNpcVariable.init();
        }catch (Exception ignore){}
        try{
            Class.forName("tip.utils.variables.BaseVariable");
            GameTipVariable.init();
        }catch (Exception ignore){}
    }


    private static RoomManager roomManager;

    private static MenuRoomManager menuRoomManager;

    public static Boolean isDisabled(){
        return plugin.isDisabled();
    }

    public static PluginBase getPlugin() {
        return plugin;
    }

    public static Config getConfig(){
        if(plugin == null){
            return new Config();
        }
        return plugin.getConfig();
    }

    public static TagItemDataManager getTagItemDataManager() {
        return tagItemDataManager;
    }

    public static void sendMessageToConsole(String msg){
        sendMessageToObject(msg,null);
    }

    public static String getTitle(){
        return TextFormat.colorize('&', plugin.getConfig().getString("title"));
    }

    public static String getScoreBoardTitle(){
        return TextFormat.colorize('&', plugin.getConfig().getString("scoreboard-title","&f[&a空岛战争&f]"));
    }

    public static void sendTipMessageToObject(String msg,Object o){
        String message = TextFormat.colorize('&',msg);
        if(o != null){
            if(o instanceof Player){
                if(((Player) o).isOnline()) {
                    ((Player) o).sendMessage(message);
                    return;
                }
            }
        }
        plugin.getLogger().info(message);

    }



    /**
     * 加载配置文件
     */
    public static void loadConfig(){
        if(plugin == null){
            return;
        }
        upExp = getConfig().getInt("up-exp",500);
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        tagItemDataManager = TagItemDataManager.asFile(new File(plugin.getDataFolder()+File.separator+"tag.json"));
        File mainFileDir = new File(plugin.getDataFolder()+File.separator+"rooms");
        if(!mainFileDir.exists()){
            if(!mainFileDir.mkdirs()){
                sendMessageToConsole("&c创建文件夹 rooms失败");
            }
        }

        roomManager = RoomManager.initGameRoomConfig(mainFileDir);
        sendMessageToConsole("&a房间数据全部加载完成");
        plugin.getServer().getPluginManager().registerEvents(roomManager,plugin);
        if(plugin.getConfig().getAll().size() == 0) {
            plugin.saveResource("config.yml", true);
            plugin.reloadConfig();
        }
        menuRoomManager = new MenuRoomManager(plugin.getConfig());
        dataManager = PlayerDataManager.asFile(new File(plugin.getDataFolder()+File.separator+"player.json"));
        //初始化排行榜
        topManager = PlayerTopManager.asFile(new File(plugin.getDataFolder()+File.separator+"top.json"));
        if(topManager != null){
            topManager.init();
        }

    }
    public static PlayerDataManager getDataManager() {
        return dataManager;
    }

    public static PlayerTopManager getTopManager() {
        return topManager;
    }

    public static RoomManager getRoomManager() {
        return roomManager;
    }

    public static MenuRoomManager getMenuRoomManager() {
        return menuRoomManager;
    }


    public static void sendMessageToObject(String msg, Object o){
        String message = TextFormat.colorize('&',getTitle()+" &b>>&r "+msg);
        sendTipMessageToObject(message,o);
    }

    public static int getUpExp() {
        return upExp;
    }

    public static void sendSubTitle(String msg, Player o){
        String message = TextFormat.colorize('&',msg);
        if(o != null){
            if(o.isOnline()) {
                o.setSubtitle(message);
            }
        }else{
            plugin.getLogger().info(message);
        }
    }

    public static void sendTitle(String msg,int time,Player o){
        String message = TextFormat.colorize('&',msg);
        if(o != null){
            if(o.isOnline()) {
                o.sendTitle(message,null,0,time,0);
            }
        }else{
            plugin.getLogger().info(message);
        }
    }

    public static void sendTip(String msg,Player o){
        String message = TextFormat.colorize('&',msg);
        if(o != null){
            if(o.isOnline()) {
                o.sendTip(message);
            }
        }else{
            plugin.getLogger().info(message);
        }
    }

    public static void saveResource(String s, String s1, boolean b) {
        if(!isDisabled()){
            plugin.saveResource(s, s1, b);
        }
    }

    public static void saveResource(String s,  boolean b) {
        if(!isDisabled()){
            plugin.saveResource(s, b);
        }
    }

    public static File getDataFolder() {
        return plugin.getDataFolder();
    }

    public static void onDisable() {
        if(topManager != null){
            topManager.save();
        }
        if(dataManager != null){
            dataManager.save();
        }
        if(roomManager != null){
            for (GameRoomConfig roomConfig: roomManager.getRoomConfigs()){
                roomConfig.save();
            }
        }
    }


    private static void checkServer(){
        boolean ver = false;
        //双核心兼容
        try {
            Class<?> c = Class.forName("cn.nukkit.Nukkit");
            c.getField("NUKKIT_PM1E");
            ver = true;

        } catch (ClassNotFoundException | NoSuchFieldException ignore) { }
        try {
            Class<?> c = Class.forName("cn.nukkit.Nukkit");
            c.getField("NUKKIT").get(c).toString().equalsIgnoreCase("Nukkit PetteriM1 Edition");
            ver = true;
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignore) {
        }

        IS_PM1E = ver;
        if(ver){
            sendMessageToConsole("&e当前核心为 Nukkit PM1E");
        }else{
            sendMessageToConsole("&e当前核心为 Nukkit");
        }
    }


}
