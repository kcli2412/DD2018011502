package com.example.student.dd2018011502;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView tv, tv2, tv3;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        pb = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void click1(View v)
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();

                String str_url = "https://5.imimg.com/data5/UH/ND/MY-4431270/red-rose-flower-500x500.jpg";
                URL url = null;
                try {
                    url = new URL(str_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    final int totalLength = conn.getContentLength();
                    int sum = 0;
                    int length;
                    while ((length = inputStream.read(buf)) != -1)
                    {
                        sum += length;
                        final int tmp = sum;
                        bos.write(buf, 0, length);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(String.valueOf(tmp) + "/" + String.valueOf(totalLength));
                                pb.setProgress(100 * tmp / totalLength);
                            }
                        });
                    }

                    byte[] results = bos.toByteArray();
                    final Bitmap bmp = BitmapFactory.decodeByteArray(results, 0, results.length);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(bmp);
                        }
                    });
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void click2(View v)
    {
        MyTask task = new MyTask();
        task.execute(10);
    }

    class MyTask extends AsyncTask <Integer, Integer, String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv3.setText(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tv2.setText(values[0].toString());
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i < integers[0]; i++)
            {
                try {
                    Thread.sleep(1000);
                    Log.d("TASK", "doInBackground: i:" + i);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "okay";
        }
    }

    public void click3(View v)
    {
        MyImageTask task = new MyImageTask();
        task.execute("https://5.imimg.com/data5/UH/ND/MY-4431270/red-rose-flower-500x500.jpg");
    }

    class MyImageTask extends AsyncTask<String, Integer, Bitmap>
    {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("TASK", "onProgressUpdate: values:" + values[0]);
            tv.setText(values[0].toString());
            pb.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                final int totalLength = conn.getContentLength();
                int sum = 0;
                int length;
                while ((length = inputStream.read(buf)) != -1)
                {
                    sum += length;
                    final int tmp = sum;
                    bos.write(buf, 0, length);
                    publishProgress(100 * tmp / totalLength);
                }
                byte[] results = bos.toByteArray();
                final Bitmap bmp = BitmapFactory.decodeByteArray(results, 0, results.length);
                return  bmp;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
