package org.sobadfish.skywars.manager;

import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.skywars.manager.data.TagItemDataManager;
import org.sobadfish.skywars.room.config.ItemConfig;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这个方法封装着一些算法功能
 * @author Sobadfish
 * @date 2022/9/28
 */
public class FunctionManager {

    /**
     * 画一条进度条
     * ■■■■□□□□□□
     * @param progress 进度（百分比）
     * @param size 总长度
     * @param hasDataChar 自定义有数据图案 ■
     * @param noDataChar 自定义无数据图案 □
     * */
    public static String drawLine(float progress,int size,String hasDataChar,String noDataChar){
        int l = (int) (size * progress);
        int other = size - l;
        StringBuilder ls = new StringBuilder();
        return mLine(l, hasDataChar, noDataChar, ls, other);
    }
    /**
     * 画一条进度条
     * ■■■■□□□□□□
     * @param progress 进度（百分比）
     * @param size 总长度
     * @param hasDataChar 自定义有数据图案 ■
     * @param noDataChar 自定义无数据图案 □
     * */
    public static String drawLine(int progress,int size,String hasDataChar,String noDataChar){
        StringBuilder ls = new StringBuilder();
        int other = size - progress;
        return mLine(progress, hasDataChar, noDataChar, ls, other);
    }

    private static String mLine(int progress, String hasDataChar, String noDataChar, StringBuilder ls, int other) {
        if(progress > 0){
            for(int i = 0;i < progress;i++){
                ls.append(hasDataChar);
            }
        }
        StringBuilder others = new StringBuilder();
        if(other > 0){
            for(int i = 0;i < other;i++){
                others.append(noDataChar);
            }
        }
        return TextFormat.colorize('&',ls +others.toString());
    }

    /**
     * 获取百分比
     * 保留两位有效数字
     *
     * */
    public static double getPercent(int n,int max){
        double r = 0;
        if(n > 0){
            r = (double) n / (double) max;
        }
        return Double.parseDouble(String.format("%.2f",r));
    }

