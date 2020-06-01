package com.example.admin_server.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_server.InterFace.ItemClickListener;
import com.example.admin_server.Model.Category;
import com.example.admin_server.R;
import com.example.admin_server.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference category;

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
        category=database.getReference("Food_details");

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
                category

        ) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                menuViewHolder.setdetails(getContext(),category.getImage(),category.getName());
                menuViewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, Boolean isLongClick) {

                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        recyclerView_menu.setAdapter(adapter);

    }
}
