package com.teaminversion.envisionbuddy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    String mCurrentPhotoPath;
    static MainRecyclerViewAdapter mainAdapter;
    MainRecyclerViewAdapter featuredAdapter;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_STORAGE_PERMISSION_CODE = 101;
    static ArrayList<Map<String, String>> recentList = new ArrayList<>();
    ArrayList<Map<String, String>> featuredList = new ArrayList<>();
    static String resultText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            getActivity().getCurrentFocus().clearFocus();
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.teaminversion.envisionbuddy", Context.MODE_PRIVATE);
        recentList.clear();
        try {
            recentList = (ArrayList<Map<String, String>>) ObjectSerializer.deserialize(sharedPreferences.getString("recentList", ObjectSerializer.serialize(new ArrayList<Map<String, String>>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultText = "";
        RecyclerView mainRecyclerView = root.findViewById(R.id.mainRecyclerView);
        mainAdapter = new MainRecyclerViewAdapter(recentList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setStackFromEnd(true);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();

        RecyclerView featuredRecyclerView = root.findViewById(R.id.featuredRecyclerView);
        featuredAdapter = new MainRecyclerViewAdapter(featuredList, getActivity());
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        featuredRecyclerView.setLayoutManager(featuredLayoutManager);
        featuredRecyclerView.setAdapter(featuredAdapter);
        featured();
        featuredAdapter.notifyDataSetChanged();

        FloatingActionButton scanButton = root.findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                dispatchTakePictureIntent();
            }

        });

        ImageButton clearImageButton = root.findViewById(R.id.clearImageButton);
        clearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recentList.clear();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.teaminversion.envisionbuddy", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("recentList", ObjectSerializer.serialize(recentList)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_PERMISSION_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (photoFile != null) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAutoZoomEnabled(true)
                            .start(getContext(), this);
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Camera permission granted", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == MY_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Storage permission granted", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
                CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                Uri resultUri = cropResult.getUri();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                if (bitmap != null) {
                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                    TextRecognizer recognizer = TextRecognition.getClient();
                    Task<Text> result = recognizer.process(image)
                            .addOnSuccessListener(visionText -> processTextBlock(visionText))
                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show());
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void processTextBlock(Text result) {
        resultText = result.getText();

        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();

            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();

                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
        if (!resultText.equals("")) {
            Navigation.findNavController(getView()).navigate(R.id.navigation_text);
        }else{
            Toast.makeText(getActivity(), "No text detected", Toast.LENGTH_SHORT).show();
        }
    }

    public void featured(){
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> thumbnails = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();
        names.add("Planets");
        thumbnails.add("https://lh3.googleusercontent.com/EzL3Wi-kjXgjC7OPW2KQNWXro1j1CMm4XgsTUJQ-5lOkzjtMs5RV8oriBUa6ZHZKBQ");
        urls.add("https://poly.googleapis.com/downloads/fp/1613667709106752/3_tN7i962hZ/cQmPxVolxwx/Planets.gltf");
        names.add("Brain");
        thumbnails.add("https://lh3.googleusercontent.com/J5ECPYxwa1LKsvt5XlzIuYnHBTOqVnuuVpjg4mBzfKEy0h0X8Jth7qnAwnDqBhAmWA");
        urls.add("https://poly.googleapis.com/downloads/fp/1612223001194184/6TULi_bk__6/2spgcll4Bib/Brain.gltf");
        names.add("Flower-Pot");
        thumbnails.add("https://lh3.googleusercontent.com/b9WHZfpKFv--ArhI0hm0OZEc0Zbma-er7_i5bbIIvAVXziBDZiOBlswwNXOxzGOOjw");
        urls.add("https://poly.googleapis.com/downloads/fp/1617773971063287/bVepm2yfJTh/6fc4dNAJzB3/model.gltf");
        names.add("Satellite-dish");
        thumbnails.add("https://lh3.googleusercontent.com/N2UP-d0wuQpgv3yPDAvhoDUHmK4nrc2YXmavBjrcHktVxBgt8Ic0vVyTT0PvL0EMfQ");
        urls.add("https://poly.googleapis.com/downloads/fp/1617752884196294/5iVbfDhRnN7/31OrgkWD5Lx/SatelliteDish.gltf");
        names.add("Storage-Ship");
        thumbnails.add("https://lh3.googleusercontent.com/R_rOIi8aaB6q7q13gopjZE5cBOXmSmM860lyxLTACUZBCAgmCIBYyP3bDhfRWi_E");
        urls.add("https://poly.googleapis.com/downloads/fp/1617195046980481/bzRjbJ74JCr/a0rI-xVS90r/model.gltf");
        for (int i=0; i<4; i++) {
            Map<String, String> modelInfo = new HashMap<>();
            modelInfo.put("name", names.get(i));
            modelInfo.put("thumbnail", thumbnails.get(i));
            modelInfo.put("url", urls.get(i));
            featuredList.add(modelInfo);
        }
    }

}