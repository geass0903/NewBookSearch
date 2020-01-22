package jp.gr.java_conf.nuranimation.new_book_search;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import jp.gr.java_conf.nuranimation.new_book_search.service.NewBookService;
import jp.gr.java_conf.nuranimation.new_book_search.ui.base.BaseFragment;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean D = true;

    private NewBookService mService;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            if (D) Log.d(TAG, "onServiceConnected");
            mService = ((NewBookService.MBinder)binder).getService();
            mService.cancelForeground();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (D) Log.e(TAG, "onServiceDisconnected");
        }
    };

    public NewBookService getService(){
        return mService;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) Log.e(TAG, "+++ ON CREATE +++");
        if (D) Log.d(TAG, "savedInstanceState = " + savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_new_books, R.id.navigation_keywords, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");
    }

    @Override
    public synchronized void onResumeFragments() {
        super.onResumeFragments();
        if (D) Log.e(TAG, "+ ON RESUME FRAGMENTS +");
        if(mService == null){
            Intent intent = new Intent(this, NewBookService.class);
            bindService(intent,connection, Service.BIND_AUTO_CREATE);
        }else{
            mService.cancelForeground();
            mService.checkServiceState();
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE - ");
        if (mService != null && mService.getServiceState() != NewBookService.STATE_NONE) {
            Intent intent = new Intent(this, NewBookService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 //               startForegroundService(intent);
            } else {
//                startService(intent);
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
        if(mService != null) {
            unbindService(connection);
            mService = null;
        }
    }


    @Override
    public void onFragmentEvent(FragmentEvent event) {
        event.apply(this);
    }

}
