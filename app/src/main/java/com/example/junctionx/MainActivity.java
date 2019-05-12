package com.example.junctionx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private LinearLayout toolbar2;
    private TextView test;
    private AutoCompleteTextView act;
    private String position[] = {"건대입구역2호선","건국대학교병원","건국대학교 서울캠퍼스"};
    private SharedPreferences preference;
    private BottomNavigationView bottomNavigationView;
    private ImageButton back;
    private TextView from;
    private TextView to;
    private FrameLayout fl;
    private MediaPlayer mp;
    private int r;
    ArrayList<String> ttsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference = getSharedPreferences("a",MODE_PRIVATE);
        int firstviewshow = preference.getInt("First",0);
        firstviewshow = 0; //시연시 주석 풀기
        if(firstviewshow != 1){ //한번 봣으면 다신 안나옴
            Intent intent = new Intent(MainActivity.this,FirstStartActivity.class);
            startActivity(intent);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar2 = (LinearLayout) findViewById(R.id.toolbar2);
        act = (AutoCompleteTextView)findViewById(R.id.list_destination);
        fl = findViewById(R.id.test);
        //test = findViewById(R.id.test);
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        bottomNavigationView.setFocusable(false);
        bottomNavigationView.setFocusableInTouchMode(false);
        back = (ImageButton)findViewById(R.id.back);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //툴바 앱이름 빼기
        //getSupportActionBar().hide();   //툴바 안보이게 하기

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_saved:
                        return true;
                    case R.id.action_navigation:
                        return true;
                    case R.id.action_nearby:
                        return true;
                }
                return false;
            }
        });
        ttsList = new ArrayList<>();
        ttsList.add("만나서 반갑습니다");
        ttsList.add("뭐라구요?");
        ttsList.add("잘 모르겠습니다");
        ttsList.add("잘 안들립니다");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //챗봇 클릭
                r = new Random().nextInt(4);
                Snackbar.make(view, ttsList.get(r), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new texttospeech().execute();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //목적지 작성 후
        act.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode== KeyEvent.KEYCODE_ENTER) {//제출

                    toolbar.setVisibility(View.INVISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                    from.setText("용산전자상가");
                    to.setText(act.getText());
                    return true;
                }
                return false;
            }
        });
        act.setAdapter(new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,position));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar2.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class texttospeech extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            String clientId = "cy9p9zhn0k";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "WcYU3KUnqwrxdP0mvFzmagnShxhRmXpRNhTC1QSP";//애플리케이션 클라이언트 시크릿값";
            try {
                String text = URLEncoder.encode(ttsList.get(r), "UTF-8"); // 13자
                String apiURL = "https://naveropenapi.apigw.ntruss.com/voice/v1/tts";
                URL url = new URL(apiURL);
                HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
                // post request
                String postParams = "speaker=mijin&speed=0&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    InputStream is = con.getInputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];


                    File dir = new File(Environment.getExternalStorageDirectory()+"/", "Naver");
                    if(!dir.exists()){
                        dir.mkdirs();
                    }

                    // 파일명
                    String tempname = "naverttstemp";
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Naver/" + tempname + ".mp3");
                    f.createNewFile();

                    OutputStream outputStream = new FileOutputStream(f);
                    while ((read =is.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }

                    is.close();
                    String Path_to_file = Environment.getExternalStorageDirectory()+File.separator+"Naver/"+tempname+".mp3";
                    MediaPlayer audioPlay = new MediaPlayer();
                    audioPlay.setDataSource(Path_to_file);
                    audioPlay.prepare(); // 파일 재생 과정
                    audioPlay.start();
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());

                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }


}
