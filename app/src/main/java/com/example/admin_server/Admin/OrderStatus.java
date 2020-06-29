package com.example.admin_server.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.admin_server.Current.Current;
import com.example.admin_server.Model.Request;
import com.example.admin_server.R;
import com.example.admin_server.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;


public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;
    MaterialSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        db=FirebaseDatabase.getInstance();
        requests=db.getReference("Requests");
        recyclerView= findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LoadOrders();
    }


    //ctrl+o


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Current.UPDATE))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        else if(item.getTitle().equals(Current.DELETE))
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);

    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();

    }

    @SuppressLint("InflateParams")
    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertdialog=new AlertDialog.Builder(OrderStatus.this);
        alertdialog.setTitle("update Order");
        alertdialog.setMessage("please choose status");
        LayoutInflater inflater=this.getLayoutInflater();
        final View view;
        view = inflater.inflate(R.layout.update_order,null);
        spinner= view.findViewById(R.id.status_spinner);
        spinner.setItems("Placed","on way","shipped");
        alertdialog.setView(view);
        final String localkey=key;
        alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           dialog.dismiss();
           item.setStatus(String.valueOf(spinner.getSelectedIndex()));
           requests.child(localkey).setValue(item);

            }
        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertdialog.show();

    }

    private void LoadOrders() {
                adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {
                Log.e("checking","not showing="+request.getPhone());
                orderViewHolder.orderid.setText(adapter.getRef(i).getKey());
                orderViewHolder.orderstatus.setText(convercodetostatus(request.getStatus()));
                orderViewHolder.orderaddress.setText(request.getAddress());
                orderViewHolder.orderphone.setText(request.getPhone());
            }
        };
        //where is the issue?
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
    public String convercodetostatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On way";
        else
            return "Shipped";

    }
}
