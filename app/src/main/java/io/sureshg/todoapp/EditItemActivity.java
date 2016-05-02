package io.sureshg.todoapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {

    private EditText edText;

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        setActionBar();

        edText = (EditText) findViewById(R.id.editText);
        pos = getIntent().getIntExtra("pos",0);
        String text = getIntent().getStringExtra("text");
        edText.setText(text);
    }


    /**
     * Set action bar properties.
     */
    private void setActionBar() {
        ActionBar aBar =  getSupportActionBar();
        if (aBar != null) {
            aBar.setTitle("Edit Item");
        }
    }

    /**
     * On save callback listener.
     * @param view save button.
     */
    public void onSave(View view) {

        String txt  = edText.getText().toString();
        if(!txt.isEmpty()) {
            Intent data = new Intent();
            data.putExtra("text", txt );
            data.putExtra("pos",pos);
            setResult(RESULT_OK,data);
            finish();
        }else {
            Toast.makeText(EditItemActivity.this, "Item text can't be empty!", Toast.LENGTH_SHORT).show();
        }

    }
}
