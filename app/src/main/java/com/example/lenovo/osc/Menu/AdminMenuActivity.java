package com.example.lenovo.osc.Menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.osc.StaffFragment.NewStockFragment;
import com.example.lenovo.osc.StaffFragment.OrdersListFragment;
import com.example.lenovo.osc.StaffFragment.RegisterUserFragment;
import com.example.lenovo.osc.StaffFragment.StocksListFragment;
import com.example.lenovo.osc.StaffFragment.UserListFragment;
import com.example.lenovo.osc.Main.LoginActivity;
import com.example.lenovo.osc.R;
import com.example.lenovo.osc.StockistOrderListFragment;


public class AdminMenuActivity extends ActionBarActivity
        implements AdminNavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private AdminNavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        mNavigationDrawerFragment = (AdminNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new RegisterUserFragment();
                break;
            case 1:
                fragment = new UserListFragment();
                break;
            case 2:
                fragment = new NewStockFragment();
                break;
            case 3:
                fragment = new StocksListFragment();
                break;
            case 4:
                fragment = new OrdersListFragment();
                break;
            case 5:
                fragment = new StockistOrderListFragment();
                break;
        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.newUser);
                break;
            case 2:
                mTitle = getString(R.string.updateUser);
                break;
            case 3:
                mTitle = getString(R.string.newStock);
                break;
            case 4:
                mTitle = getString(R.string.updateStock);
                break;
            case 5:
                mTitle = getString(R.string.supplierOrder);
                break;
            case 6:
                mTitle = getString(R.string.stockistOrder);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.admin_menu, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString("userId", "").commit();
            prefs.edit().putString("loginState", "false").commit();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_admin_menu, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AdminMenuActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onBackPressed() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("userId", LoginActivity.currentUser.getUserID()).commit();
        prefs.edit().putString("loginState", "true").commit();
        moveTaskToBack(true);
    }
}
