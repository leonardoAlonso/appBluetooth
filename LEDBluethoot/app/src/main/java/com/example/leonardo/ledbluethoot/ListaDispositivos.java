package com.example.leonardo.ledbluethoot;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Leonardo on 14/05/2017.
 */

public class ListaDispositivos extends ListActivity {

    private BluetoothAdapter adapter_dos = null;
    static String dir_mac = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> arrayBluethoot = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        adapter_dos = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosPareados = adapter_dos.getBondedDevices();

        if(dispositivosPareados.size() > 0){
            for (BluetoothDevice dispositivos : dispositivosPareados){
                String nomBt = dispositivos.getName();
                String macDir = dispositivos.getAddress();
                arrayBluethoot.add(nomBt + "\n" + macDir);
            }
        }
        setListAdapter(arrayBluethoot);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacion = ((TextView) v).getText().toString();

        //Toast.makeText(getApplicationContext(), "Informacion: " + informacion, Toast.LENGTH_LONG).show();

        String macDir = informacion.substring(informacion.length() - 17);
        //Toast.makeText(getApplicationContext(), "MAC: " + macDir, Toast.LENGTH_LONG).show();

        Intent retornaMac = new Intent();
        retornaMac.putExtra(dir_mac, macDir);
        setResult(RESULT_OK,retornaMac);
        finish();

    }
}
