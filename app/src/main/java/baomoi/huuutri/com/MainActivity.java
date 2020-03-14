package baomoi.huuutri.com;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import baomoi.huuutri.com.Activity.AboutActivity;
import baomoi.huuutri.com.Activity.DanhDauActivity;
import baomoi.huuutri.com.Adapter.AdapterViewPagerFragment;
import baomoi.huuutri.com.Service.CheckService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int PAGE_NUMBER = 13;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.startusColor));
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        //Navigation
        NavigationView navigationView = findViewById(R.id.nav_view);
               navigationView.setNavigationItemSelectedListener(this);

        //View Pager
        viewPager = findViewById(R.id.viewPager_main);
        AdapterViewPagerFragment adapter = new AdapterViewPagerFragment(getApplicationContext(),getSupportFragmentManager(), PAGE_NUMBER);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(PAGE_NUMBER);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //TAB TITLE FONT
        for (int i = 0; i < PAGE_NUMBER; i++) {
            TextView tabTitle = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_title, null);
            tabTitle.setText(adapter.getPageTitle(i));
            tabTitle.setTextColor(getResources().getColor(R.color.tabTitleColor));
            tabLayout.getTabAt(i).setCustomView(tabTitle);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.action_share:
                Toast.makeText(this, "Vui lòng đợi!", Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Báo mới";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText + "\nhttp://play.google.com/store/apps/details?id=" + this.getPackageName());
                startActivity(Intent.createChooser(sharingIntent, "Chia sẻ ứng dụng:"));
                break;
            /*case R.id.action_darkmode:
                boolean check=!isNightMode;
                SharedPreferences sharedPreferences = getSharedPreferences("night_mode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode", check);
                editor.commit();
                editor.apply();
                recreate();
                break;*/
            case R.id.action_vote:
                Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                }
                break;
            case R.id.action_info:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.action_tin_danh_dau:
                startActivity(new Intent(MainActivity.this, DanhDauActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        TabLayout.Tab tab;
        switch (id) {
            case R.id.nav_tinNoiBat:
                tab = tabLayout.getTabAt(0);
                break;
            case R.id.nav_phapLuat:
                tab = tabLayout.getTabAt(1);
                break;
            case R.id.nav_theGioi:
                tab = tabLayout.getTabAt(2);
                break;
            case R.id.nav_theThao:
                tab = tabLayout.getTabAt(3);
                break;
            case R.id.nav_xaHoi:
                tab = tabLayout.getTabAt(4);
                break;
            case R.id.nav_vanHoa:
                tab = tabLayout.getTabAt(5);
                break;
            case R.id.nav_kinhTe:
                tab = tabLayout.getTabAt(6);
                break;
            case R.id.nav_congNghe:
                tab = tabLayout.getTabAt(7);
                break;
            case R.id.nav_giaiTri:
                tab = tabLayout.getTabAt(8);
                break;
            case R.id.nav_giaoDuc:
                tab = tabLayout.getTabAt(9);
                break;
            case R.id.nav_sucKhoe:
                tab = tabLayout.getTabAt(10);
                break;
            case R.id.nav_nhaDat:
                tab = tabLayout.getTabAt(11);
                break;
            case R.id.nav_xeCo:
                tab = tabLayout.getTabAt(12);
                break;
            default:
                tab = tabLayout.getTabAt(0);
                break;
        }

        tab.select();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, CheckService.class));
        super.onDestroy();
    }
}
