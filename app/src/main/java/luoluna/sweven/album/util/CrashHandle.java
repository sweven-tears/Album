package luoluna.sweven.album.util;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import androidx.annotation.NonNull;

/**
 * Created by Sweven on 2020/7/25--22:39.
 * Email: sweventears@163.com
 */
public class CrashHandle implements Thread.UncaughtExceptionHandler {
    private static final String EXCEPTION_LOG_PATH = Environment.getExternalStorageState() + "/album/log.txt";


    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        File file = new File(EXCEPTION_LOG_PATH);
        try {
            if (file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, true);
            writer.append(Calendar.getInstance().toString());
            writer.append("\n\r");
            StringWriter st = new StringWriter();
            e.printStackTrace(new PrintWriter(st));
            writer.append(st.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
