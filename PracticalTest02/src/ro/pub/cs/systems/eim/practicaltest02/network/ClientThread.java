package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String message;
    private String informationType;
    private TextView resultTextView;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String message,
            TextView resultTextView) {
        this.address = address;
        this.port = port;
        // TODO ALTE VAR
        this.resultTextView = resultTextView;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
            	// SCRIU PE SOCKET CE VREAU SA TRIMIT
            	String[] params = message.split(",");
            	// nr1
            	printWriter.println(params[0]);
                printWriter.flush();
                // nr2
                printWriter.println(params[1]);
                printWriter.flush();
                
                printWriter.println(params[2]);
                printWriter.flush();
                
                String response;
                // PRIMESC DATE SI LE AFISEZ
                while ((response = bufferedReader.readLine()) != null) {
                    final String finalizedWeatherInformation = response;
                    resultTextView.post(new Runnable() {
                        @Override
                        public void run() {
                        	resultTextView.append(finalizedWeatherInformation + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
