package com.example.junctionx;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstStartActivity extends AppCompatActivity {

    private ViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
    }
    private View.OnClickListener mCloseButtonClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int infoFirst = 1;
            SharedPreferences a = getSharedPreferences("a",MODE_PRIVATE);
            SharedPreferences.Editor editor = a.edit();
            editor.putInt("First",infoFirst);
            editor.commit();
            finish();

        }
    };
    private class PagerAdapterClass extends PagerAdapter{

        private LayoutInflater mInflater;
        public PagerAdapterClass(Context c){
            super();
            mInflater = LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Object instantiateItem(ViewGroup pager, int position) {
            View v = null;
//position별로 페이지 각 해당 페이지에 불러올 레이아웃을 설정.
            if (position == 0) {
               v = mInflater.inflate(R.layout.firststartview1, null);
               v.findViewById(R.id.first);
            } else if (position == 1) {
               v = mInflater.inflate(R.layout.firststartview2, null);
               v.findViewById(R.id.second);
               v.findViewById(R.id.close).setOnClickListener(mCloseButtonClick);
            }
            ((ViewGroup) pager).addView(v, 0);
            return v;
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewGroup)container).removeView((View)object);
        }
    }
}

