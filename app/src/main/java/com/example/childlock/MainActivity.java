package com.example.childlock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn,btn2,btn3;

    String pass;

    static final String KEY = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this line will retrieve the password if we have save

        pass = SharedPrefUtil.getInstance(this).getString(KEY);
        final Context con = this;


        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (isAccessGranted()) {
                        if (!pass.isEmpty()){
                            startActivity(new Intent(MainActivity.this, showAll_InstalledApps.class));
                        }
                        else{
                            Toast.makeText(con,"set password first",Toast.LENGTH_LONG).show();

                        }

                    }
                    else {
                        Toast.makeText(MainActivity.this,"sorry first app usage permission",Toast.LENGTH_LONG).show();

                    }
                }
            }



        });

        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });

        btn3 = findViewById(R.id.pass);
        if (pass.isEmpty()){
            btn3.setText("set password");

        }
        else{
            btn3.setText("Update Password");
        }
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pass.isEmpty()){
                    setPassword( con);

                }
                else{
                    UpdatePassword( con);

                }

            }


        });


    }

    private void setPassword(Context con){

        AlertDialog.Builder dialog = new AlertDialog.Builder(con);
        LinearLayout ll = new LinearLayout(con);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView t1 = new TextView(con);
        t1.setText(("Enter your password"));

        ll.addView(t1);

        EditText input = new EditText(con);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ll.addView((input));
        dialog.setView(ll);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPrefUtil.getInstance(con).putString(KEY, input.getText().toString());
                Toast.makeText(con, "password set successfully",Toast.LENGTH_LONG).show();
                pass = input.getText().toString();
                btn3.setText("Update Password");



                // save password

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }
    private void UpdatePassword( final Context con){

        AlertDialog.Builder dialog = new AlertDialog.Builder(con);
        LinearLayout ll = new LinearLayout(con);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView t1 = new TextView(con);
        t1.setText(("Enter previous old password"));

        ll.addView(t1);

        EditText input = new EditText(con);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ll.addView((input));


        TextView t2 = new TextView(con);
        t2.setText(("Enter new password"));

        ll.addView(t2);

        EditText input2 = new EditText(con);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ll.addView((input2));


        dialog.setView(ll);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (pass.equals(input.getText().toString())){

                    SharedPrefUtil.getInstance(con).putString(KEY, input2.getText().toString());
                    Toast.makeText(con, "password update successfully",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(con, "sorry old password is incorrect",Toast.LENGTH_LONG).show();
                }

                // save password

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            }
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    }


