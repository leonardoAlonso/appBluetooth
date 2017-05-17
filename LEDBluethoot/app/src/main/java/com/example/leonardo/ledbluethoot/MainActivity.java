package com.example.leonardo.ledbluethoot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnConect, btnLed1, btnLed2;
    SeekBar slider;
    TextView textoSlider;
    private static final int SOLICITA_ACTIVACION = 1;
    private static final int SOLICITA_CONEXION = 2;

    ConnectedThread conecterThread;

    boolean conexion = false;

    private static String MAC = null;

    BluetoothAdapter adapter = null;
    BluetoothDevice device = null;
    BluetoothSocket socket = null;

    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConect = (Button)findViewById(R.id.Conect);
        btnLed1 = (Button)findViewById(R.id.btnLed1);
        slider = (SeekBar)findViewById(R.id.intencidad1);
        textoSlider = (TextView)findViewById(R.id.textView);

        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            Toast.makeText(getApplicationContext(), "Su dispositivo no tiene conexion", Toast.LENGTH_LONG).show();
        }else if(!adapter.isEnabled()){
            Intent activaBluethoot = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activaBluethoot, SOLICITA_ACTIVACION);
        }

        btnConect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conexion){
                    //desconectar
                    try{
                        socket.close();
                        conexion = false;
                        btnConect.setText("Conectar");
                        Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();
                    }catch(IOException error){
                        Toast.makeText(getApplicationContext(), "Ocurrio un error: " + error, Toast.LENGTH_LONG).show();
                    }
                }else{
                    //conectar
                    Intent abreLista = new Intent(MainActivity.this, ListaDispositivos.class);
                    startActivityForResult(abreLista,SOLICITA_CONEXION);
                }
            }
        });

        btnLed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conexion){
                    conecterThread.enviar("led1");
                    conecterThread.enviar("led2");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth no conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
        ///////////////////////////////////////////////////////////
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(conexion){
                    textoSlider.setText("Intencidad: " + progress);
                    conecterThread.enviar(String.valueOf(progress));
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth no conectado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SOLICITA_ACTIVACION:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "El Bluethoot se ha activado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "El Bluethoot no se ha activado, la aplicacion se cerrara", Toast.LENGTH_LONG).show();
                    finish();
                }
            break;
            case SOLICITA_CONEXION:
                if(resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(ListaDispositivos.dir_mac);
                    //Toast.makeText(getApplicationContext(), "MAC: " + MAC, Toast.LENGTH_LONG).show();
                    device = adapter.getRemoteDevice(MAC);

                    try{
                        socket = device.createRfcommSocketToServiceRecord(uuid);
                        socket.connect();

                        conexion = true;

                        conecterThread = new ConnectedThread(socket);
                        conecterThread.start();

                        Toast.makeText(getApplicationContext(), "Estas conectado con " + MAC, Toast.LENGTH_LONG).show();
                        btnConect.setText("Desconectar");

                    }catch(IOException error){
                        conexion = false;
                        Toast.makeText(getApplicationContext(), "Ocurrio un error: " + error, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Falla al obtener la direccion mac", Toast.LENGTH_LONG).show();
                }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            /*
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activit
                } catch (IOException e) {
                    break;
                }
            }
            */
        }

        /* Call this from the main activity to send data to the remote device */
        public void enviar(String datos) {
            byte[] buffer = datos.getBytes();
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) { }
        }
    }
}
