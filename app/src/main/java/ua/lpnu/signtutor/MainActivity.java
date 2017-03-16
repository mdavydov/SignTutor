package ua.lpnu.signtutor;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener
{

    ViewGroup m_my_list;
    SignVideoLibrary m_video_library = new SignVideoLibrary();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_my_list = (ViewGroup)findViewById(R.id.list);

        LayoutInflater l = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<String> string_list = new ArrayList<String>();
        string_list.addAll(m_video_library.m_word_to_video_map.keySet());
        Collections.sort(string_list, String.CASE_INSENSITIVE_ORDER);

        for(String s : string_list)
        {
            View item = l.inflate( R.layout.list_item, null);
            ((TextView)item.findViewById(R.id.itemtext)).setText(s);
            item.findViewById(R.id.morebutton).setOnClickListener(this);
            m_my_list.addView(item);
        }

        findViewById(R.id.detail).setOnClickListener(this);

        VideoView videoview = (VideoView) findViewById(R.id.video_player_view);


        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, "" + R.raw.prybyraty, duration);
        toast.show();

        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.prybyraty);
        //videoview.setVideoURI(uri);
        //videoview.start();

        videoview.setOnTouchListener(this);
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
