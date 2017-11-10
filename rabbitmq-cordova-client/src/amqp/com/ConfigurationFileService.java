package amqp.com;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by anderson.peres on 08/11/2017.
 */

public class ConfigurationFileService {

  private static String FileName = "config.txt";

  private static Context context;

  public ConfigurationFileService(Context ctx){
        context=ctx;
  }

  public static JSONObject getConfigurations() {

    JSONObject ret = new JSONObject();

    try {
      InputStream inputStream = context.openFileInput(FileName);

      if ( inputStream != null ) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ( (receiveString = bufferedReader.readLine()) != null ) {
          stringBuilder.append(receiveString);
        }
        inputStream.close();
        ret  = new JSONObject( stringBuilder.toString());
      }
    }
    catch (Exception e) {
      Log.e("login activity", "File not found: " + e.toString());
    }
    return ret;
  }

  public void saveConfiguration(String data) {
    try {
      clearConfigurations();
      File file = new File(context.getFilesDir(),FileName);
      if(!file.exists()){
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FileName, Context.MODE_PRIVATE));
      outputStreamWriter.write(data);
      outputStreamWriter.close();
      }
    }
    catch (IOException e) {
      Log.e("Exception", "File write failed: " + e.toString());
    }
  }

  public  void clearConfigurations(){
    File file = new File(context.getFilesDir(),FileName);
    if(file.exists()){
      file.delete();
    }
  }

  public  boolean existeConfigurationFile(){
    File file = new File(context.getFilesDir(),FileName);

    return  file.exists();
  }
}
