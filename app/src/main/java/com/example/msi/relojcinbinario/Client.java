package com.example.msi.relojcinbinario;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends AsyncTask<String, Void, String>
{

    private String serverIp; //your computer IP address
    private int serverPort;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public Client(OnMessageReceived listener, String serverIp, int serverPort)
    {
        mMessageListener = listener;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    private String run(String message)
    {
        try
        {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(serverIp);

            Log.d("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddr, serverPort), 4000); //Timeout de conexión de 4 segundos
            socket.setSoTimeout(2000);

            Log.d("TCP Client", "C: Connected");

            try
            {
                char[] buffer = new char[2048];
                int charsRead = 0;

                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if (mBufferOut != null && !mBufferOut.checkError())
                {
                    mBufferOut.print(message);
                    mBufferOut.flush();
                    Log.d("TCP Client", "C: Message Sent");
                }

                if ((charsRead = mBufferIn.read(buffer)) != -1 && mMessageListener != null)
                {
                    mServerMessage = new String(buffer).substring(0, charsRead);
                    Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");
                }
            }
            catch (Exception e)
            {
                mServerMessage = "Error de comunicación";
            }
            finally
            {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                //Indicar al main que se desconectó el socket
                mBufferOut.flush();
                mBufferOut.close();
                mBufferIn.close();
                socket.close();
            }
        }
        catch (Exception e)
        {
            mServerMessage = "Error de conexión";
        }

        return mServerMessage;
    }

    @Override
    protected String doInBackground(String... messageToSend)
    {
        return run(messageToSend[0]);
    }


    @Override
    protected void onPostExecute(String serverMessage)
    {
        if (serverMessage.contains("Error"))
            mMessageListener.errorMessage(serverMessage);
        else
            mMessageListener.messageReceived(serverMessage);
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived
    {
        void messageReceived(String message);

        void errorMessage(String errorMessage);
    }
}
