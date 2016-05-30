package com.example.Calculation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * Description
 *
 * @author Catalin Prata
 *         Date: 2/12/13
 */
public class TCPClient {

    public static final String SERVER_IP = "192.168.1.1"; //your computer IP address
    public static final int SERVER_PORT = 5001;
    // message to send to the server
    private String mServerMessage;
    public String clientmessage;
    // sends message received notifications
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    private Socket socket;

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */


    public void sendMessage(String message) {

        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    public boolean isConnected() {return mRun;}


    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        // send mesage that we are closing the connection
        //sendMessage(Constants.CLOSED_CONNECTION + "Kazy");

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVER_PORT);
            try {
                mRun = true;
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (mRun) {
                    mServerMessage = mBufferIn.readLine();
                    if (mServerMessage!=null) {
                        System.out.println(mServerMessage);
                    }
                }
                // send login name
            } catch (Exception e) {
                System.out.println("second try:\n" + e);
                mRun = false;
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            System.out.println("first try:\n" + e);
            mRun = false;
        }

    }

}

