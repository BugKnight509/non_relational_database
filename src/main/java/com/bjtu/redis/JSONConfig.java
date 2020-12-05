package com.bjtu.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JSONConfig {
    private static final String counterJSONPath = "D:\\xsh's work\\非关系型数据库\\redis-demo\\src\\main\\resources\\counters.json";
    private static final String actionJSONPath = "D:\\xsh's work\\非关系型数据库\\redis-demo\\src\\main\\resources\\actions.json";

    public Map<Integer,Counter> index_counter_map;
    public Map<Integer,Action> index_action_map;

    public JSONConfig() {
        System.out.println("JSONConfig start...");
        index_counter_map = new HashMap<>();
        index_action_map = new HashMap<>();
    }

    public void readCounters(){
        String total = readJSONFile(counterJSONPath);
        JSONObject jo = JSON.parseObject(total);
        JSONArray counters = jo.getJSONArray("counters");
        for(int i = 0; i < counters.size(); i++){
            JSONObject counterJSONObject = (JSONObject) counters.get(i);
            Counter counter = new Counter();
            counter.setIndex(counterJSONObject.getInteger("index"));
            counter.setKey(counterJSONObject.getString("key"));
            if( counterJSONObject.containsKey("counter_type")){counter.setCounterType(counterJSONObject.getInteger("counter_type"));}
            if(counter.getCounterType() == 1){ counter.setField(counterJSONObject.getString("field"));}
            if( counterJSONObject.containsKey("incrNum")){counter.setIncrNum(counterJSONObject.getInteger("incrNum"));}
            if( counterJSONObject.containsKey("timeInterval")){counter.setTimeInerval(counterJSONObject.getInteger("timeInterval"));}
            if( counterJSONObject.containsKey("dateFormat")){counter.setDateFormat(counterJSONObject.getString("dateFormat"));}
            if( counterJSONObject.containsKey("freq_select_type")){ counter.setFreq_select_type(counterJSONObject.getInteger("freq_select_type")); }
            if( counterJSONObject.containsKey("startTime")){ counter.setStartTime(counterJSONObject.getLong("startTime"));}
            index_counter_map.put(counter.getIndex(),counter);
        }
    }

    public void readActions(){
        String total = readJSONFile(actionJSONPath);
        JSONObject jo = JSON.parseObject(total);
        JSONArray actions = jo.getJSONArray("actions");
        for(int i = 0; i < actions.size(); i++){
            JSONObject actionJSONObject = (JSONObject) actions.get(i);
            Action temp = new Action();
            temp.index = actionJSONObject.getInteger("index");
            temp.name = (String)actionJSONObject.get("name");
            temp.tagetCounterIndex = actionJSONObject.getInteger("tagetCounterIndex");
            temp.function = (String)actionJSONObject.get("function");
            temp.parameter = null;
            if( actionJSONObject.containsKey("parameter")){
                JSONArray parameters = actionJSONObject.getJSONArray("parameter");
                temp.parameter = new String[parameters.size()];
                for (int j = 0; j < parameters.size(); j++){
                    temp.parameter[j] = (String) parameters.get(j);
                }
            }
            index_action_map.put(temp.index,temp);
        }
    }

    public void listCounters(){
        Set<Integer> keySet = index_action_map.keySet();
        Iterator<Integer> iterator = keySet.iterator();
        while (iterator.hasNext()){
            int temp = iterator.next();
            Counter counter = index_counter_map.get(temp);
            System.out.print(counter.getIndex()+" "+counter.getKey()+" ");
            if(counter.getCounterType() == 1){ System.out.print(counter.getField()); }
            System.out.println("");
        }
    }

    public void listAction(){
        Set<Integer> keySet = index_action_map.keySet();
        Iterator<Integer> iterator = keySet.iterator();
        while (iterator.hasNext()){
            int temp = iterator.next();
            Action action = index_action_map.get(temp);
            System.out.println(action.index+" "+action.name);
        }
    }

    private String readJSONFile(String path){
        String total = "";
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                total += line;
            }
            bufferedReader.close();
        }
        catch( IOException ex) {
        }
        return total;
    }

    class Action{
        int index;
        String name;
        int tagetCounterIndex;
        String function;
        String[] parameter;
    }
}
