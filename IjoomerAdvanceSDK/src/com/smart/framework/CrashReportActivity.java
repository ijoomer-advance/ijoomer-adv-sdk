package com.smart.framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.ijoomer.src.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CrashReportActivity extends Activity {

    public static final String EXTRA_STACKTRACE = "hbk.crash.stackTrace";

    private String stackTrace;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stackTrace = getIntent().getStringExtra(EXTRA_STACKTRACE);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);

        int imageResource = android.R.drawable.stat_sys_warning;
        Drawable image = this.getResources()
                .getDrawable(imageResource);

        builder.setTitle("Application Crashed!!!")
                .setMessage("Please Send " + SmartApplication.REF_SMART_APPLICATION.LOGFILENAME + " From (SDCARD or Internal Memory) Root Directory To Are Developer Team.")
                .setIcon(image)
                .setCancelable(false)
                .setNeutralButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int id) {
                                appendLog(stackTrace);
                                Intent intent = new Intent("clearStackActivity");
                                intent.setType("text/plain");
                                sendBroadcast(intent);
                                finish();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }

    private void appendLog(String text) {
        File logFile = new File("sdcard/" + SmartApplication.REF_SMART_APPLICATION.LOGFILENAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
