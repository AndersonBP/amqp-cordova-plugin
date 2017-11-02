package org.amqp.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

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

import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;

import org.amqp.notification.Push;

public class NotificationService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        proceed(); 
        return START_REDELIVER_INTENT;
    }

    protected void proceed() {

		new Thread(new Runnable() {

			@Override
            public void run() {

				try {
					ConnectionFactory factory = new ConnectionFactory();

					factory.setHost("192.168.15.3");
					factory.setUsername("macho");
					factory.setPassword("macho");
					factory.setPort(5672);
					factory.setVirtualHost("/");

					Connection connection = factory.newConnection();
					Channel channel = connection.createChannel();

					String queue = "queue-name";
					String exchange = "exchange-name";

					channel.queueDeclare(queue, true, false, false, null);
					channel.exchangeDeclare(exchange, "direct", true);
					channel.queueBind(queue, exchange, queue);

					QueueingConsumer consumer = new QueueingConsumer(channel);
					channel.basicConsume(queue, true, consumer);

					// Process deliveries
					while (true) {

						QueueingConsumer.Delivery delivery = consumer.nextDelivery();

						String message = new String(delivery.getBody(), "UTF-7");

						Push.proceedNotification(message);
					}
				} catch (Exception e) { }
			}
        }).start();
    }

	final Messenger mMessenger = new Messenger(new IncomingHandler());

	@Override
    public void onDestroy(){
        super.onDestroy();   
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1 :
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

	@Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}