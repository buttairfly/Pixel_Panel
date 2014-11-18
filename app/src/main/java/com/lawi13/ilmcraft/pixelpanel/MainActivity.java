package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.content.pm.ActivityInfo;
import android.widget.Toast;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class MainActivity extends Activity
        implements  NavigationDrawerFragment.NavigationDrawerCallbacks,
                    Settings.OnFragmentInteractionListener,
                    FillColor.OnFragmentInteractionListener{

    public static final String KEY_PREF_IP_ADDRESS = "IP_Address";
    public static final String MyPREFERENCES = "MyPrefs" ;

    private int pRed = 255;
    private int pGreen = 0;
    private int pBlue = 0;
    private int pMBrightness = 255;
    private int pFPS = 60;
    private String ipAddress = "";//"192.168.1.185";



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
        if (sharedpreferences.contains(KEY_PREF_IP_ADDRESS))
        {
            ipAddress = sharedpreferences.getString(KEY_PREF_IP_ADDRESS, "");
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        onSectionAttached(position + 1);
        Log.v(TAG,"change to Position "+ position + ": "+ mTitle);
        switch(position){
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,
                                FillColor.newInstance(pRed, pGreen, pBlue))
                        .commit();
                break;
            case 8:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,
                                Settings.newInstance(pMBrightness, pFPS, ipAddress))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section11);
                break;
            case 3:
                mTitle = getString(R.string.title_section2);
                break;
            case 4:
                mTitle = getString(R.string.title_section21);
                break;
            case 5:
                mTitle = getString(R.string.title_section22);
                break;
            case 6:
                mTitle = getString(R.string.title_section23);
                break;
            case 7:
                mTitle = getString(R.string.title_section24);
                break;
            case 8:
                mTitle = getString(R.string.title_section3);
                break;
            case 9:
                mTitle = getString(R.string.title_section4);
                break;
            default:
                mTitle = getString(R.string.title_section5);
                break;
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();

		if (!Utils.isConnected(this)) {
			Log.v(TAG, "no wifi");
            Toast toast = Toast.makeText(this, "Please activate yout Wi-Fi!", Toast.LENGTH_LONG);
            toast.show();
		}
        else {
            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (!Utils.isValidIp(ipAddress)) {
                Log.v(TAG, "no valid ip address");

                Toast toast = Toast.makeText(this, "no valid ip address found", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                Log.v(TAG, "start Thread");
                udp = (UDP_Client) new UDP_Client(this);
                udp.execute(ipAddress);
            }
		}
    }

    protected void onPause() {
        super.onPause();
        if(udp != null)
            udp.terminate();
    }

    private void showColor() {

        byte ctl = (byte)0x80;
        byte spCmd;
        byte spSubCmd;
        /*if(pulseCheckBox.isChecked()){
            //animation
            byte[] message = {(byte)0xff,0x00,pRed,pGreen,pBlue};
            spCmd 		= (byte)0x03;//SPCMD_ANI_SET
            spSubCmd 	= (byte)0x04;//aniScreenPulse
            if(udp != null){
                udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
            }
        }
        else{*/
            //draw Color
            byte[] message = {(byte)pRed,(byte)pGreen,(byte)pBlue};
            spCmd 		= (byte)0x01;//SPCMD_DRAW
            spSubCmd 	= (byte)0x01;//SPCMD__COLOR
            if(udp != null){
                udp.sendSpecialCmd(ctl, spCmd, spSubCmd, message);
            }
       // }
    }


    /*
     * Settings Listener
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

    @Override
    public void onIpAddressChange(String ip) {
        ipAddress = ip;
        //terminate udp
        if(udp != null){
            udp.terminate();
        }
        //check connectivity
        if (!Utils.isConnected(this)) {
            Log.v(TAG, "no wifi");
            Toast toast = Toast.makeText(this, "Please activate your Wi-Fi!", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            if (!Utils.isValidIp(ipAddress)) {
                Log.v(TAG, "no valid ip address");
                ipAddress = "";
                Toast toast = Toast.makeText(this, "no valid ip address found", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Log.v(TAG, "start Thread");
                udp = (UDP_Client) new UDP_Client(this);
                udp.execute(ipAddress);
            }
        }
        //update ipAddress
        Editor editor = sharedpreferences.edit();
        editor.putString(KEY_PREF_IP_ADDRESS, ipAddress);
        editor.commit();
    }


    /*
     * FillColor Listener
     *
     */
    @Override
    public void onRedChange(int red) {
        pRed = red;
        showColor();
    }

    @Override
    public void onGreenChange(int green) {
        pGreen = green;
        showColor();
    }

    @Override
    public void onBlueChange(int blue) {
        pBlue = blue;
        showColor();
    }
}
