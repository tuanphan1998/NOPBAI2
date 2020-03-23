package com.example.bai002_timhieu_sqllite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private EditText Ed1;
    private  EditText Ed2;
    private EditText Ed3;
    private Button tbn1;
    private Button tbn2;
    private ListView LvUser;
    private ArrayAdapter<User> adapter;
    private ArrayList<User> Userlist = new ArrayList<>();
    int idUpdate = -1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        Ed1 = findViewById(R.id.Ed1);
        Ed2 = findViewById(R.id.Ed2);
        Ed3 = findViewById(R.id.Ed3);
        tbn2 = findViewById(R.id.tbn2);
        tbn1 = findViewById(R.id.tbn1);
        tbn2.setEnabled(false);



        // tbn event
        tbn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(idUpdate < 0)
               {
                   InsertTest();

               }
               else
               {
                   updaterow();
                    idUpdate = -1;
               }
                LoadData();
            }
        });
        tbn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DeleteDataUser2();
                    LoadData();
                }
                catch (Exception exx)
                {
                    Toast.makeText(MainActivity.this, ""+exx, Toast.LENGTH_SHORT).show();
                }
            }
        });


        // adapter
        LvUser = findViewById(R.id.LvUser);
        adapter = new ArrayAdapter<User>(this , 0 , Userlist){
            @NonNull
            @Override
            public View getView(int position,  View convertView,  ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView =  inflater.inflate(R.layout.data_item , null);
                TextView txt1 = convertView.findViewById(R.id.txt1);
                TextView txt2 = convertView.findViewById(R.id.txt2);
                TextView txt3 = convertView.findViewById(R.id.txt3);
                User u = Userlist.get(position);
                txt1.setText(u.getName());
                txt2.setText(u.getTuoi());
                txt3.setText(u.getDiem());
                return convertView;
            }
        };
        LvUser.setAdapter(adapter);
        LvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteDataUser(position);
                LoadData();
                return false;
            }
        });
        LvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInfotenedit(position);
            }
        });

        LoadData();

    }



    private  void  showInfotenedit(int position)
    {
      try
      {
          User u = Userlist.get(position);
          Ed1.setText(u.getName());
          Ed2.setText(u.getTuoi());
          Ed3.setText(u.getDiem());
          idUpdate = u.getId();
      }
      catch (Exception e)
      {
          Log.e("loi",e.toString());
      }
    }
    private void updaterow()
    {
        try
        {
            String name = Ed1.getText().toString();
            String tuoi = Ed2.getText().toString();
            String diem = Ed3.getText().toString();
            String sql = "UPDATE tbchuno SET name =  '"+name+"' , tuoi = '"+tuoi+"' , diem = '"+diem+"' WHERE id = " + idUpdate;
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.e("eroor",e.toString());
        }
    }

    private void  initData()
    {
       try
       {
           db = openOrCreateDatabase("KiemTra2.db",MODE_PRIVATE,null);
           String sql3 = "CREATE TABLE IF NOT EXISTS tbchuno (id integer primary key autoincrement, name text ,  tuoi text , diem text )";
           db.execSQL(sql3);
       }
       catch (Exception e)
       {
           Log.e("error",e.toString());
       }
    }

    private void InsertTest()
    {
        String name = Ed1.getText().toString();
        String tuoi = Ed2.getText().toString();
        String diem = Ed3.getText().toString();
        String sql = " INSERT INTO tbchuno (name,tuoi,diem) VALUES ('"+name+"' , '"+tuoi+"','"+diem+"') ";
        db.execSQL(sql);
    }

    private  void DeleteDataUser(int position)
    {
        int id = Userlist.get(position).getId();
        String sql = "DELETE FROM tbchuno WHERE id = " + id;
        db.execSQL(sql);
    }

    private  void DeleteDataUser2()
    {
        String sql2 = "DELETE FROM tbchuno";
        db.execSQL(sql2);
    }

    private void LoadData()
    {
        Userlist.clear();
        String sql2 = "SELECT * FROM tbchuno";
        Cursor cursor = db.rawQuery(sql2 , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String tuoi = cursor.getString(2);
            String diem = cursor.getString(3);
            User u = new User();
            u.setId(id);
            u.setName(name);
            u.setTuoi(tuoi);
            u.setDiem(diem);
            Userlist.add(u);
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
    }

}
