package com.example.android.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    final DatabaseHelper db = new DatabaseHelper(this);

    /*//variables for graphview - start
    String title="Assignment-2";
    String[] verlabels = new String[]{"70", "60", "50", "40", "30", "20","10","0"};
    String[] horlabels = new String[]{"0", "10", "20", "30", "40", "50", "60","70"};
    private float[] values = new float[10]; //remove once x,y,z array fixed

    GraphView gv;
    LinearLayout root;
    */
    // three different arrays to store x,y,z values from accModel once queried from databases
    private float[] xValues = new float[10];
    private float[] yValues = new float[10];
    private float[] zValues = new float[10];

    private int x=1;
    //variables for graphview - end
    private LineGraphSeries<DataPoint> series;

    //variables for input fields
    EditText pid, age, name;
    RadioGroup gender;
    RadioButton g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;


       /* //graph view
        root=(LinearLayout) findViewById(R.id.graphview_layout);
        gv=new GraphView(this, values, title, horlabels, verlabels, GraphView.LINE); //TODO:update values for x,y,z*/

        //input fields
        pid = (EditText) findViewById(R.id.pid_editText);
        age = (EditText) findViewById(R.id.age_editText);
        name = (EditText) findViewById(R.id.pname_editText);
        gender = (RadioGroup) findViewById(R.id.radioGroup);

        //Buttons
        Button create_database = (Button) findViewById(R.id.create_db_btn);
        Button run_btn = (Button) findViewById(R.id.run_button);
        Button stop_btn = (Button) findViewById(R.id.stop_button);
        Button upload_btn = (Button) findViewById(R.id.upload_btn);
        GraphView graph = (GraphView) findViewById(R.id.graph);

        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-5);
        viewport.setMaxY(5);
        viewport.setScrollable(true);

        //TODO:pulls 10 most recent data from database and show in the graph, graph updates each second
        run_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processLatestAccValues();
                //pass to graphview by calling setvalues method

                for(int i=0;i<10;i++){
                    String st="X value : "+xValues[i]+" Y Value : "+yValues[i]+" Z value : "+zValues[i];
                    Log.d("@@@@@@@@ THESE ARE THE LAST 10 VALUES RETRIEVED",st);
                }

                DataPoint[] dp_x=new DataPoint[10];
                DataPoint[] dp_y=new DataPoint[10];
                DataPoint[] dp_z=new DataPoint[10];

                for(int j=0;j<10;j++){
                    dp_x[j]=new DataPoint (j,xValues[j]);
                    dp_y[j]=new DataPoint (j,xValues[j]);
                    dp_z[j]=new DataPoint (j,xValues[j]);
                }
                series.resetData(dp_x);
                series.resetData(dp_y);
                series.resetData(dp_z);

            }
        });

        //TODO:clears the graph
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make run=false to stop drawing
                //gv.run=false;
            }
        });

        create_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selected = gender.getCheckedRadioButtonId();
                g=(RadioButton) findViewById(selected);

                //create db table name
                String table_name=name.getText()+"_"+pid.getText()+"_"+age.getText()+"_"+g.getText();

                //call dbhandler and create database
                try{
                    db.addTable(table_name);
                    Toast toast = Toast.makeText(getApplicationContext(),table_name,Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch(Exception ex){
                    Toast.makeText(context, "Error in adding table", Toast.LENGTH_SHORT).show();
                }

                Log.d("Rutvilkkk","sdnsuhdnusnfusndfsd----------");

                //initiate acc service
                Intent intentService = new Intent(MainActivity.this, AccelerometerService.class);
                startService(intentService);

                /*int selected = gender.getCheckedRadioButtonId();
                g=(RadioButton) findViewById(selected);
                //create db table name
                String table_name=name.getText()+"_"+pid.getText()+"_"+age.getText()+"_"+g.getText();
                //call dbhandler and create database
                try{
                    db.addTable(table_name);
                    Toast toast = Toast.makeText(getApplicationContext(),table_name,Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch(Exception ex){
                    Toast.makeText(context, "Error in adding table", Toast.LENGTH_SHORT).show();
                }
               Log.d("Rutvilkkk","sdnsuhdnusnfusndfsd----------");
                //initiate acc service
                Intent intentService = new Intent(MainActivity.this, AccelerometerService.class);
                startService(intentService);
*/


                /*series.appendData(new DataPoint(0,xValues[0]), true, 10);
                series.appendData(new DataPoint(1,xValues[1]), true, 10);
                series.appendData(new DataPoint(2,xValues[2]), true, 10);
                series.appendData(new DataPoint(3,xValues[3]), true, 10);
                series.appendData(new DataPoint(4,xValues[4]), true, 10);
                series.appendData(new DataPoint(5,xValues[5]), true, 10);
                series.appendData(new DataPoint(6,xValues[6]), true, 10);
                series.appendData(new DataPoint(7,xValues[7]), true, 10);
                series.appendData(new DataPoint(8,xValues[8]), true, 10);
                series.appendData(new DataPoint(9,xValues[9]), true, 10);*/


            }
        });

        //TODO:upload db to server
        upload_btn.setOnClickListener(new View.OnClickListener() {

            // Right now it just displays a row from the table

            @Override
            public void onClick(View v) {

                List<AccelerometerModel> fromDb = db.getLatestAccEntries();

                AccelerometerModel entry1=fromDb.get(1);
                String s="X value : "+entry1.x+" Y Value : "+entry1.y+" Z Value : "+entry1.z;
                Log.d("$#$#$#$#$#$#$#$#$#$#$",s);
            }
        });
    }

    private void processLatestAccValues(){
        List<AccelerometerModel> entries = db.getLatestAccEntries();

        if(entries!=null){  //if acc doesnt have any value
            int i=0;
            for(AccelerometerModel e: entries){
                xValues[i]=e.x;
                yValues[i]=e.y;
                zValues[i]=e.z;
                i++;
            }
        }


    }



}
