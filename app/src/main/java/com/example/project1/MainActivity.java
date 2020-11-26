package com.example.project1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT="item_text";
    public static final String KEY_ITEM_POSITION="item_position";
    public static final int EDIT_TEXT_CODE=20;


    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd= findViewById(R.id.btnAdd);
        etItem= findViewById(R.id.etItem);
        rvItems= findViewById(R.id.rvItems);


        loadItems();


        ItemsAdapter.OnLongClickListener onLongClickListener= new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //delete item
                items.remove(position);
                //what position deleted from
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdapter.OnClickListener onClickListener= new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("Main Activity","Single click at position"+position);
                //create new activity
                Intent i = new Intent(MainActivity.this,EditActivity.class);

                // pass relevant data to be edited
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);
                //display the data
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };
        itemsAdapter =new ItemsAdapter(items,onLongClickListener,onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem= etItem.getText().toString();
                // add item to the model
                items.add(todoItem);
                //Notify adapter that an item has been inserted

                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");

                Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();

                saveItems();


            }
        });


    }
//after edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK && requestCode==EDIT_TEXT_CODE){
            //retrieve updated value
            String itemText=data.getStringExtra(KEY_ITEM_TEXT);



            //extract original postion of the edited item from the position key
            int position=data.getExtras().getInt(KEY_ITEM_POSITION);
            //update model at the retrieved position
            items.set(position,itemText);
            //notify adapter
            itemsAdapter.notifyItemChanged(position);

            //persist changes
            saveItems();
            Toast.makeText(getApplicationContext(),"item updated successfully!",Toast.LENGTH_SHORT).show();

        }else{
            Log.w("MainActivity","Unknown call to Activity result");

        }

    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");

    }

    // this function saves items byb writing them into the data file
    private void loadItems(){
        try{
            items=new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch(IOException e){
            e.printStackTrace();
            Log.e("MainActivity","Error reading Items",e);
            items=new ArrayList<>();


        }
    }
    // this function saves items byb writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing Items", e);
        }
    }

    }