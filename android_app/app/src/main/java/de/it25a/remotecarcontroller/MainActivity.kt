package de.it25a.remotecarcontroller


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.InputStream
import java.io.OutputStream
import java.util.LinkedList


class MainActivity : AppCompatActivity() {

    // App-Element Deklaration
    private lateinit var consoleTextView: TextView;
    private lateinit var infoTextView: TextView;

    private lateinit var connectionInfoView: TextView;
    private lateinit var reconnectButton: Button;

    private lateinit var upButton: Button;
    private lateinit var downButton: Button;
    private lateinit var leftButton: Button;
    private lateinit var rightButton: Button;
    private lateinit var consoleData: LinkedList<String>;

    private lateinit var outputStream: OutputStream;
    private lateinit var inStream: InputStream;

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sicherstellen, dass das Gerät über Bluetooth verfügt
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(
                getApplicationContext(),
                "Device does not support Bluetooth therefore this application cannot run.",
                Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // App-Elementobjekte auslesen und in den zuvor erstellten Variablen speichern, für spätere verwendung
        consoleTextView = findViewById(R.id.consoleTextView);
        consoleData = LinkedList()

        infoTextView = findViewById(R.id.infoTextView);
        connectionInfoView = findViewById(R.id.connectionInfoView);
        reconnectButton = findViewById(R.id.reconnectButton);

        upButton = findViewById(R.id.upButton);
        downButton = findViewById(R.id.downButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);

        // Erstellung des Bluetooth-Adapter Objektes
        val blueAdapter = BluetoothAdapter.getDefaultAdapter();

        // Sicherstellen, das der Adapter eingeschaltet ist
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled) {
                createConnection(blueAdapter);
            } else {
                Log.e("error", "Bluetooth is disabled.")
            }
        }

        // erstellung des OnTouchListener: Führt Code aus, wenn die Buttons des App-UIs gedrückt werden
        upButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Up Button press")
                    doButtonPress("W")
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Up Button release")
                    doButtonPress("w")
                }
            }
            true
        }
        downButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Down Button press")
                    doButtonPress("S")
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Down Button release")
                    doButtonPress("s")
                }
            }
            true
        }
        leftButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Left Button press")
                    doButtonPress("A")
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Left Button release")
                    doButtonPress("a")
                }
            }
            true
        }
        rightButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Right Button press")
                    doButtonPress("D")
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Right Button release")
                    doButtonPress("d")
                }
            }
            true
        }
        reconnectButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    createConnection(blueAdapter)
                    reconnectButton.isEnabled = false
                }
            }
            true
        }
    }

    fun createConnection(blueAdapter: BluetoothAdapter): Boolean {
        var result = false
        // Sicherstellen, das wir die Berechtigung dazu haben, Bluetooth zu verwenden
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED) {
            return result
        }
        // Auslesen aller Gekoppelten Geräte
        val bondedDevices = blueAdapter.bondedDevices
        if (bondedDevices.size > 0) {
            val devices = bondedDevices.toTypedArray() as Array<BluetoothDevice>
            var device: BluetoothDevice
            // in den gekoppelten Geräten nach dem Auto über die Mac-Adresse suchen. Wegen des Prototypen noch Statisch
            for(BD: BluetoothDevice in devices) {
                if(BD.address == "00:21:06:BE:5D:C5")
                {
                    device = BD;
                    val uuids = device.uuids
                    val socket = device.createRfcommSocketToServiceRecord(uuids[0].uuid)

                    // Versuchen sich zu verbinden...
                    try {
                        socket.connect()
                        result = true;
                    } catch (exception: Throwable) {
                        Log.e("error", "Connection Timeout?",exception)
                        // Verbindung ist fehlgeschlagen, Exception für Debuginfos anzeigen und die Steuerungsbuttons ausschalten.
                        result = false;
                        connectionInfoView.setText("Connection Timeout");
                        upButton.isEnabled = false;
                        downButton.isEnabled = false;
                        leftButton.isEnabled = false;
                        rightButton.isEnabled = false;
                    }
                    outputStream = socket.outputStream
                    inStream = socket.inputStream
                }
            }
        }
        if(result) { // wenn wir eine Verbindung haben, zeigen wir das so an und die App funktioniert
            reconnectButton.isEnabled = false;
            connectionInfoView.setText("Connected!");
            upButton.isEnabled = true;
            downButton.isEnabled = true;
            leftButton.isEnabled = true;
            rightButton.isEnabled = true;
        }
        return result
    }
    // Debugprint auf einem Element in der App
    // Wenn Fertig, bitte entfernen
    private fun customPrint(value: Any?, color: Int?) {
        consoleData.add(value.toString())
        if (consoleData.size > 10) {
            consoleData.removeFirst()
        }

        var consoleString = "";
        for (s in consoleData) {
            consoleString += s + "\n";
        }
        if(color != null) {
            consoleTextView.setTextColor(color);
        } else {
            consoleTextView.setTextColor(Color.WHITE);
        }
        consoleTextView.text = consoleString;
    }
    private fun customPrint(value: Any?) {
        customPrint(value, null);
    }

    // Verarbeite das drücken der Buttons, versende befehle über den outputstream des Bluetooth-Adapters
    fun doButtonPress(cmd: String) {
        outputStream.write( (cmd).toByteArray() );
    }
}