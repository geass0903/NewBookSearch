package jp.gr.java_conf.nuranimation.new_book_search;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean D = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) Log.e(TAG, "+++ ON CREATE +++");
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.new_books, R.id.keywords, R.id.settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (D) Log.e(TAG, "onDestinationChanged : " + destination);
            }
        });

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
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE - ");
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
    }

}
