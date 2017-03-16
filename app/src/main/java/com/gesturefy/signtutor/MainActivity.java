package com.gesturefy.signtutor;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener
{

    ViewGroup m_my_list;
    List<String> all_words_list = new ArrayList<String>();
    SignVideoLibrary m_video_library = new SignVideoLibrary();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_my_list = (ViewGroup)findViewById(R.id.list);

        all_words_list.addAll(m_video_library.m_word_to_video_map.keySet());
        Collections.sort(all_words_list, String.CASE_INSENSITIVE_ORDER);

        filterListWithPrefix("А");
        addTextChangedEvents();

        VideoView videoview = (VideoView) findViewById(R.id.video_player_view);
        videoview.setOnTouchListener(this);

        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.prybyraty);
        //videoview.setVideoURI(uri);
        //videoview.start();
    }


    protected void filterListWithPrefix(String prefix)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        m_my_list.removeAllViews();
        int num_added = 0;
        String lower_prefix = prefix.toLowerCase();

        for(String s : all_words_list)
        {
            if (s.toLowerCase().startsWith(lower_prefix))
            {
                ++num_added;
                View item = inflater.inflate(R.layout.list_item, null);
                ((TextView) item.findViewById(R.id.itemtext)).setText(s);
                item.findViewById(R.id.morebutton).setOnClickListener(this);
                m_my_list.addView(item);
                if (num_added>20) break;
            }
        }

        if (num_added==0)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    protected void addTextChangedEvents()
    {
        EditText search_text = (EditText) findViewById(R.id.text_entered);
        search_text.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") )
                {
                    filterListWithPrefix(s.toString());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (v.getId()==R.id.video_player_view)
        {
            VideoView videoview = (VideoView) findViewById(R.id.video_player_view);
            //videoview.stopPlayback();

            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.prybyraty);
            videoview.setVideoURI(uri);
            videoview.seekTo(0);
            videoview.start();
            return false;
        }
        return false;
    }

    public void onClick(View v)
    {
        if (v.getId()==R.id.morebutton)
        {
            // find parent
            View parent = (View)v.getParent();
            String name = ((TextView)parent.findViewById(R.id.itemtext)).getText().toString();
            int index = m_video_library.getResourceIdByName(name);

            if (index!=0)
            {
                VideoView videoview = (VideoView) findViewById(R.id.video_player_view);


                Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+index);
                videoview.setVideoURI(uri);
                videoview.seekTo(0);
                videoview.start();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, name, duration);
                toast.show();
            }

//            View detailview = findViewById(R.id.detail);
//            float width = findViewById(R.id.main_layout).getWidth();
//            TranslateAnimation anim = new TranslateAnimation(width, 0.0f, 0.0f, 0.0f);
//            anim.setDuration(300);
//            anim.setFillAfter(true);
//            detailview.bringToFront();
//            detailview.startAnimation(anim);
//            detailview.setVisibility(View.VISIBLE);
//            detailview.setEnabled(true);
        }
        else if (v.getId()==R.id.detail)
        {
            View detailview = v;
            TranslateAnimation anim = new TranslateAnimation(0.0f, detailview.getWidth(), 0.0f, 0.0f);
            anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    findViewById(R.id.listview).bringToFront();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            anim.setDuration(300);
            anim.setFillAfter(true);
            detailview.startAnimation(anim);
            detailview.setEnabled(false);
        }
    }

    public void init()
    {
    }
}
