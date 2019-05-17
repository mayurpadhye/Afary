package com.cube9.afary.vendor.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cube9.afary.BuildConfig;
import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.cube9.afary.vendor.view.TakePhotoFragment.RequestPermissionCode;

public class SelectDocumentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String convertedImage="";
    Bitmap myBitmap;
    Uri picUri;
    public static File file_doc;
    View v;
    @BindView(R.id.rg_doc_type)
    RadioGroup rg_doc_type;

    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.btn_upload)
    Button btn_upload;
String imageFilePath="";
    @BindView(R.id.iv_documemt)
    ImageView iv_documemt;
    public static String doc_type="";
    private OnFragmentInteractionListener mListener;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public SelectDocumentFragment() {
        // Required empty public constructor
    }


    public static SelectDocumentFragment newInstance(String param1, String param2) {
        SelectDocumentFragment fragment = new SelectDocumentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_select_document, container, false);
        ButterKnife.bind(this,v);
        rg_doc_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    doc_type=""+checkedRadioButton.getText();
                //    Toast.makeText(getActivity(), ""+ checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    @OnClick(R.id.btn_next)
    public void onNextClick()
    {
        if (doc_type.isEmpty())
        {
            CustomUtils.showToast(getResources().getString(R.string.please_select_doc_type),getActivity(),MDToast.TYPE_ERROR);
        }
        else if (convertedImage.isEmpty())
        {

            CustomUtils.showToast(getResources().getString(R.string.please_select_doc_type),getActivity(),MDToast.TYPE_ERROR);
        }

        else
        ((VendorDetailsActivity)getActivity()).handleNextClick();
    }
    @OnClick(R.id.btn_upload)
    public void onUploadClick()
    {

        if(checkPermission()){
            final CharSequence[] options = { getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from_gallery),getResources().getString(R.string.cancel) };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.upload_document));
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getResources().getString(R.string.take_photo)))
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",f));
                        startActivityForResult(intent, 1);
                    }
                    else if (options[item].equals(getResources().getString(R.string.choose_from_gallery)))
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
        else {
            requestPermission();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                file_doc = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : file_doc.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        file_doc = temp;

                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(file_doc.getAbsolutePath(), bitmapOptions);
                    iv_documemt.setImageBitmap(bitmap);

                    convertedImage = convertBitmapToBase64(bitmap);

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("convertedImage", convertedImage);
                    editor.apply();

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
                    file_doc.delete();

                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");


                    File root = android.os.Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + "/path");
                    dir.mkdirs();
                    File file1 = new File(dir, ".storage.jpg");
                    file_doc=createImageFile();
                    Reader pr;
                    String line = "";
                    try {
                        pr = new FileReader(file1);
                        int data1 = pr.read();
                        while (data1 != -1) {
                            line += (char) data1;
                            data1 = pr.read();
                        }
                        pr.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }







                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                file_doc=new File(picturePath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                convertedImage = convertImgPathToBase64(picturePath);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mydata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("convertedImage", convertedImage);
                editor.apply();
                iv_documemt.setImageBitmap(thumbnail);
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    private String convertBitmapToBase64(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
    public String convertImgPathToBase64(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, RequestPermissionCode);
//                String[]{READ_PHONE_STATE}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
//        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