    /**
     * 文本居中
     * 这个是在总长内居中
     *
     * @param input 要居中的文本
     * @param lineWidth 文本总长度
     * @return 居中后的文本
     * */
    public static String getCentontString(String input,int lineWidth){
        input = input.replace(' ','$');
        return justify(input,lineWidth,'c').replace('$',' ');
    }
    /**
     * 字符串居中算法
     *
     * @param input 输入的字符串
     * @param lineWidth 一共多少行
     * @param just 对齐方法 l: 左对齐 c: 居中 r右对齐
     *
     * @return 对齐的字符串
     * */
    public static String justify(String input, int lineWidth, char just) {
        StringBuilder sb = new StringBuilder("");
        char[] inputText = input.toCharArray();
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < inputText.length; i++) {
            if (inputText[i] != ' ' && inputText[i] != '\n') {
                sb.append(inputText[i]);
            } else {
                inputText[i] = '\n';
                words.add(sb.toString());
                //clear content
                sb = new StringBuilder("");
            }
        }
        //add last word because the last char is not space/'\n'.
        words.add(sb.toString());
        for (String s : words) {
            if (s.length() >= lineWidth) {
                lineWidth = s.length();
            }
        }
        char[] output = null;
        StringBuilder sb2 = new StringBuilder("");
        StringBuilder line;
        for (String word : words) {
            line = new StringBuilder();
            for (int i = 0; i < lineWidth; i++) {
                line.append(" ");
            }
            switch (just) {
                case 'l':
                    line.replace(0, word.length(), word);
                    break;
                case 'r':
                    line.replace(lineWidth - word.length(), lineWidth, word);
                    break;
                case 'c':
                    //all the spaces' length
                    int rest = lineWidth - word.length();
                    int begin = 0;
                    if (rest % 2 != 0) {
                        begin = (rest / 2) + 1;
                    } else {
                        begin = rest / 2;
                    }
                    line.replace(begin, begin + word.length(), word);
                    break;
                default:break;
            }

            line.append('\n');
            sb2.append(line);
        }
        return sb2.toString();

    }

    /**
     * 将加载箱子内随机物品的配置文件
     *
     *
     * */
    public static Map<String, ItemConfig> buildItem(List<Map> itemList){
        LinkedHashMap<String,ItemConfig> configLinkedHashMap = new LinkedHashMap<>();
        for(Map<?,?> map: itemList){
            if(map.containsKey("block")) {
                String block = map.get("block").toString().split(":")[0];

                List<Item> items = new ArrayList<>();
                int time = -1;
                if(map.containsKey("time")){
                    time = Integer.parseInt(map.get("time").toString());
                }
                String name = "未命名";
                if(map.containsKey("items")) {
                    List<?> list = (List<?>) map.get("items");
                    for (Object s : list) {
                        items.addAll(stringToItemList(s.toString()));
                    }
                    Collections.shuffle(items);
                }
                if(map.containsKey("name")){
                    name = map.get("name").toString();
                }
                TotalManager.sendMessageToConsole("&e物品读取完成 &7("+block+")&r》"+items.size()+"《");
                configLinkedHashMap.put(block,new ItemConfig(block,name,time,items));
            }
        }
        TotalManager.sendMessageToConsole("&a物品加载完成: &r》"+configLinkedHashMap.size()+"《");
        return configLinkedHashMap;

    }

    public static List<Item> stringToItemList(String str){
        ArrayList<Item> items = new ArrayList<>();
        String[] sl = str.split("-");
        if(sl.length > 1){
            for(int i = 0;i < Integer.parseInt(sl[1]);i++){
                items.add(stringToItem(sl[0]));
            }
        }else{
            items.add(stringToItem(sl[0]));
        }
        return items;
    }

    public static Item stringToItem(String s){

        String[] sEnchant = s.split("&");
        String[] sList = s.split(":");
        if(sEnchant.length > 1){
            sList = sEnchant[0].split(":");
        }
        //tag物品截胡检测一下
        Item item = Item.get(0);
        TagItemDataManager itemDataManager = TotalManager.getTagItemDataManager();
        if(itemDataManager.hasItem(sList[0])){
            item = itemDataManager.getItem(sList[0]);
            int count = 1;
            if(sList.length > 1){
                count = Integer.parseInt(sList[1]);
            }
            item.setCount(count);
        }
        //TODO 特殊物品检测
        if(NbtItemManager.NBT_MANAGER.containsKey(sList[0])){
            item = NbtItemManager.NBT_MANAGER.get(sList[0]).getItem();
            int count = 1;
            if(sList.length > 1){
                count = Integer.parseInt(sList[1]);
            }
            item.setCount(count);
        }

        if(item.getId() == 0){
            try {
                item = Item.get(Integer.parseInt(sList[0]));
            }catch (Exception e){
                item = Item.fromString(sList[0].replace(".",":"));
            }
            if(sList.length > 1){
                item.setDamage(Integer.parseInt(sList[1]));
                if(sList.length > 2){
                    item.setCount(Integer.parseInt(sList[2]));
                }else{
                    item.setCount(1);
                }
            }
        }
        //TODO 给物品一个附魔 先做一个TAG标记 然后在设置箱子内附魔

        if(sEnchant.length > 1){
            CompoundTag compoundTag = item.getNamedTag();
            ListTag<StringTag> rEnchants =new ListTag<>("ROUND_ENCHANT");
            if(compoundTag == null){
                compoundTag = new CompoundTag();
            }
            for(int i = 1;i < sEnchant.length;i++){
                String[] dEnchant = sEnchant[i].split(":");
                String dLevel = "1~1";
                if(dEnchant.length > 1){
                    String[] rLevel = dEnchant[1].split("~");
                    if(rLevel.length > 1){
                        dLevel =  dEnchant[1];
                    }
                }
                rEnchants.add(new StringTag(dEnchant[0],dEnchant[0]+"&"+dLevel));
            }
            compoundTag.putList(rEnchants);
            item.setNamedTag(compoundTag);

        }


        return item;

    }
    private static ExecutorService service = Executors.newSingleThreadExecutor();



    public static List<Position> spawnGlass(Position position){
        ArrayList<Position> glasses = new ArrayList<>();
        glasses.add(position);
        glasses.add(position.add(1,1,0));
        glasses.add(position.add(1,2,0));
        glasses.add(position.add(-1,1,0));
        glasses.add(position.add(-1,2,0));

        glasses.add(position.add(0,1,1));
        glasses.add(position.add(0,2,1));
        glasses.add(position.add(0,1,-1));
        glasses.add(position.add(0,2,-1));
        glasses.add(position.add(0,3,0));
        return glasses;
    }

    /**
     * 将秒转换为时间显示格式 00:00
     * @param s 秒
     * @return 时间显示格式
     * */
    public static String formatTime(int s){
        int min = s / 60;
        int ss = s % 60;
        String mi = min+"";
        String sss = ss+"";
        if(min < 10){
            mi = "0"+mi;
        }
        if(ss < 10){
            sss = "0"+ss;
        }
        if(min > 0){

            return mi+":"+sss;
        }else{
            return "00:"+sss+"";
        }

    }

}
