package com.example.carrenting.FragmentPages.Customer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.Adapter.NotificationAdapter;
import com.example.carrenting.Model.Activity;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobsandgeeks.saripaar.adapter.TextViewStringAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.collections.ArrayDeque;


public class CustomerNotificationFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    ArrayList<Activity> notifications;
    FirebaseFirestore dtb_noti;
    ProgressDialog progressDialog;
    String current_user_id;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.frame_layout_noti);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        storageReference = FirebaseStorage.getInstance().getReference();
        dtb_noti = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        notifications = new ArrayList<Activity>();
        notificationAdapter = new NotificationAdapter(CustomerNotificationFragment.this,notifications);
        recyclerView.setAdapter(notificationAdapter);

        EventChangeListener();
        return view;
    }

    private void EventChangeListener()
    {

        dtb_noti.collection("Notification")
                .whereEqualTo("customer_id", current_user_id)
//                .whereNotEqualTo("status","Dang cho")
//                .whereEqualTo("status","Xac nhan")
//                .whereEqualTo("status","Khong xac nhan")
                .whereIn("status", Arrays.asList("Xac nhan","Khong xac nhan"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Activity temp = new Activity();
                                temp.setNoti_id(document.get("noti_id").toString());
                                temp.setProvider_id(document.get("provider_id").toString());
                                temp.setCustomer_id(document.get("customer_id").toString());
                                temp.setStatus(document.get("status").toString());
                                temp.setVehicle_id(document.get("vehicle_id").toString());
                                notifications.add(temp);
                                notificationAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getContext(), "Không thể lấy thông tin đơn hàng ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}