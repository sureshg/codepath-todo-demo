package io.sureshg.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * ToDo main activity class.
 *
 * @author Suresh
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private ArrayList<String> todoItems;
    private ArrayAdapter<String> todoAdapter;
    private ListView lvItems;
    private EditText etItemText;
    private File todoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBar();
        // Files used to persist items.
        todoFile = new File(getFilesDir(), "todo.txt");
        readItems();
        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);

        etItemText = (EditText) findViewById(R.id.etItemText);

        // List view items
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(todoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item = todoAdapter.getItem(position);
                todoAdapter.remove(item);
                Toast.makeText(MainActivity.this, "Removed ToDo item: " + item, Toast.LENGTH_SHORT).show();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("pos", position);
                i.putExtra("text", todoAdapter.getItem(position));
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String iText = data.getStringExtra("text");
            int pos = data.getIntExtra("pos", 0);
            todoItems.set(pos,iText);
            todoAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    /**
     * Set action bar properties.
     */
    private void setActionBar() {
        ActionBar aBar = getSupportActionBar();
        if (aBar != null) {
            aBar.setTitle("Sample Todo");
        }
    }

    /**
     * Initialize todo items by reading the files.
     */
    private void readItems() {
        try {
            todoItems = new ArrayList<>(FileUtils.readLines(todoFile, Charset.defaultCharset()));
        } catch (IOException e) {
            todoItems = new ArrayList<>();
        }
    }

    /**
     * Persists todo items to file.
     */
    private void writeItems() {
        try {
            FileUtils.writeLines(todoFile, todoItems);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Can't persist ToDo items.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Add item onClick listener
     *
     * @param view add item button view.
     */
    public void onAddItem(View view) {
        String iText = etItemText.getText().toString();
        if (!iText.isEmpty()) {
            todoAdapter.add(iText);
            Toast.makeText(MainActivity.this, "Added ToDo item: " + iText, Toast.LENGTH_SHORT).show();
            writeItems();
            etItemText.setText("");

        } else {
            Toast.makeText(MainActivity.this, "ToDo item can't be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
