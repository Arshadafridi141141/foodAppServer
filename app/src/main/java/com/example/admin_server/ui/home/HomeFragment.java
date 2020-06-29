package com.example.admin_server.ui.home;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_server.Current.Current;
import com.example.admin_server.Home;
import com.example.admin_server.InterFace.ItemClickListener;
import com.example.admin_server.Model.Category;
import com.example.admin_server.Model.Food;
import com.example.admin_server.R;
import com.example.admin_server.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    EditText product_name,price, description,Id;
    Button upload,select;
    FirebaseDatabase database;
    DatabaseReference catogries;
    Food food;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveuri;
    public final int Pick_image=71;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    RecyclerView recyclerView_menu;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        database=FirebaseDatabase.getInstance();
        catogries=database.getReference("Food_details");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        recyclerView_menu=(RecyclerView) root.findViewById(R.id.recycler_menu);
        recyclerView_menu.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_menu.setHasFixedSize(true);
        loadMenu();

        return root;
    }


    private void loadMenu() {
        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,
                R.layout.menu_item,
                MenuViewHolder.class,
                catogries

        ) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                menuViewHolder.setdetails(getContext(),category.getImage(),category.getName());
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, Boolean isLongClick) {

                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView_menu.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Current.UPDATE)){
            ShowUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }
        else if(item.getTitle().equals(Current.DELETE)){
            Deletecat(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    private void Deletecat(String key) {
        catogries.child(key).removeValue();
        Toast.makeText(getContext(),"Item Deleted",Toast.LENGTH_SHORT).show();

    }

    private void ShowUpdateDialog(final String key, final Food item) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Update Item");
        builder.setMessage("Enter Information ");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_menu_layout,null);
        product_name=add_menu_layout.findViewById(R.id.Update_name);
        price=add_menu_layout.findViewById(R.id.Update_price);
        description=add_menu_layout.findViewById(R.id.Update_Description);
        Id=add_menu_layout.findViewById(R.id.update_ID);

        upload=add_menu_layout.findViewById(R.id.btnupload);
        select=add_menu_layout.findViewById(R.id.btnselect);
       product_name.setText(item.getName());
       price.setText(item.getPrice());
       description.setText(item.getDescription());
       Id.setText(item.getMenuId());




        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();


            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImage(item);
            }
        });


        builder.setView(add_menu_layout);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(product_name.getText().toString());
                catogries.child(key).setValue(item);


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

    private void ChooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Pick_image);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_image && resultCode==RESULT_OK &&  data!=null && data.getData()!=null){
            saveuri=data.getData();
            select.setText("Image selected ");

        }
    }

    private void ChangeImage(final Food item) {
        if(saveuri!=null){
            final ProgressDialog Dialog=new ProgressDialog(getContext());
            Dialog.setMessage("Uploading...");
            Dialog.show();
            String Imagename= UUID.randomUUID().toString();
            final StorageReference Imagefolder= storageReference.child("images/"+Imagename);

            Imagefolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Dialog.dismiss();
                    Toast.makeText(getContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                    Imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            food=new Food(description.getText().toString(),uri.toString(),Id.getText().toString(),product_name.getText().toString(),price.getText().toString());
                             item.setImage(uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Dialog.dismiss();
                    Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
