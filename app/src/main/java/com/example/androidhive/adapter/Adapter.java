package com.example.androidhive.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidhive.model.Data;
import com.example.androidhive.R;
import com.example.androidhive.MainActivity;

import java.util.List;

public class Adapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> lists;

    public Adapter(Activity activity, List<Data> lists) {
        this.activity = activity;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null && inflater != null) {
            view = inflater.inflate(R.layout.activity_list_row, null);
        }
        if (view != null) {
            TextView name = view.findViewById(R.id.text_name);
            TextView nim = view.findViewById(R.id.text_nim);
            TextView email = view.findViewById(R.id.text_email);
            TextView phone = view.findViewById(R.id.text_phone);
            Button btnEdit = view.findViewById(R.id.btn_edit);
            Button btnDelete = view.findViewById(R.id.btn_delete);

            Data data = lists.get(i);
            name.setText(data.getName());
            nim.setText(data.getNim());
            email.setText(data.getEmail());
            phone.setText(data.getPhone());

            btnEdit.setOnClickListener(v -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).showEditDialog(data);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).deleteData(data.getId());
                }
            });
        }
        return view;
    }
}
