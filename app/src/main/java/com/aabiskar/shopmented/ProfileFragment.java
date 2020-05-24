package com.aabiskar.shopmented;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static android.content.Context.MODE_PRIVATE;
import static com.aabiskar.shopmented.extras.KEYS.KEY_NAME;
import static com.aabiskar.shopmented.extras.KEYS.KEY_SHARED_PREFS;
import static com.aabiskar.shopmented.extras.KEYS.KEY_USER_ID;
import static com.aabiskar.shopmented.extras.KEYS.KEY_UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    ImageView qrImgView;
    private TextView  memberCardName;
    private CardView history_card_view;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.yellow,getActivity().getTheme()));

        getActivity().getWindow().setStatusBarColor(Color.WHITE);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        View v = inflater.inflate(R.layout.fragment_profile, container, false);;
        qrImgView = v.findViewById(R.id.profile_qr_imgview);
        history_card_view = v.findViewById(R.id.profile_history_card);
        memberCardName = v.findViewById(R.id.profile_memberShipCardName);
        history_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFS, MODE_PRIVATE);
                int user_id = sharedPreferences.getInt(KEY_USER_ID, 0);

                Intent history_intent = new Intent(getActivity(), HistoryTransactionIdList.class);

                history_intent.putExtra("customer_id",String.valueOf(user_id));
                startActivity(history_intent);
            }
        });

        loadSharedPrefData();

        return v;

    }


        private void genQR(String customerId, ImageView imageView){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(customerId, BarcodeFormat.QR_CODE,200,200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

    private void loadSharedPrefData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY_SHARED_PREFS, MODE_PRIVATE);
        String uuid = sharedPreferences.getString(KEY_UUID, "");
        int user_id = sharedPreferences.getInt(KEY_USER_ID, 0);
        String user_name = sharedPreferences.getString(KEY_NAME, "").toUpperCase();
        String user_id_qr = user_id+"";
        if (!uuid.isEmpty()) {
            genQR(user_id_qr,qrImgView);
            memberCardName.setText(user_name);
            //     Toast.makeText(getActivity(), "WELCOME,"+uuid, Toast.LENGTH_SHORT).show();
        }
    }
}
