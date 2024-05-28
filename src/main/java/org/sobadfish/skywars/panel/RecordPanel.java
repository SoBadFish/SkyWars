package org.sobadfish.skywars.panel;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import com.google.gson.JsonObject;
import net.easecation.ghosty.LevelRecordPack;
import org.sobadfish.skywars.manager.RecordManager;
import org.sobadfish.skywars.manager.TotalManager;
import org.sobadfish.skywars.panel.from.GameFrom;
import org.sobadfish.skywars.panel.from.button.BaseIButton;

import java.io.File;
import java.util.List;

/**
 * 录像管理面板
 *
 * @author LT_Name
 */
public class RecordPanel {

    private static final String PLUGIN_NAME = "&l&cS&6k&ey&aW&ba&9r";


    public static void disPlayerMenu(Player player) {

        GameFrom simple = new GameFrom(PLUGIN_NAME + " - 录像管理","请选择您要进行的操作", GameFrom.getRId());
        simple.add(new BaseIButton(new ElementButton("查看录像列表")) {
            @Override
            public void onClick(Player player) {
                RecordPanel.disRecordList(player);
            }
        });
        simple.add(new BaseIButton(new ElementButton("查找录像")) {
            @Override
            public void onClick(Player player) {
                RecordPanel.disRecordSearch(player);
            }
        });
        DisPlayWindowsFrom.FROM.put(player.getName(), simple);
//        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(PLUGIN_NAME + " - 录像管理", "请选择您要进行的操作");

        simple.disPlay(player);
    }

    public static void disRecordList(Player player) {
        GameFrom simple = new GameFrom(PLUGIN_NAME + " - 录像列表","请选择要操作的录像", GameFrom.getRId());
//        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(PLUGIN_NAME + " - 录像列表", "请选择要操作的录像");
        List<File> list = TotalManager.getRecordManager().getRecordFileList();
        if (list != null && !list.isEmpty()) {
            for (File file : list) {
                simple.add(new BaseIButton(new ElementButton(file.getName().split("\\.")[0])) {
                    @Override
                    public void onClick(Player player) {
                        disRecordInfo(player, file);
                    }
                } );
            }
        } else {
            simple.setContext("哎呀！还没有录像文件呢！快让玩家们玩一局吧！");
        }
        simple.add(new BaseIButton(new ElementButton("返回")) {
            @Override
            public void onClick(Player player) {
                RecordPanel.disPlayerMenu(player);
            }
        });
        DisPlayWindowsFrom.FROM.put(player.getName(), simple);
        simple.disPlay(player);
    }

    public static void disRecordSearch(Player player) {
//        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(PLUGIN_NAME + " - 录像查找");
//
//        //TODO
//
//        custom.showToPlayer(player);
    }

    public static void disRecordInfo(Player player, File file) {
        GameFrom simple = new GameFrom(PLUGIN_NAME + " - 录像详细信息","",GameFrom.getRId());
//        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(PLUGIN_NAME + " - 录像详细信息");
        LevelRecordPack recordPack = TotalManager.getRecordManager().getRecordPack(file);
        JsonObject metadata = recordPack.getMetadata();
        simple.setContext("录像名称：" + file.getName() +
                "\n录像房间：" + metadata.getAsJsonPrimitive("roomName") +
                "\n录像地图：" + metadata.getAsJsonPrimitive("roomWorld") +
                "\n录制完成时间：" + metadata.getAsJsonPrimitive("time"));
        simple.add(new BaseIButton(new ElementButton("播放录像")) {
            @Override
            public void onClick(Player player) {
                RecordManager.OK ok = TotalManager.getRecordManager().playRecord(file, player);
                if (ok.isOK()) {
                    player.sendMessage("开始播放录像：" + file.getName());
                } else {
                    player.sendMessage("播放录像失败！原因：" + ok.getMessage());
                }
            }
        });
        simple.add(new BaseIButton(new ElementButton("返回")) {
            @Override
            public void onClick(Player player) {
                disRecordList(player);
            }
        });
        DisPlayWindowsFrom.FROM.put(player.getName(), simple);
        simple.disPlay(player);
    }

}
