package com.example.academy_intern.sampledesign.Fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCodeGenerator extends Fragment {

    View view;
    Button btnGenerate;
    EditText edtItemName, edtItemPrice;
    ImageView ivQRCode;
    public final static int QRcodeWidth = 500;
    public final static int QRcodeHeight = 500;
//    private static final String IMAGE_DIRECTORY = "/uploads";
    private static String ITEM_NAME_AND_PRICE;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.qr_code_generator,container,false);

        edtItemName = view.findViewById(R.id.et_item_name);
        edtItemPrice = view.findViewById(R.id.et_item_price);
        btnGenerate = view.findViewById(R.id.generate);
        ivQRCode = view.findViewById(R.id.iv_code);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCode();
            }
        });
    }

    public void generateCode()
    {
        if (edtItemPrice.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Please enter a price.", Toast.LENGTH_LONG).show();
        } else {
            try {
                ITEM_NAME_AND_PRICE = edtItemName.getText().toString() + ";" + edtItemPrice.getText().toString();
                bitmap = TextToImageEncode(ITEM_NAME_AND_PRICE);
                ivQRCode.setImageBitmap(bitmap);
//                String path = saveImage(bitmap);
//                Toast.makeText(getActivity(), "QRCode saved to -> " + path, Toast.LENGTH_LONG).show();
//                saveImageToGallery();
                saveImage(bitmap);
                Toast.makeText(getActivity(), "QRCode saved to gallery", Toast.LENGTH_LONG).show();
                edtItemName.setText("");
                edtItemPrice.setText("");
            } catch (WriterException ex) {
                ex.printStackTrace();
            }

        }
    }



    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeHeight, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void saveImageToGallery()
    {
        ivQRCode.setDrawingCacheEnabled(true);
        Bitmap qrCodeImage = ivQRCode.getDrawingCache();
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), qrCodeImage, ITEM_NAME_AND_PRICE, ITEM_NAME_AND_PRICE);
    }


    public void saveImage(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/image.png");

        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
}