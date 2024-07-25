package com.pfeiwu.qqautomation;

import mmarquee.automation.*;
import mmarquee.automation.controls.Application;
import mmarquee.automation.controls.Search;
import mmarquee.automation.controls.Window;
import mmarquee.automation.pattern.Invoke;
import mmarquee.uiautomation.TreeScope;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class NTQQAutomation {

    private final UIAutomation automation;

    private Application application;

    private Window window;

    // NTQQ 消息列表元素
    private Element messageList;

    // NTQQ 会话列表元素
    private Element sessionList;

    public NTQQAutomation(){
        automation = UIAutomation.getInstance();
    }

    protected Application getApplication() throws AutomationException{
        if (application==null) {
            application = automation.findProcess("QQ.exe");
        }
        return application;
    }

    protected Window getWindow() throws AutomationException {
        if (window==null){
            window = getApplication().getWindow(Search.getBuilder("QQ").build());
        }
        return window;
    }

    /**
     * 获取 NTQQ 消息列表元素
     * @return NTQQ 消息列表元素
     */
    protected Element getMessageList() throws AutomationException{
        if (messageList == null) {
            return getWindow().getElement().findFirst(new TreeScope(TreeScope.DESCENDANTS),automation.createNamePropertyCondition("消息列表"));
        }
        return messageList;
    }

    /**
     * 获取 NTQQ 会话列表元素
     * @return NTQQ 消息列表元素
     */
    protected Element getSessionList() throws AutomationException{
        if (sessionList == null) {
            return getWindow().getElement().findFirst(new TreeScope(TreeScope.DESCENDANTS),automation.createNamePropertyCondition("会话列表"));
        }
        return sessionList;
    }

    /**
     * 返回QQ聊天记录，是ScrollView目前展示出来以及周围一部分的文本内容，比较远的可能因为被销毁所以无法遍历到
     * @return QQ聊天记录文本列表
     */
    public List<String> getRenderedMessageTextList()throws AutomationException{
        return getMessageList().findAll(new TreeScope(TreeScope.DESCENDANTS),automation.createControlTypeCondition(ControlType.Text))
                .stream().map(
                        x->{
                            try{
                                return x.getName();
                            }catch (AutomationException e){
                                return null;
                            }
                        }
                ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 返回会话列表
     * @return
     * @throws AutomationException
     */
    public List<String> getSessionNameList()throws AutomationException {
        return getSessionList().findAll(new TreeScope(TreeScope.CHILDREN),automation.createControlTypeCondition(ControlType.Group))
                .stream().map(
                        x->{
                            try{
                                return x.getName().split(" ")[0];
                            }catch (AutomationException e){
                                return null;
                            }
                        }
                ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 切换到会话
     * @param sessionName
     * @throws AutomationException
     */
    public void switchToSession(String sessionName)throws AutomationException{
         Optional<Element> match = getSessionList().findAll(new TreeScope(TreeScope.CHILDREN),automation.createControlTypeCondition(ControlType.Group))
                .stream().filter(
                        x->{
                            try{
                                return x.getName().contains(sessionName+" ");
                            }catch (AutomationException e){
                                return false;
                            }
                        }
                ).findFirst();
         if (match.isPresent()){
             new Invoke(match.get()).invoke();
         }else{
             throw new AutomationException("Appointed session element not found");
         }
    }

//    /**
//     * 返回当前会话名称, 但是当前会话的hasKeyboardFocus不一定是true，这个方法用不了
//     * @return
//     * @throws AutomationException
//     */
//    public String getCurrentSessionName() throws AutomationException{
//        return getSessionList().findAll(new TreeScope(TreeScope.CHILDREN),automation.createControlTypeCondition(ControlType.Group))
//                .stream().filter(
//                        x->{
//                            try{
//                                Object hasKeyboardFocus = x.getPropertyValue(PropertyID.HasKeyboardFocus.getValue());
//                                return "-1".equals(String.valueOf(hasKeyboardFocus));
//                            }catch (AutomationException e){
//                                return false;
//                            }
//                        }
//                ).findFirst().map(
//                        x->{
//                            try{
//                                return x.getName().split(" ")[0];
//                            }catch (AutomationException e){
//                                return "";
//                            }
//                        }
//                ).orElse("");
//    }


}
