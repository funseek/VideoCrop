package net.vrgsoft.videocrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import net.vrgsoft.videcrop.VideoCropActivity;
import net.vrgsoft.videcrop.util.FileUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;


public class MainActivity extends AppCompatActivity {
    private static final int OPEN_DOCUMENT_REQUEST = 100;
    private static final int CROP_REQUEST = 200;
    private static final String TAG = "MainActivity";

    private String outputPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setType("video/mp4");
                startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_DOCUMENT_REQUEST) {
            outputPath = getVideoFolderPath() + "/" + (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) )+ ".mp4";
            String inputPath = FileUtils.getPath(this, data.getData());
            Log.d(TAG, "inputpath:" + inputPath + " outputpath:" + outputPath);
            startActivityForResult(VideoCropActivity.createIntent(this, inputPath, outputPath, "30"), CROP_REQUEST);
        }
        if(requestCode == CROP_REQUEST && resultCode == RESULT_OK){
            //crop successful
            VideoView video = (VideoView)findViewById(R.id.videoView);
            video.setVideoURI(Uri.parse(outputPath));
            video.start();
        }
    }

    private String getVideoFolderPath() {
        //create destination directory
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).toString() + "/VideoCrop/");
        if (f.mkdirs() || f.isDirectory()) {
            return f.getPath();
        }
        return f.getPath();
    }

}
