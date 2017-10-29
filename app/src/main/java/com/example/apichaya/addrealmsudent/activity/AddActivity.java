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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apichaya.addrealmsudent.Model.RgbColorObject;
import com.example.apichaya.addrealmsudent.R;
import com.example.apichaya.addrealmsudent.adepter.MyAdapter;
import com.example.apichaya.addrealmsudent.customs.AbstractToolbarActivity;
import com.example.apichaya.addrealmsudent.database.ChemicalManager;
import com.example.apichaya.addrealmsudent.database.TestManager;
import com.example.apichaya.addrealmsudent.dialog.MyAlertDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AbstractToolbarActivity implements View.OnClickListener {

    private int chemicalId = 0;
    private int redValue = 0;
    private int greenValue = 0;
    private int blueValue = 0;
    private Bitmap bitmap;

    //    TextView btn2;
    private TextView txtName;
    private TextView txtRed;
    private TextView txtGreen;
    private TextView txtBlue;
    private TextView txtAdd;
    private TextView txtRedPercent;
    private TextView txtGreenPercent;
    private TextView txtBluePercent;
    private ImageView imageview;
    private RecyclerView mRecyclerView;

    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int sum = 0;
    ArrayList<RgbColorObject> rgbColorObjectArrayList = new ArrayList<>();
    private TestManager testManager;
    private ChemicalManager chemicalManager;

    @Override
    protected int setContentView() {
        return R.layout.activity_add;
    }

    @Override
    protected void bindActionbar(ImageView imgIcon, ImageView menuLeft, LinearLayout toolbar, TextView txtTitleToolbar) {
        setTitle("Add experiment");
    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
//        btn2 = (TextView) findViewById(R.id.textviewCalculate);
        txtName = (TextView) findViewById(R.id.textviewName);
        txtAdd = (TextView) findViewById(R.id.textviewAdd);
        txtRedPercent = (TextView) findViewById(R.id.textviewRedPercent);
        txtGreenPercent = (TextView) findViewById(R.id.textviewGreenPercent);
        txtBluePercent = (TextView) findViewById(R.id.textviewBluePercent);
        txtRed = (TextView) findViewById(R.id.textviewRed);
        txtGreen = (TextView) findViewById(R.id.textviewGreen);
        txtBlue = (TextView) findViewById(R.id.textviewBlue);
        imageview = (ImageView) findViewById(R.id.quick_start_cropped_image);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

//        btn2.setOnClickListener(this);
        txtAdd.setOnClickListener(this);

        chemicalId = getIntent().getIntExtra(MainActivity.EXTRA_CHEMICAL_ID, 0);
        testManager = new TestManager();
        chemicalManager = new ChemicalManager();
        onBackPressedButtonLeft();

    }

    @Override
    protected void setupUI() {
        txtName.setText(chemicalManager.getChemicalName(chemicalId));
        initRecycleView();
        showData();
    }

    private void initRecycleView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(getApplicationContext(), rgbColorObjectArrayList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemDelete(new MyAdapter.OnItemDelete() {
            @Override
            public void onClicked(final int position, final int id) {
                MyAlertDialog.dialogAlert(activity, new MyAlertDialog.OnClickPositiveListener() {
                    @Override
                    public void onClicked() {
                        testManager.delete(id);
                        mAdapter.removePosition(position);
                    }
                });
            }
        });
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
                getAverageColorRGB(bitmap);
                getAverageColorRGBpercent(bitmap);

                txtAdd.setAlpha(1f);
                txtAdd.setEnabled(true);

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

        redValue = r;
        greenValue = g;
        blueValue = b;

        txtRed.setText(getString(R.string.red_value, redValue));
        txtGreen.setText(getString(R.string.green_value, greenValue));
        txtBlue.setText(getString(R.string.blue_value, blueValue));
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

        txtRedPercent.setText(String.format("%.2f ", percentRed) + " %");
        txtGreenPercent.setText(String.format("%.2f ", percentGreen) + " %");
        txtBluePercent.setText(String.format("Blu%.2f ", percentBlue) + " %");

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
//            case R.id.textviewCalculate:
//                Intent intent = new Intent(AddActivity.this, CalculateActivity.class);
//                startActivity(intent);
//                break;
            case R.id.textviewAdd:
                if (redValue == 0 && greenValue == 0 && blueValue == 0) {
                    return;
                }
                mAdapter.addRgbColor(new RgbColorObject(
                        redValue,
                        greenValue,
                        blueValue));
                try {
                    writeToDB(testManager.getSize() + 1, chemicalId,
                            redValue,
                            greenValue,
                            blueValue);
                } catch (Exception e) {
                    Toast.makeText(AddActivity.this, "Input Data!", Toast.LENGTH_SHORT).show();
                }
                imageview.setImageResource(R.drawable.add_image_icon);
                //Clear value
                redValue = 0;
                greenValue = 0;
                blueValue = 0;

                txtRed.setText(getString(R.string.red_value, redValue));
                txtGreen.setText(getString(R.string.green_value, greenValue));
                txtBlue.setText(getString(R.string.blue_value, blueValue));

                txtRedPercent.setText("0 %");
                txtGreenPercent.setText("0 %");
                txtBluePercent.setText("0 %");
                txtAdd.setAlpha(0.5f);
                txtAdd.setEnabled(false);
                break;

//            case R.id.delButton:
//                deleteData();
//                showData();
//                break;

            default:
                break;
        }
    }
}