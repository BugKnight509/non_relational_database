package com.bjtu.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.regex.Pattern;

@Controller
public class commandLineRunner implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;
    private JSONConfig jsonConfig;

    @Override
    public void run(String... strings) throws Exception {
        jsonConfig = (JSONConfig) applicationContext.getBean("JSONConfig");
        jsonConfig.readCounters();
        jsonConfig.readActions();
        System.out.println("Action:");
        jsonConfig.listAction();
        System.out.println("Counter:");
        jsonConfig.listCounters();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Command Line Running");
        String input;
        while (!(input = scanner.next()).equals("#")){
            if(isInteger(input)){
                JSONConfig.Action action = jsonConfig.index_action_map.get(Integer.parseInt(input));
                handleAction(action);
            }
        }
    }

    public void handleAction(JSONConfig.Action action){
        Scanner scanner = new Scanner(System.in);
        int counter_index = action.tagetCounterIndex;
        Counter counter = jsonConfig.index_counter_map.get(counter_index);
        String[] parameters = null;
        String input = "";
        if(action.parameter != null){
            parameters = action.parameter;
        }
        switch (action.function){
            case "incr":
                counter.incr();
                break;
            case "getIncr":
                System.out.print("Key:"+counter.getKey()+" ");
                if(counter.getCounterType() == 1){System.out.print("Field:"+counter.getField()+" ");}
                System.out.println("Count number:"+counter.getIncr());
                break;
            case "freq":
                String from,to;
                if(parameters == null){
                    System.out.println("Input the start point.");
                    from = scanner.next();
                    System.out.println("Input the end point.");
                    to = scanner.next();
                }else {
                    from = parameters[0];
                    to = parameters[1];
                }
                counter.freq(from,to);
                break;
            case "str":
                String str;
                if(parameters == null){
                    System.out.println("Input the string.");
                    str = scanner.next();
                }else {
                    str = parameters[0];
                }
                counter.str(str);
                break;
            case "getStr":
                System.out.println("Key:"+counter.getKey()+" string:"+counter.getStr());
                break;
            case "list":
                LinkedList<String> list = new LinkedList<>();
                if(parameters == null){
                    System.out.println("Input the list.");
                    while (!scanner.next().equals("")){
                        list.add(scanner.next());
                    }
                }else {
                    for(int i = 0 ; i < parameters.length; i++){
                        list.add(parameters[i]);
                    }
                }
                counter.list(list);
                break;
            case "getList":
                LinkedList<String> show_list = (LinkedList<String>) counter.getList();
                System.out.println("Key:"+counter.getKey()+" list:");
                for(int i = 0,size = show_list.size(); i < size; i++){
                    System.out.println(show_list.get(i));
                }
                break;
            case "set":
                HashSet<String> set = new HashSet<>();
                if(parameters == null){
                    System.out.println("Input the list.");
                    while (!scanner.next().equals("")){
                        set.add(scanner.next());
                    }
                }else {
                    for(int i = 0 ; i < parameters.length; i++){
                        set.add(parameters[i]);
                    }
                }
                counter.set(set);
                break;
            case "getSet":
                HashSet<String> show_set = (HashSet<String>) counter.getSet();
                System.out.println("Key:"+counter.getKey()+" set:");
                Iterator<String> iterator_set = show_set.iterator();
                while (iterator_set.hasNext()){
                    System.out.println(iterator_set.next());
                }
                break;
            case "zset":
                SortedSet<String> zset = new TreeSet<>();
                if(parameters == null){
                    System.out.println("Input the list.");
                    while (!scanner.next().equals("")){
                        zset.add(scanner.next());
                    }
                }else {
                    for(int i = 0 ; i < parameters.length; i++){
                        zset.add(parameters[i]);
                    }
                }
                counter.zset(zset);
                break;
            case "getZset":
                HashSet<String> show_zset = (HashSet<String>) counter.getZset();
                System.out.println("Key:"+counter.getKey()+" set:");
                Iterator<String> iterator_zset = show_zset.iterator();
                while (iterator_zset.hasNext()){
                    System.out.println(iterator_zset.next());
                }
                break;
            case "setIncrNum":
                long incrNum = 1;
                if(parameters == null){
                    System.out.println("Input increase number:");
                    input = scanner.next();
                    if(isInteger(input)){ incrNum = Integer.parseInt(input); }
                }else {
                    incrNum = Long.parseLong(parameters[0]);
                }
                counter.setIncrNum(incrNum);
                break;
            case "setTimeInerval":
                long timeInerval = 3600;
                if(parameters == null){
                    System.out.println("Input time inerval:");
                    input = scanner.next();
                    if(isInteger(input)){ timeInerval = Integer.parseInt(input); }
                }else {
                    timeInerval = Long.parseLong(parameters[0]);
                }
                counter.setTimeInerval(timeInerval);
                break;
            case "setStartTime":
                String startTime = counter.getFormatedStartTime();
                if(parameters == null){
                    System.out.println("Input start time: (Date Format:"+counter.getDateFormat()+")");
                    input = scanner.next();
                    startTime = input;
                }else {
                    startTime = parameters[0];
                }
                counter.setFormatedStartTime(startTime);
                break;
            case "setDateFormat":
                String dateFormat = counter.getDateFormat();
                if(parameters == null){
                    System.out.println("Input date Format:");
                    input = scanner.next();
                    dateFormat = input;
                }else {
                    dateFormat = parameters[0];
                }
                counter.setDateFormat(dateFormat);
                break;
            default:
                break;
        }
    }


    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
