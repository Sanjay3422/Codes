package tab.swipe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
   
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     *
     */
    private Animation fabopen, fabclose, rot_for, rot_back;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private boolean isfabopen=false;
    private FloatingActionButton fab, fab1, fab2;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
      fab1= (FloatingActionButton) findViewById(R.id.fab1);
      fab2= (FloatingActionButton)findViewById(R.id.fab2);
        fabopen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fabopen);
        fabclose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fabclose);
        rot_for= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotateforward);
        rot_back= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotateback);

      fab.setOnClickListener(this);
      fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
    }

    private void animateFAB() {
        if (isfabopen)
        {
            fab.startAnimation(rot_back);
            fab1.startAnimation(fabclose);
            fab2.startAnimation(fabclose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isfabopen=false;
        }
        else
        {
            fab.startAnimation(rot_for);
            fab1.startAnimation(fabopen);
            fab2.startAnimation(fabopen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isfabopen=true;

        }
    }

    boolean doubleBack = false;

    @Override
    public void onBackPressed() {
        if (isfabopen)
        {
            animateFAB();
        }
        else {
            if (doubleBack) {
                super.onBackPressed();
                return;
            }
            this.doubleBack = true;
            Toast.makeText(MainActivity.this, "Press again to Exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBack = false;
                }
            }, 2000);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu1, menu);
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

    @Override
    public void onClick(View view) {
        int id= view.getId();
        switch (id)
        {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                Intent in= new Intent(MainActivity.this, Main6Activity.class);
                startActivity(in);
                break;
            case R.id.fab2:
                Intent in1= new Intent(MainActivity.this, Draw.class);
                startActivity(in1);
                break;
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    Tab1 tab1= new Tab1();
                    return tab1;
                case 1:
                    Tab2 tab2= new Tab2();
                    return tab2;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DOCUMENTS";
                case 1:
                    return "DRAWINGS";
            }
            return null;
        }
    }


}

