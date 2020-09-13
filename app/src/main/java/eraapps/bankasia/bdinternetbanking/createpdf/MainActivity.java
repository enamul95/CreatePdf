package eraapps.bankasia.bdinternetbanking.createpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnCreatePdf;

    TextView tv_title;
    TextView tv_sub_title;
    TextView tv_location;
    TextView tv_city;


    String file_name_path= "";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {

            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }

        File file = new File(Environment.getExternalStorageDirectory(), "pdfsdcard_location");
        if (!file.exists()) {
            file.mkdir();
        }

        btnCreatePdf = findViewById(R.id.btnCreatePdf);
        tv_title = findViewById(R.id.tv_title);
        tv_sub_title = findViewById(R.id.tv_sub_title);
        tv_location = findViewById(R.id.tv_location);
        tv_city = findViewById(R.id.tv_city);


        btnCreatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createpdf();
            }
        });

    }


    public void createpdf() {
        Rect bounds = new Rect();
        int pageWidth = 300;
        int pageheight = 470;
        int pathHeight = 2;

        final String fileName = "mypdf";
        file_name_path = "/visacard_receit/" + fileName + ".pdf";
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        Path path = new Path();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageheight, 1).create();
        PdfDocument.Page documentPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = documentPage.getCanvas();
        int y = 25; // x = 10,
        //int x = (canvas.getWidth() / 2);
        int x = 10;

        paint.getTextBounds(tv_title.getText().toString(), 0, tv_title.getText().toString().length(), bounds);
        x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        canvas.drawText(tv_title.getText().toString(), x, y, paint);

        paint.getTextBounds(tv_sub_title.getText().toString(), 0, tv_sub_title.getText().toString().length(), bounds);
        x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        y += paint.descent() - paint.ascent();
        canvas.drawText(tv_sub_title.getText().toString(), x, y, paint);

        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

        //horizontal line
        path.lineTo(pageWidth, pathHeight);
        paint2.setColor(Color.GRAY);
        paint2.setStyle(Paint.Style.STROKE);
        path.moveTo(x, y);

        canvas.drawLine(0, y, pageWidth, y, paint2);

        //blank space
        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

        y += paint.descent() - paint.ascent();
        x = 10;
        canvas.drawText(tv_location.getText().toString(), x, y, paint);

        y += paint.descent() - paint.ascent();
        x = 10;
        canvas.drawText(tv_city.getText().toString(), x, y, paint);

        //blank space
        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

        //horizontal line
        path.lineTo(pageWidth, pathHeight);
        paint2.setColor(Color.GRAY);
        paint2.setStyle(Paint.Style.STROKE);
        path.moveTo(x, y);
        canvas.drawLine(0, y, pageWidth, y, paint2);

        //blank space
        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.logo);
        Bitmap b =  (Bitmap.createScaledBitmap(bitmap, 100, 50, false));
        canvas.drawBitmap(b, x, y, paint);
        y+= 25;
        canvas.drawText(getString(R.string.app_name), 120,y, paint);



        myPdfDocument.finishPage(documentPage);

        File file = new File(Environment.getExternalStorageDirectory() + file_name_path);
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();
        viewPdfFile();
    }

    public void viewPdfFile() {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + file_name_path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}