package com.example.androidhive;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidhive.adapter.Adapter;
import com.example.androidhive.helper.Helper;
import com.example.androidhive.model.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnDialogAdd;
    private ListView listView;
    private AlertDialog.Builder dialog;
    private List<Data> lists = new ArrayList<>();
    private Adapter adapter;
    private Helper db = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.list_item);
        adapter = new Adapter(this, lists);
        listView.setAdapter(adapter);

        btnDialogAdd = findViewById(R.id.btn_dialog_add);
        btnDialogAdd.setOnClickListener(v -> showAddDialog());

        // Load data into list
        getData();
    }

    private void showAddDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_note_diolog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText editname = dialog.findViewById(R.id.edit_name);
        EditText editnim = dialog.findViewById(R.id.edit_nim);
        EditText editemail = dialog.findViewById(R.id.edit_email);
        EditText editphone = dialog.findViewById(R.id.edit_phone);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnCreate = dialog.findViewById(R.id.btn_create);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnCreate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editname.getText()) || TextUtils.isEmpty(editnim.getText()) || TextUtils.isEmpty(editemail.getText()) || TextUtils.isEmpty(editphone.getText())) {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int nim = Integer.parseInt(editnim.getText().toString());
                    int phone = Integer.parseInt(editphone.getText().toString());
                    db.insert(editname.getText().toString(), nim, editemail.getText().toString(), phone);
                    dialog.dismiss();
                    getData(); // Refresh the list
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Nomor registrasi dan telepon harus berupa angka", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void showEditDialog(Data data) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_note_dialog_edit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText editname = dialog.findViewById(R.id.edit_name);
        EditText editnim = dialog.findViewById(R.id.edit_nim);
        EditText editemail = dialog.findViewById(R.id.edit_email);
        EditText editphone = dialog.findViewById(R.id.edit_phone);

        editname.setText(data.getName());
        editnim.setText(data.getNim());
        editemail.setText(data.getEmail());
        editphone.setText(data.getPhone());

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnUpdate = dialog.findViewById(R.id.btn_update);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editname.getText()) || TextUtils.isEmpty(editnim.getText()) || TextUtils.isEmpty(editemail.getText()) || TextUtils.isEmpty(editphone.getText())) {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int nim = Integer.parseInt(editnim.getText().toString());
                    int phone = Integer.parseInt(editphone.getText().toString());
                    db.update(Integer.parseInt(data.getId()), editname.getText().toString(), nim, editemail.getText().toString(), phone);
                    dialog.dismiss();
                    getData(); // Refresh the list
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Nomor registrasi dan telepon harus berupa angka", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void deleteData(String id) {
        db.delete(Integer.parseInt(id));
        getData(); // Refresh the list
    }

    private void getData() {
        lists.clear();
        ArrayList<HashMap<String, String>> rows = db.getAll();
        for (HashMap<String, String> row : rows) {
            String id = row.get("id");
            String name = row.get("name");
            String nim = row.get("nim");
            String email = row.get("email");
            String phone = row.get("phone");

            Data data = new Data();
            data.setId(id);
            data.setName(name);
            data.setNim(nim);
            data.setEmail(email);
            data.setPhone(phone);
            lists.add(data);
        }
        adapter.notifyDataSetChanged();
    }
}
