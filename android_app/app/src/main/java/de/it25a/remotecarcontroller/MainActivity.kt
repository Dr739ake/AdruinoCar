package de.it25a.remotecarcontroller


import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
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

    private lateinit var upButton: Button;
    private lateinit var downButton: Button;
    private lateinit var leftButton: Button;
    private lateinit var rightButton: Button;
    private lateinit var consoleData: LinkedList<String>;
    private lateinit var pairedDevices: Set<BluetoothDevice>;

    var bt: BluetoothAdapter? = null
    var bts: BluetoothSocket? = null
    val REQUEST_BLUETOOTH_PERMISSION: Int = 1
    val REQUEST_BLUETOOTH_ENABLE: Int = 2

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

        bt = BluetoothAdapter.getDefaultAdapter()
        if (bt == null) {
            // This device does not have Bluetooth.
            Toast.makeText(
                getApplicationContext(),
                "Device does not have a Bluetooth adapter therefore this application cannot run.",
                Toast.LENGTH_SHORT
            ).show();
            return;
        }
        bluetoothConnect();

        // Find the Elements, store them in the class-Variabeles
        consoleTextView = findViewById(R.id.consoleTextView);
        consoleData = LinkedList()

        infoTextView = findViewById(R.id.infoTextView);

        upButton = findViewById(R.id.upButton);
        downButton = findViewById(R.id.downButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);

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
    }

    fun bluetoothConnect() {
        val CONTEXT: Context = this;

        if (ContextCompat.checkSelfPermission(
                CONTEXT,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            if (bt?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_ENABLE)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    REQUEST_BLUETOOTH_PERMISSION
                )
                pairedDevices = bt?.bondedDevices!!
                pairedDevices?.forEach { device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                }
            }
        }
        else {
            // Request permission. That will call back to onActivityResult which in the case of success will call this method again.
            // Ask for permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
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
        when (direction) {
            Direction.Up -> {
                infoTextView.setTextColor(Color.MAGENTA);
            }

            Direction.Down -> {
                infoTextView.setTextColor(Color.YELLOW);
            }

            Direction.Left -> {
                infoTextView.setTextColor(Color.RED);
            }

            Direction.Right -> {
                infoTextView.setTextColor(Color.GREEN);
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