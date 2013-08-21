// Copyright (c) 2013 Intel Corporation. All rights reserved
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.xwalk.core.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.WebStorage;
import android.webkit.GeolocationPermissions;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.widget.EditText;

import org.xwalk.core.JsPromptResult;
import org.xwalk.core.JsResult;
import org.xwalk.core.R;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebChromeClient;

public class XWalkDefaultWebChromeClient extends XWalkWebChromeClient {

    // Strings for displaying Dialog
    private static String mJSAlertTitle;
    private static String mJSConfirmTitle;
    private static String mJSPromptTitle;
    private static String mOKButton;
    private static String mCancelButton;

    private Context mContext;
    private AlertDialog mDialog;
    private EditText mPromptText;

    public XWalkDefaultWebChromeClient(Context context) {
        mContext = context;
        initResources(context);
    }

    private static void initResources(Context context) {
        if (mJSAlertTitle != null) return;
        mJSAlertTitle = context.getString(R.string.js_alert_title);
        mJSConfirmTitle = context.getString(R.string.js_confirm_title);
        mJSPromptTitle = context.getString(R.string.js_prompt_title);
        mOKButton = context.getString(android.R.string.ok);
        mCancelButton = context.getString(android.R.string.cancel);
    }

    @Override
    public boolean onJsAlert(XWalkView view, String url, String message,
            final JsResult result) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setTitle(mJSAlertTitle)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(mOKButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                        dialog.dismiss();
                    }
                });
        mDialog = dialogBuilder.create();
        mDialog.show();
        return false;
    }

    @Override
    public boolean onJsConfirm(XWalkView view, String url, String message,
            final JsResult result) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setTitle(mJSConfirmTitle)
                .setMessage(message)
                .setPositiveButton(mOKButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(mCancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                        dialog.dismiss();
                    }
                });
        mDialog = dialogBuilder.create();
        mDialog.show();
        return false;
    }

    @Override
    public boolean onJsPrompt(XWalkView view, String url, String message,
            String defaultValue, final JsPromptResult result) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setTitle(mJSPromptTitle)
                .setMessage(message)
                .setPositiveButton(mOKButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm(mPromptText.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(mCancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                        dialog.dismiss();
                    }
                });
        mPromptText = new EditText(mContext);
        mPromptText.setVisibility(View.VISIBLE);
        mPromptText.setText(defaultValue);
        mPromptText.selectAll();

        dialogBuilder.setView(mPromptText);
        mDialog = dialogBuilder.create();
        mDialog.show();
        return false;
    }
}
