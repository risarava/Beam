package com.example.apichaya.addrealmsudent.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apichaya.addrealmsudent.Model.RgbColorObject;
import com.example.apichaya.addrealmsudent.R;
import com.example.apichaya.addrealmsudent.adepter.MyAdapter;
import com.example.apichaya.addrealmsudent.customs.AbstractToolbarActivity;
import com.example.apichaya.addrealmsudent.database.TestManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AbstractToolbarActivity implements View.OnClickListener {

    private int chemicalId = 0;

    Button btnAssay;
    Button btnSendData;
    TextView btn2;
    Button btnAssayPercent;
    Bitmap bitmap;
    ImageView imageview;
    TextView tvRedvalue;
    TextView tvGreenvalue;
    TextView tvBluevalue;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btnAdd;
    private Button btnDelete;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    TextView total;
    int sum = 0;
    ArrayList<RgbColorObject> rgbColorObjectArrayList = new ArrayList<>();
    private TestManager testManager;

    @Override
    protected int setContentView() {
        return R.layout.activity_add;
    }

    @Override
    protected void bindActionbar(ImageView imgIcon, ImageView menuLeft, LinearLayout toolbar, TextView txtTitleToolbar) {
        setTitle("เพิ่ม");
    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        btn2 = (TextView) findViewById(R.id.textviewCalculate);
        btnAssay = (Button) findViewById(R.id.btnAssay);
        btnAssayPercent = (Button) findViewById(R.id.btnAssayPerCent);
        imageview = (ImageView) findViewById(R.id.quick_start_cropped_image);
        btnSendData = (Button) findViewById(R.id.sentDataButton);
        btnAdd = (Button) findViewById(R.id.addButton);
        btnDelete = (Button) findViewById(R.id.delButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tvRedvalue = (TextView) findViewById(R.id.tvRedvalue);
        tvGreenvalue = (TextView) findViewById(R.id.tvGreenvalue);
        tvBluevalue = (TextView) findViewById(R.id.tvBluevalue);

        btn2.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        initInstance();

        chemicalId = getIntent().getIntExtra(MainActivity.EXTRA_CHEMICAL_ID, 0);
        testManager = new TestManager();
        onBackPressedButtonLeft();

    }

    @Override
    protected void setupUI() {
        initRecycleView();
        showData();
    }

    private void initInstance() {

        btnAssay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAverageColorRGB(bitmap);
            }
        });
        btnAssayPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAverageColorRGBpercent(bitmap);
            }
        });
    }

    private void initRecycleView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(getApplicationContext(), rgbColorObjectArrayList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void deleteData() {
        testManager.deleteAll();
    }

    private void showData() {
        rgbColorObjectArrayList = new ArrayList<>();
        rgbColorObjectArrayList = testManager.query(chemicalId);
        mAdapter.setRgbColorObjectArrayList(rgbColorObjectArrayList);
    }

    private void writeToDB(int id, int chemicalId, final int redValue, final int greenValue, final int blueValue) {
        testManager.addChemical(new RgbColorObject(id, chemicalId, redValue, greenValue, blueValue));
    }

    public void onSelectImageClick(View view) {
        CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setGuidelinesColor(Color.BLACK)
                .setBorderCornerColor(Color.BLACK)
                .setBorderLineColor(Color.BLACK)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(400, 400)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageview.setImageBitmap(bitmap);
//                imageview.setImageURI(result.getUri());
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public int[] getAverageColorRGB(Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        int size = width * height;
        int pixelColor;
        int r, g, b;
        r = g = b = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixelColor = bitmap.getPixel(x, y);
                if (pixelColor == 0) {
                    size--;
                    continue;
                }
                r += Color.red(pixelColor);
                g += Color.green(pixelColor);
                b += Color.blue(pixelColor);
            }
        }
        r /= size;
        g /= size;
        b /= size;

        tv1.setText(String.valueOf(r));
        tv2.setText(String.valueOf(g));
        tv3.setText(String.valueOf(b));
        Log.e("Color red = ", String.valueOf(r));
        Log.e("Color G = ", String.valueOf(g));
        Log.e("Color B = ", String.valueOf(b));

        return new int[]{
                r, g, b
        };
    }

    public int[] getAverageColorRGBpercent(Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        int size = width * height;
        int pixelColor;
        int r, g, b;
        r = g = b = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixelColor = bitmap.getPixel(x, y);
                if (pixelColor == 0) {
                    size--;
                    continue;
                }
                r += Color.red(pixelColor);
                g += Color.green(pixelColor);
                b += Color.blue(pixelColor);
            }
        }
        r /= size;
        g /= size;
        b /= size;

        Double total = Double.parseDouble(String.valueOf(r + g + b));
        Double percentRed = (r * 100) / total;
        Double percentGreen = (g * 100) / total;
        Double percentBlue = (b * 100) / total;

        tv1.setText(String.format("Red: %.2f ", percentRed) + " %");
        tv2.setText(String.format("Green: %.2f ", percentGreen) + " %");
        tv3.setText(String.format("Blue: %.2f ", percentBlue) + " %");

        Log.e("Color red = ", String.valueOf(r));
        Log.e("Color G = ", String.valueOf(g));
        Log.e("Color B = ", String.valueOf(b));

        return new int[]{
                r, g, b
        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textviewCalculate:
                Intent intent = new Intent(AddActivity.this, CalculateActivity.class);
                startActivity(intent);
                break;
            case R.id.addButton:
                if (tv1.length() == 0 && tv2.length() == 0 && tv3.length() == 0) {
                    return;
                }
                mAdapter.addRgbColor(new RgbColorObject(
                        Integer.parseInt(tv1.getText().toString().trim()),
                        Integer.parseInt(tv2.getText().toString().trim()),
                        Integer.parseInt(tv3.getText().toString().trim())));
                try {
                    writeToDB(testManager.getSize() + 1, chemicalId,
                            Integer.parseInt(tv1.getText().toString().trim()),
                            Integer.parseInt(tv2.getText().toString().trim()),
                            Integer.parseInt(tv3.getText().toString().trim()));
                } catch (Exception e) {
                    Toast.makeText(AddActivity.this, "Input Data!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.delButton:
                deleteData();
                showData();
                break;

            default:
                break;
        }
    }
}