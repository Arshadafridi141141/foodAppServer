package com.example.admin_server.ViewHolder;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_server.Current.Current;
import com.example.admin_server.InterFace.ItemClickListener;
import com.example.admin_server.R;
import com.squareup.picasso.Picasso;

public class MenuViewHolder extends RecyclerView.ViewHolder implements ItemClickListener, View.OnCreateContextMenuListener, View.OnClickListener {
    TextView menuname;
    ImageView menuimage;
    ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        menuimage=itemView.findViewById(R.id.admin_menu_image);
        menuname=itemView.findViewById(R.id.admin_menu_name);
        itemView.setOnClickListener(this);
    }
    public void setOnClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    public void setdetails(Context ctx, String image, String name){

        menuname.setText(name);
        // Picasso.get().load(image).into(myimage);
        Picasso.get()
                .load(image).into(menuimage);
        //Log.e("checking","image location ="+image);
        // Picasso.get().load(image).into(myimage);
        // Picasso.get().load(image).into(myimage);
        //Picasso.get().load(image).into(myimage);



    }

    @Override
    public void onClick(View view, int position, Boolean isLongClick) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(), Current.UPDATE);
        menu.add(0,1,getAdapterPosition(), Current.DELETE);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
