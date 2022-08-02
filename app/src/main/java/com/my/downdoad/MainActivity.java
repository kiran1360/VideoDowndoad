package com.my.downdoad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;



public class MainActivity extends AppCompatActivity {
    String URL=null;
EditText getreellink;
Button getreel;
VideoView mperticulaerreel;
Button downloadreel;
 MediaController mediaController;
String reelurl="1";
 Uri uri2;


 ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);*/


        getreellink=findViewById(R.id.getReelLink);
        getreel=findViewById(R.id.getReel);
        mperticulaerreel=findViewById(R.id.pass);
        downloadreel=findViewById(R.id.downloadReel);
        mediaController=new MediaController(MainActivity.this);
        mediaController.setAnchorView(mperticulaerreel);

        dialog=new ProgressDialog(MainActivity.this);
        dialog.setTitle("loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("please wait...");



        getreel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                try {
                    URL = getreellink.getText().toString().trim();
                    if (getreellink.equals("NULL")) {
                        Toast.makeText(MainActivity.this, "Paste URL", Toast.LENGTH_SHORT).show();
                    }


                    String result2 = StringUtils.substringBefore(URL, "/?");
                    URL = result2 + "/?__a=1";
                } catch (Exception e) {
                    e.printStackTrace();
                }
             //   proccessdata();


                StringRequest request=new StringRequest(URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();


                        try {
                            MainURL mainURL = gson.fromJson(response, MainURL.class);
                            reelurl = mainURL.getGraphql().getShortcode_media().getVideo_url();
                            uri2 = Uri.parse(reelurl);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }


                        mperticulaerreel.setMediaController(mediaController);
                        mperticulaerreel.setVideoURI(uri2);
                        mperticulaerreel.requestFocus();
                        mperticulaerreel.start();
                        dialog.dismiss();
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Not Able To Fetch", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                queue.add(request);





                downloadreel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!reelurl.equals("1"))
                        {
                            DownloadManager.Request request=new DownloadManager.Request(uri2);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download..");
                            request.setDescription(".......");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,""+System.currentTimeMillis()+".mp4");
                            DownloadManager manager=(DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                            Toast.makeText(MainActivity.this, "Downloaded..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Video Not Downloaded..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

   /* private void proccessdata() {


    }*/
}