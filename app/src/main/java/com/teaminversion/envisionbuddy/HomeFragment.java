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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Map;

public class HomeFragment extends Fragment {

    String mCurrentPhotoPath;
    static MainRecyclerViewAdapter mainAdapter;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_STORAGE_PERMISSION_CODE = 101;
    static ArrayList<Map<String, String>> recentList = new ArrayList<>();
    static String resultText;
    Button continueButton;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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
                    /*Uri photoURI = FileProvider.getUriForFile(this, "com.teaminversion.envisionbuddy.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);*/
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
                //File file = new File(mCurrentPhotoPath);
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
        /*textView.setText(resultText);
        continueButton.setEnabled(true);
        continueButton.setOnClickListener(v -> {*/
        /*Intent intent = new Intent(getActivity(), ChoiceActivity.class);
        intent.putExtra("text", resultText);
        startActivity(intent);*/
        //});
        /*NotificationsFragment notificationsFragment = new NotificationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", resultText);
        notificationsFragment.setArguments(bundle);*/
        Navigation.findNavController(getView()).navigate(R.id.navigation_text);
    }

}