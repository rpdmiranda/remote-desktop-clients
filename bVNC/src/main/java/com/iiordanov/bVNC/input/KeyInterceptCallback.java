package com.iiordanov.bVNC.input;

import android.view.KeyEvent;

public interface KeyInterceptCallback {
    /** @return true if the event was consumed */
    boolean onInterceptedKeyEvent(KeyEvent event);
}
