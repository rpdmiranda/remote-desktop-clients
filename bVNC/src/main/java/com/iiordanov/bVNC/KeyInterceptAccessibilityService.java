package com.iiordanov.bVNC;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.iiordanov.bVNC.input.KeyInterceptCallback;

import java.lang.ref.WeakReference;

public class KeyInterceptAccessibilityService extends AccessibilityService {

    private static final String TAG = "KeyInterceptA11y";

    private static volatile WeakReference<KeyInterceptCallback> callbackRef;
    private static volatile boolean clipboardFieldFocused;

    public static void setCallback(KeyInterceptCallback callback) {
        callbackRef = (callback != null) ? new WeakReference<>(callback) : null;
    }

    public static void setClipboardFocused(boolean focused) {
        clipboardFieldFocused = focused;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        KeyInterceptCallback callback = (callbackRef != null) ? callbackRef.get() : null;
        if (callback == null) {
            return false;
        }

        if (clipboardFieldFocused) {
            return false;
        }

        InputDevice device = event.getDevice();
        if (device == null || device.isVirtual()) {
            return false;
        }

        int metaState = event.getMetaState();
        boolean altPressed = (metaState & KeyEvent.META_ALT_ON) != 0;
        boolean metaPressed = (metaState & KeyEvent.META_META_ON) != 0;

        if (!altPressed && !metaPressed) {
            return false;
        }

        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_ALT_LEFT || keyCode == KeyEvent.KEYCODE_ALT_RIGHT
                || keyCode == KeyEvent.KEYCODE_META_LEFT || keyCode == KeyEvent.KEYCODE_META_RIGHT) {
            return false;
        }

        Log.d(TAG, "onKeyEvent: " + event);
        return callback.onInterceptedKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
