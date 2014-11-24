package com.lawi13.ilmcraft.pixelpanel;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        Connectivity.OnFragmentInteractionListener,
        Display.OnFragmentInteractionListener,
        OneColorFragment.OnFragmentInteractionListener {

    public static String PACKAGE_NAME;

    public static final String KEY_PREF_IP_ADDRESS = "IP_Address";
    public static final String KEY_PREF_pColor1 = "pColor1";

    public static final String MyPREFERENCES = "MyPrefs";

    private int actGroup = 0;
    private int actChild = 0;

    private int pColor1 = 0;
    private int pMBrightness = 255;
    private int pFPS = 60;
    private String ipAddress = "";
    private UDP_Client udp;
    final static String TAG = "MainActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    SharedPreferences sharedpreferences;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //shared prefs
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(KEY_PREF_IP_ADDRESS)) {
            ipAddress = sharedpreferences.getString(KEY_PREF_IP_ADDRESS, "");
        }
        if (sharedpreferences.contains(KEY_PREF_pColor1)) {
            pColor1 = sharedpreferences.getInt(KEY_PREF_pColor1, 0);
        }
        onNavigationDrawerItemSelected(0, 0);
    }

    @Override
    public void onNavigationDrawerItemSelected(int group, int child) {
        actGroup = group;
        actChild = child;
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        onSectionAttached(group, child);
        Log.v(TAG, "change to Group: " + group + ", Child: " + child + ", Title: " + mTitle);
        switch (group) {
            case 0:
                switch (child) {
                    case 0:
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        break;
                    default:
                        loadDefaultFragment("Dummy: " + mTitle);
                        break;
                }
                break;
            case 1:
                switch (child) {
                    case 0: {
                        /* Pulse Color
                         */
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        break;
                    }
                    case 1: {
                        /* Fade Dir
                         * paramSize: 5
                         * [0]: diff
                         * [1]: direction //TODO: implement
                         * [2]: red
                         * [3]: green
                         * [4]: blue
                         */
                        byte diff = 4;
                        byte dir = 0;
                        byte[] message = {diff, dir, (byte) 0x00, (byte) 0xff, 0x00};
                        byte ctl = (byte) 0x80;
                        byte spCmd = (byte) 0x03;
                        byte spSubCmd = (byte) 0x02;
                        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                        loadDefaultFragment(mTitle + " - Left - Started");
                        break;
                    }
                    case 4: {
                        /* Rotor
                         * paramSize: 7
                         * [0]: p_x1
                         * [1]: p_y1
                         * [2]: p_x2
                         * [3]:	p_y2
                         * [4]: red
                         * [5]:	green
                         * [6]:	blue
                         */
                        byte[] message = {(byte) 0x00, 0x00, 0x00, 0x00,
                                (byte) Utils.ARGBtoR(pColor1),
                                (byte) Utils.ARGBtoG(pColor1),
                                (byte) Utils.ARGBtoB(pColor1)};
                        byte ctl = (byte) 0x80;
                        byte spCmd = (byte) 0x03;
                        byte spSubCmd = (byte) 0x05;
                        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                        loadDefaultFragment(mTitle + " - Started");
                        break;
                    }
                    case 5: {
                        /* Drops
                         * paramSize: 6
                         * [0]: x
                         * [1]: y
                         * [2]: radius
                         * [3]: red
                         * [4]: green
                         * [5]: blue
                         */
                        byte[] message = {(byte) 0x04, 0x02, 0x00,
                                (byte) Utils.ARGBtoR(pColor1),
                                (byte) Utils.ARGBtoG(pColor1),
                                (byte) Utils.ARGBtoB(pColor1)};
                        byte ctl = (byte) 0x80;
                        byte spCmd = (byte) 0x03;
                        byte spSubCmd = (byte) 0x06;
                        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                        loadDefaultFragment(mTitle + " - Started");
                        break;
                    }
                    default:
                        loadDefaultFragment("Dummy: " + mTitle);
                        break;
                }
                break;
            case 2:
                switch (child) {
                    default:
                        loadDefaultFragment("Dummy: " + mTitle);
                        break;
                }
                break;
            case 3:
                switch (child) {
                    case 0:
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        Connectivity.newInstance(ipAddress))
                                .commit();
                        break;
                    case 1:
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        Display.newInstance(pMBrightness, pFPS))
                                .commit();
                        break;
                    default:
                        loadDefaultFragment("Dummy: " + mTitle);
                        break;
                }
                break;
            default:
                loadDefaultFragment("Dummy: " + mTitle);
                break;
        }
    }

    private void loadDefaultFragment(CharSequence s) {
        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                DefaultFragment.newInstance(s))
                .commit();
    }

    public void onSectionAttached(int group, int child) {
        try {
            String[] groupArray = getResources().getStringArray(R.array.groups);
            int[] ids = {R.array.Draw, R.array.Animation, R.array.Games, R.array.Settings};
            String[] childArray = getResources().getStringArray(ids[group]);
            mTitle = groupArray[group] + " - " + childArray[child];
        } catch (Exception e) {
            throw new RuntimeException("onSectionAttached error with group: " + group +
                    ", child: " + child, e);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            throw new AssertionError();
        }
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
            getMenuInflater().inflate(R.menu.main, menu);
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();

        if (!Utils.isConnected(this)) {
            Log.v(TAG, "no wifi");
            Toast toast = Toast.makeText(this, "Please activate yout Wi-Fi!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            if (!Utils.isValidIp(ipAddress)) {
                Log.v(TAG, "no valid ip address");

                Toast toast = Toast.makeText(this, "no valid ip address found", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Log.v(TAG, "start Thread");
                udp = new UDP_Client(this);
                udp.execute(ipAddress);
            }
        }
    }

    protected void onPause() {
        super.onPause();
        if (udp != null)
            udp.terminate();
    }

    private void showColor() {
        byte ctl = (byte) 0x80;
        byte spCmd;
        byte spSubCmd;
        switch (actGroup) {
            case 0: {
                //draw Color
                byte[] message = {
                        (byte) Utils.ARGBtoR(pColor1),
                        (byte) Utils.ARGBtoG(pColor1),
                        (byte) Utils.ARGBtoB(pColor1)};
                spCmd = (byte) 0x01;//SPCMD_DRAW
                spSubCmd = (byte) 0x01;//SPCMD__COLOR
                if (udp != null) {
                    udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                }
                break;
            }
            case 1: {
                //animation
                byte[] message = {(byte) 0xff, 0x00,
                        (byte) Utils.ARGBtoR(pColor1),
                        (byte) Utils.ARGBtoG(pColor1),
                        (byte) Utils.ARGBtoB(pColor1)};
                spCmd = (byte) 0x03;//SPCMD_ANI_SET
                spSubCmd = (byte) 0x04;//aniScreenPulse
                if (udp != null) {
                    udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                }
                break;
            }
        }
    }


    /*
     * Display Listener
     *
     */
    @Override
    public void onMasterBrightnessChange(int brightness) {
        pMBrightness = brightness;
    }

    @Override
    public void onFpsChange(int fps) {
        pFPS = fps;
    }

    /*
     * Connectivity Listener
     *
     */
    @Override
    public void onIpAddressChange(String ip) {
        ipAddress = ip;
        //terminate udp
        if (udp != null) {
            udp.terminate();
        }
        //check connectivity
        if (!Utils.isConnected(this)) {
            Log.v(TAG, "no wifi");
            Toast toast = Toast.makeText(this, "Please activate your Wi-Fi!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            if (!Utils.isValidIp(ipAddress)) {
                Log.v(TAG, "no valid ip address");
                ipAddress = "";
                Toast toast = Toast.makeText(this, "no valid ip address found", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Log.v(TAG, "start Thread");
                udp = new UDP_Client(this);
                udp.execute(ipAddress);
            }
        }
        //update ipAddress
        Editor editor = sharedpreferences.edit();
        editor.putString(KEY_PREF_IP_ADDRESS, ipAddress);
        editor.apply();
    }


    /*
     * OneColorFragment Listener
     *
     */
    @Override
    public void onColor1Change(int color1) {
        pColor1 = color1;
        showColor();
        //update color1
        Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_PREF_pColor1, pColor1);
        editor.apply();
    }
}
