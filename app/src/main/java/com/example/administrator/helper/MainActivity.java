package com.example.administrator.helper;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.administrator.helper.Fragment.HomePageFragment;
import com.example.administrator.helper.Fragment.RecelivePageFragment;
import com.example.administrator.helper.Fragment.SendPageFragment;
import com.example.administrator.helper.Fragment.SharePageFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        List<Fragment> fragmentList=new ArrayList<Fragment>();
        List<Button> buttonList=new ArrayList<Button>();
        int preIndex;
        int currentIndex;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            SendPageFragment sendPageFragment=new SendPageFragment();
            RecelivePageFragment recelivePageFragment=new RecelivePageFragment();
            SharePageFragment sharePageFragment=new SharePageFragment();
            HomePageFragment homePageFragment=new HomePageFragment();
            Button btnSend= (Button) findViewById(R.id.btn_send);
            Button btnRecelive= (Button) findViewById(R.id.btn_recelive);
            Button btnShare= (Button) findViewById(R.id.btn_share);
            Button btnHome= (Button) findViewById(R.id.btn_home);
            buttonList.add(btnSend);
            buttonList.add(btnRecelive);
            buttonList.add(btnShare);
            buttonList.add(btnHome);
            btnSend.setSelected(true);
            fragmentList.add(sendPageFragment);
            fragmentList.add(recelivePageFragment);
            fragmentList.add(sharePageFragment);
            fragmentList.add(homePageFragment);
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container,fragmentList.get(0)).commit();
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void onTabClicked(View v){

            switch(v.getId()){
                case R.id.btn_send:
                    currentIndex=0;
                    break;
                case R.id.btn_recelive:
                    currentIndex=1;
                    break;
                case R.id.btn_share:
                    currentIndex=2;
                    break;
                case R.id.btn_home:
                    currentIndex=3;
                    break;
            }
            if(currentIndex!=preIndex) {
                buttonList.get(preIndex).setSelected(false);
                buttonList.get(currentIndex).setSelected(true);
                toggleFragment(fragmentList.get(preIndex),fragmentList.get(currentIndex));
            }
            preIndex=currentIndex;
        }
        public void toggleFragment(Fragment hideFragment,Fragment showFragment){
            if(hideFragment!=showFragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(hideFragment);
                if (!showFragment.isAdded()) {
                    fragmentTransaction.add(R.id.fragment_container, showFragment);
                }
                fragmentTransaction.show(showFragment);
                fragmentTransaction.commit();
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==SendPageFragment.REQUECT&&resultCode==RESULT_OK){
            currentIndex=0;
            buttonList.get(preIndex).setSelected(false);
            buttonList.get(currentIndex).setSelected(true);
            toggleFragment(fragmentList.get(preIndex),fragmentList.get(currentIndex));
            preIndex=currentIndex;
        }
    }
}

