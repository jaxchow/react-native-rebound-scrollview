package com.jg.zhang.scrollview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
        //setContentView(this.initView());
        setContentView(this.relativeView());
	}

    protected ViewGroup relativeView(){
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams mRelativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout mRelativeLayout = new RelativeLayout(this);

        ReboundScrollView reboundScrollView= new ReboundScrollView(this);
        reboundScrollView.setLayoutParams(mRelativeParams);
        reboundScrollView.addView(this.initView());
        mRelativeLayout.addView(reboundScrollView);
        return mRelativeLayout;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    protected ViewGroup initView() {
        // 创建LinearLayout对象

        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout mLinearLayout = new LinearLayout(this);
        // 建立布局样式宽和高，对应xml布局中：

        // android:layout_width="fill_parent"
        // android:layout_height="fill_parent"
        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        // 设置方向，对应xml布局中：
        // android:orientation="vertical"


        // 创建TextView对象
        TextView mTextView = new TextView(this);
        // 设置文字
        mTextView.setText("hello world sdflasdj foasid fjasodf lsdjf lsdjf lksadjf lasfj ;adfjl asdjf ;djf al;dsjf alsdfjdslkfjas;dfjdasklfjsdlfkjasdfl; jsd flkjf skdlfj sldfj sldjflksjdfl sjdflsjflksdjf lkdsjf lsdjf ;asdfj ksdlfj sdlfd;fj s;dfa");
        mTextView.setTextSize(100);

        ImageView image= new ImageView(this);
        image.setLayoutParams(mLayoutParams);
        image.setImageResource(R.drawable.test);

        ImageView image2= new ImageView(this);
        image2.setImageResource(R.drawable.test2);
        image2.setLayoutParams(mLayoutParams);

        ImageView image3= new ImageView(this);
        image3.setImageResource(R.drawable.test3);
        image3.setLayoutParams(mLayoutParams);

        // 为其建立布局样式，对应xml布局中：
        // android:layout_width="fill_parent"
        // 在父类布局中添加它，及布局样式
        mLinearLayout.addView(image);
        mLinearLayout.addView(image2);
        mLinearLayout.addView(image3);
    //    mLinearLayout.addView(mTextView);

        return mLinearLayout;
    }

}
