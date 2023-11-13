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

    enum class Direction {
        Up,
        Down,
        Left,
        Right
    }

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

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(
                getApplicationContext(),
                "Device does not support Bluetooth therefore this application cannot run.",
                Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Find the Elements, store them in the class-Variabeles
        consoleTextView = findViewById(R.id.consoleTextView);
        consoleData = LinkedList()

        infoTextView = findViewById(R.id.infoTextView);
        connectionInfoView = findViewById(R.id.connectionInfoView);
        reconnectButton = findViewById(R.id.reconnectButton);

        upButton = findViewById(R.id.upButton);
        downButton = findViewById(R.id.downButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);

        val blueAdapter = BluetoothAdapter.getDefaultAdapter();

        if (blueAdapter != null) {
            if (blueAdapter.isEnabled) {
                createConnection(blueAdapter);
            } else {
                Log.e("error", "Bluetooth is disabled.")
            }
        }

        // Create the OnTouchListener: Getting triggered, when user touches the Button
        upButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Up Button press")
                    doButtonPress(Direction.Up, motionEvent)
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Up Button release")
                    doButtonPress(Direction.Up, motionEvent)
                }
            }
            true
        }
        downButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Down Button press")
                    doButtonPress(Direction.Down, motionEvent)
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Down Button release")
                    doButtonPress(Direction.Down, motionEvent)
                }
            }
            true
        }
        leftButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Left Button press")
                    doButtonPress(Direction.Left, motionEvent)
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Left Button release")
                    doButtonPress(Direction.Left, motionEvent)
                }
            }
            true
        }
        rightButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    customPrint("Right Button press")
                    doButtonPress(Direction.Right, motionEvent)
                }

                MotionEvent.ACTION_UP -> {
                    customPrint("Right Button release")
                    doButtonPress(Direction.Right, motionEvent)
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
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED) {
            return result
        }
        val bondedDevices = blueAdapter.bondedDevices
        if (bondedDevices.size > 0) {
            val devices = bondedDevices.toTypedArray() as Array<BluetoothDevice>
            var device: BluetoothDevice
            for(BD: BluetoothDevice in devices) {
                if(BD.address == "00:21:06:BE:5D:C5")
                {
                    device = BD;
                    val uuids = device.uuids
                    val socket = device.createRfcommSocketToServiceRecord(uuids[0].uuid)

                    try {
                        socket.connect()
                        result = true;
                    } catch (exception: Throwable) {
                        Log.e("error", "Connection Timeout?",exception)
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
        if(result) {
            connectionInfoView.setText("Connected!");
        }
        reconnectButton.isEnabled = true
        return result
    }
    // Custom Print, to get Debug-output while the app is running
    // Remove when app is fully done
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

    // Handle the Buttons. Send data via BT here later
    fun doButtonPress(direction: Direction, motionEvent: MotionEvent) {
        val cmd: String;
        when (direction) {
            Direction.Up -> {
                infoTextView.setTextColor(Color.MAGENTA);
                cmd = "up"
                outputStream.write(("+"+cmd+";").toByteArray());
            }

            Direction.Down -> {
                infoTextView.setTextColor(Color.YELLOW);
                cmd = "down"
                outputStream.write(("+"+cmd+";").toByteArray());
            }

            Direction.Left -> {
                infoTextView.setTextColor(Color.RED);
                cmd = "left"
                outputStream.write(("+"+cmd+";").toByteArray());
            }

            Direction.Right -> {
                infoTextView.setTextColor(Color.GREEN);
                cmd = "right"
                outputStream.write(("+"+cmd+";").toByteArray());
            }

            else -> {
                println("that should not happen. WTF?");
                customPrint("ERROR: that should not happen. WTF?", Color.RED);
            }
        }
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            infoTextView.text = "";
        } else {
            infoTextView.text = direction.toString();
        }
    }
}