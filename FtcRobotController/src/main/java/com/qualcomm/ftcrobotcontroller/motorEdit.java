package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.NoahR.globalInfoPacket;

/**
 * Created by Noah Rose-Ledesma on 1/5/2016.
 */

public class motorEdit extends Activity{
    static globalInfoPacket editingGlobalInfoPacket;
    TextView title;
    EditText cprinput, gearinput, cirinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.motorpop);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((width), (height));

        title = (TextView)findViewById(R.id.motorTitle);
        cprinput = (EditText)findViewById(R.id.cprinput);
        gearinput = (EditText)findViewById(R.id.gearinput);
        cirinput = (EditText)findViewById(R.id.cirinput);

        title.setText("Motors");
        cprinput.setText(Integer.toString(editingGlobalInfoPacket.encoderCPR));
        gearinput.setText(Float.toString(editingGlobalInfoPacket.gearRatio));
        cirinput.setText(Float.toString(editingGlobalInfoPacket.wheelCircumference));
    }

    @Override
    protected void onDestroy() {
        editingGlobalInfoPacket.setEncoderCPR(Integer.parseInt(cprinput.getText().toString()));
        editingGlobalInfoPacket.setGearRatio(Float.parseFloat(gearinput.getText().toString()));
        editingGlobalInfoPacket.setWheelCircumference(Float.parseFloat(cirinput.getText().toString()));
        FtcRobotControllerActivity.activityAtomicReference.get().setGlobalInfo(editingGlobalInfoPacket);
        super.onDestroy();
    }
}
