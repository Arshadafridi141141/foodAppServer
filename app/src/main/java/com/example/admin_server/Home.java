package com.example.admin_server;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin_server.Admin.OrderStatus;
import com.example.admin_server.InterFace.ListerOrder;
import com.example.admin_server.Model.Category;
import com.example.admin_server.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

public class Home extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference catogries;
    FirebaseStorage storage;
    StorageReference storageReference;
    EditText product_name,price,description,id;
    Button select,upload;
    Food food;
    Uri saveuri;
    public final int Pick_image=71;

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database=FirebaseDatabase.getInstance();
        catogries=database.getReference("Food_details");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent service=new Intent(getBaseContext(), ListerOrder.class);
        startService(service);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();

            }
        });
       final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.Log_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home://menu
                        Toast.makeText(getApplicationContext(), "Menu is selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_gallery://cart
                        Toast.makeText(getApplicationContext(), "cart is selected", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.nav_slideshow://orders
                        Toast.makeText(getBaseContext(),"order status is selected",Toast.LENGTH_SHORT).show();
                        Intent Order=new Intent(Home.this, OrderStatus.class);
                        startActivity(Order);
                        break;
                    case R.id.Log_out://logout
                        Toast.makeText(getApplicationContext(), "Logout is is selected", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Home.this, MainActivity.class);
                        startActivity(intent);

                        break;
                }
                drawer.closeDrawers();

                return false;
            }
        });
    }

    private void ShowDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
        builder.setTitle("new Item");
        builder.setMessage("Please Enter Information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_food_layout,null);
        product_name=add_menu_layout.findViewById(R.id.add_name);
        description=add_menu_layout.findViewById(R.id.add_product_Description);
        price=add_menu_layout.findViewById(R.id.add_product_price);
        id=add_menu_layout.findViewById(R.id.add_product_ID);

        upload=add_menu_layout.findViewById(R.id.btnupload);
        select=add_menu_layout.findViewById(R.id.btnselect);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();


            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });


        builder.setView(add_menu_layout);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(food !=null){
                    catogries.push().setValue(food);

                }


            }


        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }


    private void UploadImage() {
        if(saveuri!=null){
            final ProgressDialog Dialog=new ProgressDialog(this);
            Dialog.setMessage("Uploading...");
            Dialog.show();
            String Imagename= UUID.randomUUID().toString();
            final StorageReference Imagefolder= storageReference.child("images/"+Imagename);
            Imagefolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Dialog.dismiss();
                    Toast.makeText(Home.this,"Uploaded",Toast.LENGTH_SHORT).show();
                    Imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            food =new Food(description.getText().toString(),uri.toString(),id.getText().toString(),product_name.getText().toString(),price.getText().toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Dialog.dismiss();
                    Toast.makeText(Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void ChooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Pick_image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_image && resultCode==RESULT_OK &&  data!=null && data.getData()!=null){
            saveuri=data.getData();
            select.setText("Image selected ");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
