package com.ijoomer.components.jbolochat;import android.app.Activity;import android.app.Notification;import android.app.NotificationManager;import android.app.PendingIntent;import android.content.Context;import android.content.Intent;import com.ijoomer.library.jbolochat.JBoloChatDataProvider;import com.ijoomer.src.R;import com.ijoomer.weservice.WebCallListener;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Timer;import java.util.TimerTask;/** * This Class Contains All Method Related To JBoloChatManager. * * @author tasol * */public class JBoloChatManager {    private static Context context;    private static JBoloChatManager chatManager;    private Timer timer;    private JBoloChatDataProvider provider;    private ArrayList<JBoloChatHandler> jBoloChatHandlersList = new ArrayList<JBoloChatHandler>();    private long pollingInterval=30000;    private String currentChatId;    /**     * Private constructor     */    private JBoloChatManager(){    }    /**     * This method used to get polling interval.     * @return represented {@link Long}     */    public long getPollingInterval() {        return pollingInterval;    }    /**     * This method used to change polling interval.     * @param pollingInterval represented polling interval     */    public void changePollingInterval(long pollingInterval) {        this.pollingInterval = pollingInterval;        if(timer!=null){            timer.cancel();            timer = new Timer();            startPolling();        }    }    public void stopPolling() {        if(timer!=null){            timer.cancel();        }    }    /**     * This method used to get current chat id.     * @return represented {@link String}     */    public String getCurrentChatId() {        return currentChatId;    }    /**     * This method used to set current chat id.     * @param currentChatId represented chat id     */    public void setCurrentChatId(String currentChatId) {        this.currentChatId = currentChatId;    }    /**     * This method used to get JBoloChatManager singleton instance     * @param mContext represented {@link android.content.Context}     * @return represented {@link com.ijoomer.components.jbolochat.JBoloChatManager}     */    public static JBoloChatManager getInstance(Context mContext){        context = mContext;        if(chatManager==null){            chatManager = new JBoloChatManager();        }        return chatManager;    }    /**     * This method used to add JBoloChatHandler.     * @param handler represebted {@link JBoloChatHandler}     */    public void addJBoloChatHandler(JBoloChatHandler handler){        jBoloChatHandlersList.add(handler);    }    /**     * This method used to start polling.     */    public void startPolling(){        if(timer==null){            timer = new Timer();        }else{            timer.cancel();            timer = new Timer();        }        provider= new JBoloChatDataProvider(context);        timer.scheduleAtFixedRate(new TimerTask() {            @Override            public void run() {                ((Activity)context).runOnUiThread(new Runnable() {                    @Override                    public void run() {                        provider.polling(new WebCallListener() {                            @Override                            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {                                if (responseCode != 123) {                                    sendPush((List<String>) data2);                                    for (JBoloChatHandler handler : jBoloChatHandlersList) {                                        handler.onUserOnlineUpdated(provider.getOnlineUserDB());                                        handler.onMessageUpdate(provider.getMessageDB(getCurrentChatId()));                                    }                                }                            }                            @Override                            public void onProgressUpdate(int progressCount) {                            }                        });                    }                });            }        },0,getPollingInterval());    }    /**     * This method used to send internal push.     * @param pushList represented push list     */    private void sendPush(List<String> pushList){        for (String push : pushList){            if(push.contains("=")){                Intent intent = new Intent(context,JboloMessageListActivity.class);                String[] pushArray = push.split("=");                intent.putExtra("IN_NODEID",pushArray[2]);                intent.putExtra("IN_MESSAGE_TYPE",pushArray[3]);                intent.putExtra("IN_CHAT_USER_NAME",pushArray[0]);                addToNotificationBar("New Message",pushArray[0]+" "+pushArray[1],"",intent,Integer.parseInt(pushArray[2]));            }else{                Intent intent = new Intent(context,JboloOnlineUserListActivity.class);                addToNotificationBar(push,push,"",intent,0);            }        }    }    /**     * This method used to add custom notification.     *     * @param ticker     *            represented notification ticker     * @param title     *            represented notification title     * @param message     *            represented notification message     * @param index     *            represented notification index     */    public static void addToNotificationBar(String ticker, String title,                                            String message,Intent intent,int index) {        long when = System.currentTimeMillis();        int icon = R.drawable.ijoomer_push_notification_icon;        NotificationManager notificationManager = (NotificationManager) context                .getSystemService(Context.NOTIFICATION_SERVICE);        PendingIntent contentIntent = PendingIntent.getActivity(                context, (int) (Math.random() * 100), intent, PendingIntent.FLAG_UPDATE_CURRENT);        Notification notification = new Notification(icon, ticker, when);        notification.setLatestEventInfo(context, title, message,                contentIntent);        notification.flags = Notification.FLAG_AUTO_CANCEL;        notificationManager.notify(index==0?(int) (Math.random() * 100):index, notification);    }}