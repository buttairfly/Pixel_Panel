package com.lawi13.ilmcraft.pixelpanel;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
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
    public static int navBarColor = 0xcc000050;

    private int actGroup = 0;
    private int actChild = 0;

    private int pColor1 = 0;
    private int pColor2 = 0;
    private int pMBrightness = 255;
    private int pFPS = 60;
    private String ipAddress = "";


    private UDP_Client udp;

    private FragmentTabHost mTabHost;

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
/*
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(),R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Color1").setIndicator("Color1"),
                OneColorFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Color2").setIndicator("Color2"),
                OneColorFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Menu").setIndicator("Menu"),
                Display.class, null);
*/
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
        navBarColor = pColor1;
        navBarColor = Utils.AToARGB(navBarColor, 0x33);
        onNavigationDrawerItemSelected(0, 0);
        //send frame to panel
        showColor();
    }

    @Override
    public void onNavigationDrawerItemSelected(int group, int child) {
        actGroup = group;
        actChild = child;
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        onSectionAttached(group, child);
        Log.v(TAG, "change to Group: " + actGroup + ", Child: " + actChild + ", Title: " + mTitle);
        switch (actGroup) {
            case 0:
                //Draw
                switch (actChild) {
                    case 0:
                        /* Draw Color
                         */
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        showColor();
                        break;
                    default:
                        loadDefaultFragment("Dummy: " + mTitle);
                        break;
                }
                break;

            case 1:
                //Animation
                switch (child) {
                    case 0: {
                        /* Pulse Color
                         */
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        showColor();
                        break;
                    }
                    case 1: {
                        /* Fade Screen
                         * ani 0x03
                         * paramSize: 4
                         * [0]: skipFrame
                         * [1]: delta
                         * [2]: colorDimmer
                         * [3]: saturation
                         */
                        byte skipFrame = 0;
                        byte delta = 4;
                        byte dimmer = (byte) 0xff;
                        byte saturation = 0x00;
                        byte[] message = {skipFrame, delta, dimmer, saturation};
                        byte ctl = (byte) 0x80;
                        byte spCmd = (byte) 0x03;//set ani
                        byte spSubCmd = (byte) 0x03; //fadeScreen
                        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                        loadDefaultFragment(mTitle + " - Started");
                        break;
                    }
                    case 2: {
                        /* Fade Direction
                         * ani 0x02
                         * paramSize: 5
                         * [0]: skipFrame
                         * [1]: delta
                         * [2]: direction
                         * [3]: colorDimmer
                         * [4]: saturation
                         */
                        byte skipFrame = 0;
                        byte delta = 4;
                        byte dir = 3;
                        byte dimmer = (byte) 0x80;
                        byte saturation = (byte) 0x10;
                        byte[] message = {skipFrame, delta, dir, dimmer, saturation};
                        byte ctl = (byte) 0x80;
                        byte spCmd = (byte) 0x03;//set ani
                        byte spSubCmd = (byte) 0x02;//fade dir
                        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                        loadDefaultFragment(mTitle + " - dirRightBot - Started");
                        break;
                    }
                    case 3: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        rotor();
                        break;
                    }
                    case 4: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        drops();
                        break;
                    }
                    case 5: {
                        /* Invader
                         * ani 0x01
                         * paramSize : 3
                         * [0]: skipFrame
                         * [1]: colorDimmer
                         * [2]: saturation
                         */
                        byte[] message = {(byte) 0x00, (byte) 0x90, (byte) 0x20};
                        byte ctl = (byte) 0x80;
                        byte spCmd = (byte) 0x03;//set ani
                        byte spSubCmd = (byte) 0x01;//invader
                        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                        loadDefaultFragment(mTitle + " - Started");
                        break;
                    }
                    case 6: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        fadingPixel();
                        break;
                    }
                    case 7: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        OneColorFragment.newInstance(pColor1))
                                .commit();
                        dirFallingPixel();
                        break;
                    }
                    default:
                        loadDefaultFragment(mTitle + " - Started");
                        break;
                }
                break;
            //Games
            case 2:
                switch (child) {
                    default:
                        loadDefaultFragment("Dummy: " + mTitle);
                        break;
                }
                break;
            //Settings
            case 3:
                switch (child) {
                    case 0:
                        //connectivity
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame,
                                        Connectivity.newInstance(ipAddress))
                                .commit();
                        break;
                    case 1:
                        //display
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
            //should not happen:
            default:
                loadDefaultFragment("Dummy: " + mTitle);
                break;
        }
    }

    private void loadDefaultFragment(CharSequence s) {
        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                DefaultFragment.newInstance(s, pColor1))
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


    private void rotor() {
    /* Rotor
     * ani 0x05
     * paramSize: 11
     * [ 0]: frameSkip
     * [ 1]: antiAliasing//TODO implement
     * [ 2]: width
     * [ 3]: rotate_x//TODO implement
     * [ 4]: rotate_y//TODO implement
     * [ 5]: rotorRed
     * [ 6]: rotorGreen
     * [ 7]: rotorBlue
     * [ 8]: backgroundRed
     * [ 9]: backgroundGreen
     * [10]: backgroundBlue
     */
        byte[] message = {(byte) 0x03, 0x00, 0x01, 0x00, 0x00,
                (byte) Utils.ARGBtoR(pColor1),
                (byte) Utils.ARGBtoG(pColor1),
                (byte) Utils.ARGBtoB(pColor1),
                (byte) Utils.ARGBtoR(pColor2),
                (byte) Utils.ARGBtoG(pColor2),
                (byte) Utils.ARGBtoB(pColor2)};
        byte ctl = (byte) 0x80;
        byte spCmd = (byte) 0x03;//set ani
        byte spSubCmd = (byte) 0x05;//rotor
        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
    }

    private void drops() {
    /* Drops
     * ani 0x06
     * paramSize: 9
     * [0]: frameSkip
     * [1]: antiAliasing//TODO implement
     * [2]: width
     * [3]: dropRed
     * [4]: dropGreen
     * [5]: dropBlue
     * [6]: backgroundRed
     * [7]: backgroundGreen
     * [8]: backgroundBlue
        */
        byte[] message = {(byte) 0x03, 0x00, 0x01,
                (byte) Utils.ARGBtoR(pColor1),
                (byte) Utils.ARGBtoG(pColor1),
                (byte) Utils.ARGBtoB(pColor1),
                (byte) Utils.ARGBtoR(pColor2),
                (byte) Utils.ARGBtoG(pColor2),
                (byte) Utils.ARGBtoB(pColor2)};
        byte ctl = (byte) 0x80;
        byte spCmd = (byte) 0x03;//set ani
        byte spSubCmd = (byte) 0x06;//waterdrops
        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
    }

    private void fadingPixel() {
    /* Fading Pixels
     * ani 0x07
     * paramSize: 7
     * [0]: frameSkip
     * [1]: targetPixHigh
     * [2]: targetPixLow
     * [3]: fadeSpeed
     * [4]: pixRed
     * [5]: pixGreen
     * [6]: pixBlue
     */
        byte[] message = {(byte) 0x00, 0x00, (byte) 100, 0x02,
                (byte) Utils.ARGBtoR(pColor1),
                (byte) Utils.ARGBtoG(pColor1),
                (byte) Utils.ARGBtoB(pColor1)};
        byte ctl = (byte) 0x80;
        byte spCmd = (byte) 0x03;//set ani
        byte spSubCmd = (byte) 0x07;//fading Pixels
        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
    }

    private void dirFallingPixel() {
    /* dir Falling Pixels
     * ani 0x08
    * paramSize: 8
    * [0]: frameSkip
    * [1]: targetPix
    * [2]: length
    * [3]: direction
    * [4]: dropDiff
    * [5]: pixRed
    * [6]: pixGreen
    * [7]: pixBlue
    */
        byte[] message = {(byte) 0x03, 10, 4, 0x02, 50,
                (byte) Utils.ARGBtoR(pColor1),
                (byte) Utils.ARGBtoG(pColor1),
                (byte) Utils.ARGBtoB(pColor1)};
        byte ctl = (byte) 0x80;
        byte spCmd = (byte) 0x03;//set ani
        byte spSubCmd = (byte) 0x08;//dir Falling Pixels
        udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
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
                /* ani 0x04
                 * paramSize: 5
                 * [0]: skipFrame
                 * [1]: delta//should be 1
                 * [2]: red
                 * [3]: green
                 * [4]: blue
                 */
                byte[] message = {(byte) 0x00, 0x01,
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

    @Override
    public void onSystemCall(int id) {
        byte ctl = (byte) 0x80;
        byte spCmd;
        byte spSubCmd;
        switch (id) {
            case 0x4A17: {
                //halt
                byte[] message = {};
                spCmd = (byte) 0xFE;//SPCMD_SYSTEM_ADMIN
                spSubCmd = (byte) 0x01;//halt
                if (udp != null) {
                    udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                }
            }
            break;
            case 0x012eb007: {
                //reboot
                byte[] message = {};
                spCmd = (byte) 0xFE;//SPCMD_SYSTEM_ADMIN
                spSubCmd = (byte) 0x02;//reboot
                if (udp != null) {
                    udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
                }
                break;
            }
            case 0xB1149: {
                //ping
                byte[] message = {};
                ctl = 0x40;
                byte cmd = (byte) 0x20;
                if (udp != null) {
                    udp.sendCmd(ctl, cmd, message);
                    //  udp.getMessage();
                }

                break;
            }
        }
    }


    /*
     * OneColorFragment Listener
     *
     */
    @Override
    public void onColor1Change(int color1) {
        pColor1 = color1;
        navBarColor = color1;
        navBarColor = Utils.AToARGB(navBarColor, 0x33);
        if (((actGroup == 0) && (actChild == 0)) || ((actGroup == 1) && (actChild == 0))) {
            showColor();
        }
        if ((actGroup == 1) && (actChild == 3)) {
            rotor();
        }
        if ((actGroup == 1) && (actChild == 4)) {
            drops();
        }
        if ((actGroup == 1) && (actChild == 6)) {
            fadingPixel();
        }
        if ((actGroup == 1) && (actChild == 7)) {
            dirFallingPixel();
        }
        //update color1
        Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_PREF_pColor1, pColor1);
        editor.apply();
    }
}
