package com.pfeiwu.qqautomation;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        NTQQAutomation automation = new NTQQAutomation();

        try{

            //API - 获取当前会话名称  --  不行，拿不到DOM class
//            automation.getCurrentSessionName();
//            System.out.println("当前会话名称 : " + automation.getCurrentSessionName());


            // API - 获取会话列表
            List<String> sessionNameList = automation.getSessionNameList();
            System.out.println("--会话列表START--------------------------------------------");
            for (String msg: sessionNameList){
                System.out.println(msg);
            }
            System.out.println("--会话列表END--------------------------------------------");

            // API - 切换到会话
            automation.switchToSession("小鹤双拼新手村");

            // API - 获取聊天记录
            List<String> renderedMessageTextList = automation.getRenderedMessageTextList();
            System.out.println("--聊天记录START--------------------------------------------");
            for (String msg: renderedMessageTextList){
                System.out.println(msg);
            }
            System.out.println("--聊天记录END--------------------------------------------");



        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
