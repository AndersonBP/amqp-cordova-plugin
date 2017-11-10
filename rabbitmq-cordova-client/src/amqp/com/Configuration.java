package org.amqp.notification;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import amqp.com.ConfigurationFileService;

public class Configuration {

  private static Configuration INSTANCE;

  private Configuration(Context ctx){
    try {
    ConfigurationFileService _ConfigurationFileService = new ConfigurationFileService(ctx);
    JSONObject conf = ConfigurationFileService.getConfigurations();
        this.HostName = conf.getString("Host");
        this.User = conf.getString("UserName");
        this.Pass = conf.getString("Password");
        this.Port = Integer.parseInt(conf.getString("Port"));
        this.QueueName = conf.getString("QueueName");
        this.ExchangeName = conf.getString("ExchangeName");
        this.PackageName = conf.getString("PackageName");
        this.FieldContentMessage = conf.getString("FieldContentMessage");
        this.FieldContentHeader = conf.getString("FieldContentHeader");

    } catch (Throwable t) {
      Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  public static Configuration GetInstance(Context ctx){
    if(INSTANCE==null){
      INSTANCE = new Configuration(ctx);
    }
    return INSTANCE;
  }

  public String User;
  public String Pass;
  public String HostName;
  public String ExchangeName;
  public String QueueName;
  public String PackageName;
  public int Port;
  public String FieldContentMessage;
  public String FieldContentHeader;
}
