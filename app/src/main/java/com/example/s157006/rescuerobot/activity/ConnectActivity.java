package com.example.s157006.rescuerobot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.TcpClient;
import com.example.s157006.rescuerobot.MainActivity;
import com.example.s157006.rescuerobot.R;
import com.example.s157006.rescuerobot.Util;

import java.util.Arrays;

/**
 * Created by joaquin on 8-10-2017.
 *
 */

public class ConnectActivity extends Activity{

    public final static String KEY_IP   = "ip";
    public final static String KEY_PORT = "port";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        final AutoCompleteTextView ip_txt   = (AutoCompleteTextView) findViewById(R.id.ip_input);
        final EditText port_txt = (EditText) findViewById(R.id.port_input);
        Button connect_btn = (Button) findViewById(R.id.connect_btn);
        Button continue_btn = (Button) findViewById(R.id.continue_btn);

        //We add the static ip's of our Arduino for convenience
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                Arrays.asList(new String[]{
                        TcpClient.LAN_IP,
                        TcpClient.WIFI_IP
                }));
        ip_txt.setAdapter(adapter);

        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ip = ip_txt.getText().toString();
                final String port  = port_txt.getText().toString();

                //validate the ip and port
                if (!Util.validateIP(ip)){
                    alert("The IP you entered is invalid");
                } else {
                    try{
                        final int iport = Integer.valueOf(port);
                        alert("Do you want to connect to \"" + ip + ":" + port + "\"", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                                intent.putExtra(KEY_IP,   ip);
                                intent.putExtra(KEY_PORT, iport);
                                startActivity(intent);
                            }
                        }, null);
                    } catch (NumberFormatException ex){
                        alert("The port you entered should be a number");
                    }
                }
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void alert(String msg){
        alert(msg, null, null);
    }
    private void alert(String msg,
                       DialogInterface.OnClickListener positive,
                       DialogInterface.OnClickListener negative){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setPositiveButton(R.string.ok, positive);

        // if neither of the options lead to an action we might just as well only use 1 button to close the dialog
        if (positive == null && negative == null)
            builder.setNegativeButton(R.string.cancel, negative);

        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
