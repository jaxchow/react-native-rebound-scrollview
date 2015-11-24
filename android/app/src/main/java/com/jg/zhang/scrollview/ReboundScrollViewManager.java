package com.jg.zhang.scrollview;

import android.util.Log;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.views.scroll.ReactScrollView;
import com.facebook.react.views.scroll.ReactScrollViewCommandHelper;

import java.util.Map;

import javax.annotation.Nullable;


/**
 * Created by jaxchow on 18/11/2015.
 */
public class ReboundScrollViewManager extends ViewGroupManager<ReboundScrollView> implements ReactScrollViewCommandHelper.ScrollCommandHandler<ReactScrollView> {

    public static final String REACT_CLASS = "ReboundScrollView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ReboundScrollView createViewInstance(ThemedReactContext context) {
        return new ReboundScrollView(context);
    }
    /*
    @UIProp(UIProp.Type.ARRAY)
    public static final String PROP_VALUES = "values";

    @UIProp(UIProp.Type.NUMBER)
    public static final String PROP_SELECTED = "selected";
    */
    @Override
    public void updateView(ReboundScrollView view, CatalystStylesDiffMap props) {
        super.updateView(view, props);
        /*
        if (props.hasKey(PROP_VALUES)) {
            view.setValues(props.getArray(PROP_VALUES));
        }

        if (props.hasKey(PROP_SELECTED)) {
            view.setSelected(props.getInt(PROP_SELECTED, 0));
        }
        */
        //System.out.print(view.getChildAt(0));
    }

    public void setRemoveClippedSubviews(ReactScrollView view, boolean removeClippedSubviews) {
        view.setRemoveClippedSubviews(removeClippedSubviews);
    }

    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return ReactScrollViewCommandHelper.getCommandsMap();
    }

    public void receiveCommand(ReactScrollView scrollView, int commandId, @Nullable ReadableArray args) {
        ReactScrollViewCommandHelper.receiveCommand(this, scrollView, commandId, args);
    }

    public void scrollTo(ReactScrollView scrollView, ReactScrollViewCommandHelper.ScrollToCommandData data) {
        scrollView.smoothScrollTo(data.mDestX, data.mDestY);
    }

    public void scrollWithoutAnimationTo(ReactScrollView scrollView, ReactScrollViewCommandHelper.ScrollToCommandData data) {
        scrollView.scrollTo(data.mDestX, data.mDestY);
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.builder().put("topScroll", MapBuilder.of("registrationName", "onScroll")).put("topScrollBeginDrag", MapBuilder.of("registrationName", "onScrollBeginDrag")).put("topScrollEndDrag", MapBuilder.of("registrationName", "onScrollEndDrag")).put("topScrollAnimationEnd", MapBuilder.of("registrationName", "onScrollAnimationEnd")).put("topMomentumScrollBegin", MapBuilder.of("registrationName", "onMomentumScrollBegin")).put("topMomentumScrollEnd", MapBuilder.of("registrationName", "onMomentumScrollEnd")).build();
    }

}
