package com.example.lakshmi.backup;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.opencv.core.Core.subtract;

public class Main2Activity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    String path;
    MediaPlayer mediaPlayer;
    int length;
    Button start_button;
    Button stop_button;
    Button pause_button;
    AsyncTaskBitmap disp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         start_button = (Button)findViewById(R.id.start);
         stop_button = (Button)findViewById(R.id.stop);
         pause_button = (Button)findViewById(R.id.pause);


        Bundle bundle = getIntent().getExtras();
        path = bundle.getString("path");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }



    public void start(View view) throws IOException {
       disp= new AsyncTaskBitmap(path);
        disp.execute();
        start_button.setEnabled(false);
        stop_button.setEnabled(true);
        pause_button.setEnabled(true);
    }

    public void pause(View view) throws IOException {
        if(!disp.getPause() )
        {
            disp.setPause(true);
            length = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }

        else
        {
            disp.setPause(false);
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
        }
    }

    public void stop(View view) throws IOException {
        Log.d(this.getClass().getSimpleName(), "stop works ");
        disp.cancel(true);
        mediaPlayer.stop();
        mediaPlayer.release();

        start_button.setEnabled(true);
        stop_button.setEnabled(false);
        pause_button.setEnabled(false);

        // Bundle bundle = uegetIntent().getExtras();
        //String path = bundle.getString("path");

        //AsyncTaskBitmap disp = new AsyncTaskBitmap(path);

        //disp.execute();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lakshmi.backup/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lakshmi.backup/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class AsyncTaskBitmap extends AsyncTask<Void, Bitmap, Void> {


        Bitmap bm;
        private String pathName;

        boolean pause = false;
        int count = 0;

        AsyncTaskBitmap(String pName) {
            pathName = pName;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String che = Environment.getExternalStorageDirectory().getAbsolutePath();
                //String path1 = che + "/WhatsApp/Media/WhatsApp Images/IMG-20160325-WA0006.jpg";
                //String path1 ="/storage/emulated/0/bluetooth/20160314_231514-1.jpg";
                /*****************************below image is important**************************/
                //String path1 = che + "/DCIM/Camera/IMG_20160403_125331.jpg";


                Mat image = Imgcodecs.imread(pathName + "/Image.jpg");

                //Mat resizeimage = new Mat();
                Size sz = new Size(500, 500);
                Imgproc.resize(image, image, sz);


                // Log.d(this.getClass().getSimpleName(), "width of " + image.width());


                Mat gray = new Mat(image.size(), CvType.CV_8U);
                Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);//gray scale

                Mat bw = new Mat(gray.size(), gray.type());
                Mat invertcolormatrix = new Mat(gray.rows(), gray.cols(), gray.type(), new Scalar(255, 255, 255));
                subtract(invertcolormatrix, gray, gray);//inverted gray image
                Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);//changed here

                int horizontalsize = bw.cols() / 30;

                Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize, 1));
                Imgproc.erode(bw, bw, horizontalStructure, new Point(-1, -1));
                Imgproc.dilate(bw, bw, horizontalStructure, new Point(-1, -1));


                Mat grayImage = new Mat(image.size(), CvType.CV_8U);  //CvType.CV_8UC1
                Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

                Mat binaryImage = new Mat(grayImage.size(), grayImage.type());
                Imgproc.threshold(grayImage, binaryImage, 100, 255, Imgproc.THRESH_BINARY_INV);//changed here

                final Mat bgImage = new Mat(image.rows(), image.cols(), CvType.CV_8U, new Scalar(0, 0, 0));//background image commented


                int error = 300;
                int count = 0;
                int previous = 0;
                double y1 = 0;
                double y2 = 0;
                int col_width = bw.cols();//assuming 500
                int threshold = col_width - error;
                //int width=0;

                for (int i = 0; i < bw.rows(); i++) {
                    count = 0;
                    for (int j = 0; j < bw.cols(); j++) {
                        //double [] ptr=bw.get(j,i);
                        double[] ptr = bw.get(i, j); //changed here
                        //       Log.d(this.getClass().getSimpleName(), "value : " +ptr[0] );

                        if (ptr[0] == 255) {
                            count = count + 1;
                        }
                    }

                    if ((count) > threshold)//if number of white pixels is greater than the threshold
                    {
                        if (previous == 0)//if y1 is not set
                        {
                            y1 = i;
                            i = i + 10; //skip these many rows
                            previous = 1;//changed here
                        } else {
                            y2 = i;
                            break;//break the outer for loop
                        }
                    }
                }

                int width = (int) ((int) y2 - y1);


                int delay = 30;
                int thickness = 5;//5; Change it to some higher no.
                int counter = 0;
                int font_size = width;


                bm = Bitmap.createBitmap(bw.cols(), bw.rows(), Bitmap.Config.ARGB_8888);
                //final ImageView iv = (ImageView) findViewById(R.id.imageView1);
                //Utils.matToBitmap(binaryImage, bm);
                //iv.setImageBitmap(bm);


                double param = (width * 0.25) + y1;
                int start = (int) param;


                String json_str = loadJSONFromAsset();
                JSONObject json_obj = null;
                try {
                    json_obj = new JSONObject(json_str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray json_arr = null;
                int time1;
                int line;
                int sp;//stores cumulative speed at a time
                ArrayList<Integer> time_list = new ArrayList<Integer>();
                ArrayList<Integer> lines = new ArrayList<Integer>();
                ArrayList<Integer> speed_list = new ArrayList<Integer>();
                try {
                    if (json_obj != null) {
                        json_arr = json_obj.getJSONArray("arr");
                    }
                    if (json_arr != null) {
                        for (int i = 0; i < json_arr.length(); i++) {
                            JSONObject json_item = json_arr.getJSONObject(i);
                            time1 = json_item.getInt("time");
                            line = json_item.getInt("lines");
                            sp = json_item.getInt("speed");
                            time_list.add(i, time1);
                            lines.add(i, line);
                            speed_list.add(i, sp);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int standard_animate_speed = 100;
                ArrayList<Integer> lines_array = new ArrayList<Integer>();
                for (int i = 0; i < lines.size(); i++) {
                    int num_lines = lines.get(i);
                    int speed_var = 0;
                    while (num_lines > 0) {
                        speed_var = standard_animate_speed + ((speed_list.get(i) * standard_animate_speed) / 100);
                        lines_array.add(speed_var);//till the num of lines becomes zero,maintain the same animation speed.
                        num_lines = num_lines - 1;
                    }
                }

                // playAudio();
                mediaPlayer = new MediaPlayer();
                //String audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SHAREit/audios/Voice 006.m4a";
                String audioFilePath = pathName + "/Audio.m4a";
                mediaPlayer.setDataSource(audioFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();

                int line_count = 0;
                int animate_speed;
                int length;
                for (int k = start; k <= image.rows(); k = k + font_size) {
                    animate_speed = lines_array.get(line_count);
                    for (int i = 0; i < image.cols(); i++) {
                        for (int j = k; (j < (k + font_size)) && (j < image.rows()); j++) {
                            if(isCancelled())
                            {
                                 return null;
                            }

                            else {
                                while (pause) {
                                    Thread.sleep(10);

                                }

                            }

                            double[] data1 = binaryImage.get(j, i);
                            bgImage.put(j, i, data1);
                        }

                        if ((i % thickness) == 0) {

                            //Log.d(this.getClass().getSimpleName(), "hello ");
                            Utils.matToBitmap(bgImage, bm);
                            // AsyncTaskBitmap disp = new AsyncTaskBitmap();
                            //disp.execute(bm);
                            Thread.sleep(animate_speed);

                            publishProgress(bm);


                        }


                    }
                    line_count = line_count + 1;

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String loadJSONFromAsset() {
            String json = null;
            try {

                InputStream is = getAssets().open("time_file.json");

                int size = is.available();

                byte[] buffer = new byte[size];

                is.read(buffer);

                is.close();

                json = new String(buffer, "UTF-8");


            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;

        }

        public void setPause(boolean pau)
        {
            pause = pau;
        }

        public boolean getPause()
        {
            return pause;
        }

        public void playAudio() throws IOException {
            MediaPlayer mediaPlayer = new MediaPlayer();
            String audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SHAREit/audios/Voice 006.m4a";
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }


        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }


        //@Override
        protected void onProgressUpdate(Bitmap... pars) {
            ImageView iv = (ImageView) findViewById(R.id.imageView1);
            iv.setImageBitmap(pars[0]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
