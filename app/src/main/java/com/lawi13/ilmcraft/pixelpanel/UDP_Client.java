package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class UDP_Client extends AsyncTask<String, String, Void> {
    //  private AsyncTask<Void, Void, Void> async_client;
    private BlockingQueue<byte[]> outQueue = new ArrayBlockingQueue<byte[]>(50);
    private static final int UDP_SERVER_PORT = 65506;
    private static final String TAG = "UDP_Client";
    boolean asyncTaskIsRunning = false;
    boolean isConnected = false;
    private int receiveTimeout = 1000;

    private Activity appActivity;

    public UDP_Client(Activity appActivity) {
        this.appActivity = appActivity;
    }

    public void checkServerResponse(int time, String response) {
        Log.v(TAG, "after time : " + time + "ms");
        if (response.contains("FAILED")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.appActivity);
            builder.setMessage(response).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // do some stuff
                }
            });
            builder.show();
        }
    }

    protected void onProgressUpdate(String... obj) {
        String serverResponse = obj[0];
        int time = Integer.parseInt(obj[1]);
        checkServerResponse(time, serverResponse);
    }

    public void sendData(byte packetNum, byte numPacket, byte[] data) {
        byte[] Message;
        int dataLen = data.length;
        Message = new byte[dataLen + 7];
        Message = new byte[data.length + 7];
        int i = 0;
        Message[i++] = (byte) 0x9C;
        Message[i++] = (byte) 0xDA;
        Message[i++] = (byte) ((dataLen >>> 8) & 0xFF);
        Message[i++] = (byte) (dataLen & 0xFF);
        Message[i++] = packetNum;
        Message[i++] = numPacket;
        for (int j = 0; j < data.length; j++) {
            Message[i++] = data[j];
        }
        Message[i++] = (byte) 0x36;
        put(Message);
    }

    public void sendCmd(byte ctl, byte cmd, byte[] param) {
        byte[] Message;
        int dataLen = param.length + 2;
        Message = new byte[dataLen + 7];
        int i = 0;
        Message[i++] = (byte) 0x9C;
        Message[i++] = (byte) 0xC0;
        Message[i++] = (byte) ((dataLen >>> 8) & 0xFF);
        Message[i++] = (byte) (dataLen & 0xFF);
        Message[i++] = (byte) 0x00;
        Message[i++] = (byte) 0x01;
        Message[i++] = ctl;
        Message[i++] = cmd;
        for (int j = 0; j < param.length; j++) {
            Message[i++] = param[j];
        }
        Message[i++] = (byte) 0x36;
        put(Message);
    }

    public void sendSpecialCmd(byte ctl, byte spCmd, byte spSubCmd, byte[] param) {
        byte[] Message;
        int dataLen = param.length + 4;
        Message = new byte[dataLen + 7];
        int i = 0;
        Message[i++] = (byte) 0x9C;
        Message[i++] = (byte) 0xC0;
        Message[i++] = (byte) ((dataLen >>> 8) & 0xFF);
        Message[i++] = (byte) (dataLen & 0xFF);
        Message[i++] = (byte) 0x00;
        Message[i++] = (byte) 0x01;
        Message[i++] = ctl;
        Message[i++] = (byte) 0xff;
        Message[i++] = spCmd;
        Message[i++] = spSubCmd;
        for (int j = 0; j < param.length; j++) {
            Message[i++] = param[j];
        }
        Message[i++] = (byte) 0x36;
        put(Message);
    }

    public void sendAck(byte ackType) {
        byte[] Message;
        Message = new byte[0 + 7 + 1];
        int i = 0;
        Message[i++] = (byte) 0x9C;
        Message[i++] = (byte) 0xAC;
        Message[i++] = (byte) 0x00;
        Message[i++] = (byte) 0x01;
        Message[i++] = (byte) 0x00;
        Message[i++] = (byte) 0x01;
        Message[i++] = ackType;
        Message[i++] = (byte) 0x36;
        put(Message);
    }

    public void sendAckData(byte ackType, byte[] data) {
        byte[] Message;
        int dataLen = data.length + 1;
        Message = new byte[dataLen + 7];
        int i = 0;
        Message[i++] = (byte) 0x9C;
        Message[i++] = (byte) 0xAD;
        Message[i++] = (byte) ((dataLen >>> 8) & 0xFF);
        Message[i++] = (byte) (dataLen & 0xFF);
        Message[i++] = (byte) 0x00;
        Message[i++] = (byte) 0x01;
        Message[i++] = ackType;
        for (int j = 0; j < data.length; j++) {
            Message[i++] = data[j];
        }
        Message[i++] = (byte) 0x36;
        put(Message);
    }

    private void put(byte[] data) {
        if (outQueue.size() < 40) {
            try {
                outQueue.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Log.v(TAG, "queue is already full");
        }
    }

    public boolean isRunning() {
        return asyncTaskIsRunning;
    }

    public void terminate() {
        this.cancel(true);
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    protected Void doInBackground(String... arg0) {
        byte[] clientCommand;
        String serverAddress = arg0[0];
        DatagramSocket ds = null;
        asyncTaskIsRunning = true;
        try {
            ds = new DatagramSocket();
            DatagramPacket dp;
            isConnected = true;

            while (true) {
                clientCommand = (byte[]) outQueue.take();
                Log.v(TAG, "send udp Packet (" + clientCommand.length + "): 0x" + Utils.bytesToHex(clientCommand));
                dp = new DatagramPacket(clientCommand, clientCommand.length, InetAddress.getByName(serverAddress), UDP_SERVER_PORT);
                ds.send(dp);
                //wait for reply if needed
                if ((clientCommand[6] & 0x40) == 0x40) {
                    Log.v(TAG, "receive udp Packet...");
                    // Set a receive timeout
                    ds.setSoTimeout(receiveTimeout);
                    long t = System.nanoTime();
                    ds.receive(dp);
                    t = System.nanoTime() - t;
                    t = TimeUnit.NANOSECONDS.toMillis(t);
                    Integer time = (int) t;
                    Log.v(TAG, "length : " + dp.getLength());
                    if (dp.getLength() > 0) {
                        String s = new String(dp.getData());
                        publishProgress(new String[]{s, time.toString()});
                    } else {
                        Log.v(TAG, "receive timeout");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            asyncTaskIsRunning = false;
            isConnected = false;
            if (ds != null) {
                ds.close();
            }
        }
        return null;
    }

}