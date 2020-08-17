package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.tacoma.uw.csquizzer.authentication.AuthenticationActivity;

/**
 * The MainActivity contains HomeFragment, ShowQuestionFragment, QuizFragment,
 * RepositoryFragment, AboutUsFragment.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class MainActivity extends AppCompatActivity {
    // shared preferences member variable
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    /**
     * Render components to GUI
     *
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mSharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new QuizFragment()).commit();
        }
    }

    /**
     * Add bottom navigation menu to main activity.
     *
     * @param menu BottomNavigation menu.
     * @return true
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Add bottom toolbar to authentication activity*/
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    /**
     * Listen menu item actions.
     *
     * @param item Menu item.
     * @return boolean
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // When user clicks on Sign out button, redirect to Authentication Activity
        if (item.getItemId() == R.id.action_logout) {
            mSharedPreferences.edit().clear().commit();
            Intent i = new Intent(this, AuthenticationActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /* Listen actions for bottom toolbar */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment;
                    switch (item.getItemId()) {
                        case R.id.mnRepo:
                            // System menu is for admins only
                            if (mSharedPreferences.getBoolean(getString(R.string.ADMIN), false)) {
                                selectedFragment = new RepositoryFragment();
                            } else {
                                selectedFragment = new UserFragment(mContext);
                            }
                            break;
                        case R.id.mnInfo:
                            selectedFragment = new AbousUsFragment();
                            break;
                        default:
                            selectedFragment = new QuizFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}