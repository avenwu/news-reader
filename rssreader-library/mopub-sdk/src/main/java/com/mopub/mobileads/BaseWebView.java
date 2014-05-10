package com.mopub.mobileads;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import com.mopub.mobileads.util.VersionCode;

import java.lang.reflect.Method;

public class BaseWebView extends WebView {
    public BaseWebView(Context context) {
        /*
         * Important: don't allow any WebView subclass to be instantiated using
         * an Activity context, as it will leak on Froyo devices and earlier.
         */
        super(context.getApplicationContext());
        enablePlugins(false);
    }

    protected void enablePlugins(final boolean enabled) {
        // Android 4.3 and above has no concept of plugin states
        if (VersionCode.currentApiLevel().isAtLeast(VersionCode.JELLY_BEAN_MR2)) {
            return;
        }

        if (VersionCode.currentApiLevel().isBelow(VersionCode.FROYO)) {
            getSettings().setPluginsEnabled(enabled);
        } else {
            try {
                Class<Enum> pluginStateClass = (Class<Enum>) Class.forName("android.webkit.WebSettings$PluginState");

                Class<?>[] parameters = {pluginStateClass};
                Method method = getSettings().getClass().getDeclaredMethod("setPluginState", parameters);

                Object pluginState = Enum.valueOf(pluginStateClass, enabled ? "ON" : "OFF");
                method.invoke(getSettings(), pluginState);
            } catch (Exception e) {
                Log.d("MoPub", "Unable to modify WebView plugin state.");
            }
        }
    }
}
