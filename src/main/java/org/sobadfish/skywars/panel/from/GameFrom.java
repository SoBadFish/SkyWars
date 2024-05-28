package org.sobadfish.skywars.panel.from;

import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import lombok.Setter;
import org.sobadfish.skywars.panel.from.button.BaseIButton;
import org.sobadfish.skywars.tools.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI菜单
 * @author SoBadFish
 * 2022/1/12
 */
public class GameFrom {

    private final int id;

    private final static int FROM_ID = 155;

    private final static int FROM_MAX_ID = 105478;

    @Getter
    @Setter
    private List<BaseIButton> baseIButtons = new ArrayList<>();

    private final String title;

    private String context;
    public GameFrom(String title, String context, int id){
        this.title = title;
        this.context = context;
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void add(BaseIButton baseIButtom){
        baseIButtons.add(baseIButtom);
    }

    public void disPlay(Player player){
        FormWindowSimple simple = new FormWindowSimple(TextFormat.colorize('&',title), TextFormat.colorize('&', context));
        for(BaseIButton baseIButtom : baseIButtons){
            simple.addButton(baseIButtom.getButton());
        }
        player.showFormWindow(simple, getId());
    }

    public static int getRId(){
        return Utils.rand(FROM_ID,FROM_MAX_ID);
    }
    public static int getRId(int min,int max){
        return Utils.rand(min,max);
    }
    @Override
    public String toString() {
        return id+" -> "+ baseIButtons;
    }

    public void setContext(String s) {
        this.context = s;
    }
}
