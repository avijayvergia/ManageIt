package com.example.aksha.manageit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    public String username;
    private FirebaseDatabase orderDatabase;
    DatabaseReference orderRef;
    MyOrderRecyclerViewAdapter orderAdapter;
    private Button addButton;
    private EditText org_name, ord_quantity;
    public static final String TAG = "OrderFragment";
    List<Order> items;
    RecyclerView recyclerView;

    public OrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrderFragment newInstance(int columnCount, String username) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        fragment.username = username;
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        items = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        addButton = (Button) view.findViewById(R.id.addOrder);
        org_name = (EditText) view.findViewById(R.id.org_name);
        ord_quantity = (EditText) view.findViewById(R.id.ord_quantity);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order item = new Order(ord_quantity.getText().toString(), org_name.getText().toString());
                orderRef.push().setValue(item);
            }
        });

        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        orderAdapter = new MyOrderRecyclerViewAdapter(items, mListener);
        recyclerView.setAdapter(orderAdapter);
        setFirebase();
        return view;
    }

    private void setFirebase() {
        orderDatabase = FirebaseDatabase.getInstance();
        orderRef = orderDatabase.getReference().child("orders");
        orderRef.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    Order ordersSet = dataSnapshot.getValue(Order.class);
                    Log.d(TAG, "onChildAdded: q=" + ordersSet.getQuantity());
                    Log.d(TAG, "onChildAdded: name=" + ordersSet.getOrg_name());
                    items.add(ordersSet);
                    recyclerView.scrollToPosition(items.size() - 1);
                    orderAdapter.notifyItemInserted(items.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Order item);
    }
}
