/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

/** 
 * This class notifies the receiver of incoming notifcation packets asynchronously.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationPacketListener implements PacketListener {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationPacketListener.class);

    private final XmppManager xmppManager;

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @SuppressLint("SimpleDateFormat") @Override
    public void processPacket(Packet packet) {
        Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

        if (packet instanceof NotificationIQ) {
            NotificationIQ notification = (NotificationIQ) packet;

            if (notification.getChildElementXML().contains(
                    "androidpn:iq:notification")) {
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                //String notificationTicker = notification.getTicker();
                String notificationUri = notification.getUri();
                String notificationImageUrl = notification.getImageUrl();

                //存放历史推送消息
                NotificationHistory notificationHistory = new NotificationHistory();
                notificationHistory.setApiKey(notificationApiKey);
                notificationHistory.setTitle(notificationTitle);
                notificationHistory.setMessage(notificationMessage);
                notificationHistory.setUri(notificationUri);
                notificationHistory.setImageUrl(notificationImageUrl);
                SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm");//格式化时间
                String time = sdf.format(new Date());//格式化时间，返回String
                notificationHistory.setTime(time);
                notificationHistory.save();//存放历史推送消息到数据库表当中
                //-----------
                
                Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
                intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
                intent.putExtra(Constants.NOTIFICATION_API_KEY,
                        notificationApiKey);
                intent
                        .putExtra(Constants.NOTIFICATION_TITLE,
                                notificationTitle);
                intent.putExtra(Constants.NOTIFICATION_MESSAGE,
                        notificationMessage);
                intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
                intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, notificationImageUrl);
                
                //                intent.setData(Uri.parse((new StringBuilder(
                //                        "notif://notification.androidpn.org/")).append(
                //                        notificationApiKey).append("/").append(
                //                        System.currentTimeMillis()).toString()));

                xmppManager.getContext().sendBroadcast(intent);
                //构建一条回执
                DeliverConfirmIQ deliverConfirmIQ = new DeliverConfirmIQ();
                deliverConfirmIQ.setUuid(notificationId);
                deliverConfirmIQ.setType(IQ.Type.SET);//发送消息的类型是set，（不是请求）
                //发送，通过asmack库提供的方法
                xmppManager.getConnection().sendPacket(deliverConfirmIQ);
            }
        }

    }

}
