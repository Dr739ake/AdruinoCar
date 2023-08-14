package de.it25a.remotecarcontroller


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var firstTextView_obj: TextView;
    private lateinit var klickMeButton_obj: Button;

    private lateinit var infoTextView_obj: TextView;

    private lateinit var upButton_obj: Button;
    private lateinit var downButton_obj: Button;
    private lateinit var leftButton_obj: Button;
    private lateinit var rightButton_obj: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTextView_obj = findViewById(R.id.firstTextView);
        klickMeButton_obj = findViewById(R.id.klickMeButton);

        klickMeButton_obj.setOnClickListener {
            firstTextView_obj.text = "Hello World!";
        };

        infoTextView_obj = findViewById(R.id.infoTextView);

        upButton_obj = findViewById(R.id.upButton);
        downButton_obj = findViewById(R.id.downButton);
        leftButton_obj = findViewById(R.id.leftButton);
        rightButton_obj = findViewById(R.id.rightButton);

        upButton_obj.setOnClickListener {
            infoTextView_obj.text = "up";
        };
        downButton_obj.setOnClickListener {
            infoTextView_obj.text = "down";
        };
        leftButton_obj.setOnClickListener {
            infoTextView_obj.text = "left";
        };
        rightButton_obj.setOnClickListener {
            infoTextView_obj.text = "right";
        };
        
    }
}