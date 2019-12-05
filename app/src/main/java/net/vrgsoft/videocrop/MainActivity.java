package net.vrgsoft.videocrop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.vrgsoft.videcrop.VideoCropActivity;
import net.vrgsoft.videcrop.util.FileUtils;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private static final int OPEN_DOCUMENT_REQUEST = 100;
    private static final int CROP_REQUEST = 200;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("video/mp4");
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_DOCUMENT_REQUEST) {
            String outputPath = getVideoFolderPath() + "test.mp4";
            String inputPath = FileUtils.getPath(this, data.getData());
            Log.d(TAG, "inputpath:" + inputPath + " outputpath:" + outputPath);
            startActivityForResult(VideoCropActivity.createIntent(this, inputPath, outputPath), CROP_REQUEST);
        }
        if(requestCode == CROP_REQUEST && resultCode == RESULT_OK){
            //crop successful
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
