package com.jg.zhang.scrollview;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by jaxchow on 18/11/2015.
 */
public class ReboundScrollViewPackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        return new ArrayList<>();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return Arrays.<ViewManager>asList(
                new ReboundScrollViewManager()
        );
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Arrays.asList();
    }
}
