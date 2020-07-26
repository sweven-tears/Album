package luoluna.sweven.album.util;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by Sweven on 2020/7/25--22:39.
 * Email: sweventears@163.com
 */
public class CrashHandle implements Thread.UncaughtExceptionHandler {


    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        File file = new File("S://info.txt");
        try {
            if (file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file,true);
            writer.append(Calendar.getInstance().toString());
            writer.append("\n\r");
            StringWriter st  = new StringWriter();
            e.printStackTrace(new PrintWriter(st));
            writer.append(st.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
