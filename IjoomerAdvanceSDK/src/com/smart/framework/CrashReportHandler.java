package com.smart.framework;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashReportHandler implements UncaughtExceptionHandler {

    public static void attach(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(
                new CrashReportHandler(context)
        );
    }

    ///////////////////////////////////////////// implementation

    private CrashReportHandler(Context context) {
        m_context = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        Intent intent = new Intent(m_context, CrashReportActivity.class);
        intent.putExtra(CrashReportActivity.EXTRA_STACKTRACE, stackTrace.toString());
        m_context.startActivity(intent);

        // from RuntimeInit.crash()
        Process.killProcess(Process.myPid());
        System.exit(10);
    }

    private Context m_context;

}
