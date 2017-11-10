package org.amqp.notification;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import java.lang.Runnable;
import java.lang.Thread;
import java.lang.Exception;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;

import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import java.util.Random;
import android.content.Context;
import android.app.Activity;
import android.widget.Toast;

import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;

import org.amqp.notification.Push;
import org.amqp.notification.Configuration;
import org.json.JSONObject;

import amqp.com.ConfigurationFileService;

public class NotificationService extends Service{


  private Handler mHandler = new Handler();

  @Override
  public void onStart(Intent intent, int startId) {
    proceed();
  }

  protected void proceed() {

    new Thread(new Runnable() {

      @Override
      public void run() {

        try {
          ConfigurationFileService configurationFileService = new ConfigurationFileService(getApplicationContext());
          if(configurationFileService.existeConfigurationFile()){
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost(Configuration.GetInstance(getApplicationContext()).HostName);
            factory.setUsername(Configuration.GetInstance(getApplicationContext()).User);
            factory.setPassword(Configuration.GetInstance(getApplicationContext()).Pass);
            factory.setPort(Configuration.GetInstance(getApplicationContext()).Port);
            factory.setVirtualHost("/");

            Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            final String queue = Configuration.GetInstance(getApplicationContext()).QueueName;
            final String exchange = Configuration.GetInstance(getApplicationContext()).ExchangeName;

            channel.queueDeclare(queue, true, false, false, null);
            channel.exchangeDeclare(exchange, "direct", true);
            channel.queueBind(queue, exchange, queue);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queue, false, consumer);

            // Process deliveries
            while (true && connection.isOpen() && channel.isOpen()) {
              if(!configurationFileService.existeConfigurationFile()){
                if(connection.isOpen())
                  connection.close();
                if(channel.isOpen())
                  channel.close();
              }else{
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                final long tag = delivery.getEnvelope().getDeliveryTag();
                String message = new String(delivery.getBody(), "UTF-7");

                JSONObject jo = new JSONObject();
                try{
                  jo = new JSONObject(message);
                }catch (Exception e){
                }

                final String finalMessage = Configuration.GetInstance(getApplicationContext()).FieldContentMessage.isEmpty()?message:jo.getString(Configuration.GetInstance(getApplicationContext()).FieldContentMessage);
                final String contentTitle = Configuration.GetInstance(getApplicationContext()).FieldContentHeader.isEmpty()?"Nova Notificação!":jo.getString(Configuration.GetInstance(getApplicationContext()).FieldContentHeader);

                mHandler.post(new Runnable() {
                  @Override
                  public void run() {
                    try{
                      sendNotification(finalMessage, getApplicationContext(), Configuration.GetInstance(getApplicationContext()).PackageName, contentTitle);
                      channel.basicAck(tag, false);
                    }catch(Exception ex){
                      try{
                        channel.basicRecover();
                      }catch(Exception exx){}
                    }
                  }
                });
              }
            }
          }
        } catch (Exception e) {
          Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
      }
    }).start();
  }



  @Override
  public void onDestroy(){
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  public static void sendNotification(String message, Context ctx, String IntentPackage, String ContentTitle)
  {
    try {
      NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
        .setSmallIcon(android.R.drawable.ic_dialog_map)
        .setContentTitle(ContentTitle)
        .setContentText(message)
        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 })
        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
//        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

      Intent launchIntent = ctx.getPackageManager().getLaunchIntentForPackage(IntentPackage);
      if (launchIntent == null) {
        launchIntent = new Intent(Intent.ACTION_VIEW);
      }

      PendingIntent resultPendingIntent =
        PendingIntent.getActivity(ctx, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

      mBuilder.setContentIntent(resultPendingIntent);

      Random r = new Random();
      int notificationId = r.nextInt(8675310);

      NotificationManager mNotifyMgr = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);

      mNotifyMgr.notify(notificationId, mBuilder.build());
    }catch(Exception e){}
  }
}
