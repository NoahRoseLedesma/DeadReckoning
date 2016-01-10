package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.NoahR.instruction;

/**
 * Created by Noah Rose-Ledesma on 1/5/2016.
 */

public class InstructionEdit extends Activity{
    static instruction editingInstruction;
    static int instructionNumber;
    TextView title;
    EditText motor1, motor2, motor3, motor4;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.instructionpop);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((width), (height));

        title = (TextView)findViewById(R.id.title);
        button = (Button)findViewById(R.id.delete);
        motor1 = (EditText)findViewById(R.id.motor1);
        motor2 = (EditText)findViewById(R.id.motor2);
        motor3 = (EditText)findViewById(R.id.motor3);
        motor4 = (EditText)findViewById(R.id.motor4);

        title.setText("Instruction #" + (instructionNumber + 1));
        motor1.setText(Integer.toString(editingInstruction.motorFRDest));
        motor2.setText(Integer.toString(editingInstruction.motorFLDest));
        motor3.setText(Integer.toString(editingInstruction.motorBRDest));
        motor4.setText(Integer.toString(editingInstruction.motorBLDest));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingInstruction = null;
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(editingInstruction != null) {
            editingInstruction.setValues(Integer.parseInt(motor1.getText().toString()), Integer.parseInt(motor2.getText().toString()), Integer.parseInt(motor3.getText().toString()), Integer.parseInt(motor4.getText().toString()));
        }
        FtcRobotControllerActivity.activityAtomicReference.get().setInstruction(instructionNumber, editingInstruction);
        super.onDestroy();
    }

}
