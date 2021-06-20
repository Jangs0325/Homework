package com.swufe.work;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv_main_diary;
    private ImageView iv_main_write;
    private DiaryListAdapter diaryListAdapter;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver DiaryReceiver = new BroadcastReceiver(){


        @Override
        public void onReceive(Context context, Intent intent) {

        }

        public <Intent> void onReceive(Context context, Intent intent) {
            refresh();
        }
    };
    private String title;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initView();
            initData();
            initListener();
            refresh();
        }

        private void refresh() {
            List<Diaryinfo> workinfos = Model.getInstance().getDiaryTableDao().getDiaryByTitle();
            diaryListAdapter.refresh(workinfos);
        }

        private void initListener() {
            iv_main_write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                    startActivity(intent);
                }
            });
        }

        private void initData() {
            diaryListAdapter = new DiaryListAdapter(MainActivity.this);
            lv_main_diary.setAdapter(diaryListAdapter);
            lv_main_diary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Diaryinfo diaryinfo = (Diaryinfo) adapterView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, DiaryDetailActivity.class);
                    intent.putExtra("title", diaryinfo.getTitle());
                    intent.putExtra("content", diaryinfo.getContent());
                    startActivity(intent);
                }
            });
            mLBM = LocalBroadcastManager.getInstance(MainActivity.this);
            mLBM.registerReceiver(DiaryReceiver, new IntentFilter("diary_change"));
            //绑定listview和contextmenu
            registerForContextMenu(lv_main_diary);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            Diaryinfo diaryinfo = (Diaryinfo) lv_main_diary.getItemAtPosition(position);
            title = diaryinfo.getTitle();
            MainActivity.this.getMenuInflater().inflate(R.menu.delete, menu);
        }

        @Override
        public boolean onContextItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.diary_delete) {
                Model.getInstance().getDiaryTableDao().deleteDiary(title);
                refresh();
                return true;
            }
            return super.onContextItemSelected(item);
        }

        private void initView() {
            iv_main_write = findViewById(R.id.iv_main_write);
            lv_main_diary = findViewById(R.id.lv_main_diary);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            unregisterReceiver(DiaryReceiver);
        }
    };



