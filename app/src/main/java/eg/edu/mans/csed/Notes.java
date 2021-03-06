package eg.edu.mans.csed;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.KeyListener;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

public class Notes extends AppCompatActivity {
    private KeyListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        EditText editText = findViewById(R.id.editNotes);

        List<String> notes = AlarmMe.tsvToList("notes.txt");
        String contents = "";
        for(int i=0; i<notes.size();i++){
            contents = contents + notes.get(i) + '\n';
        }
        editText.setText(contents);

        // store editText key listener to recover it when edit is clicked
        listener = editText.getKeyListener();
        //Enable links click
        //editText.setMovementMethod(LinkMovementMethod.getInstance());
        // make links long clickable for copying link
        editText.setMovementMethod(BetterLinkMovementMethod.newInstance().setOnLinkLongClickListener(new BetterLinkMovementMethod.OnLinkLongClickListener() {
            @Override
            public boolean onLongClick(TextView textView, String url) {

                //copy to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", url);
                clipboard.setPrimaryClip(cData);

                // when long clicked, text is selected so this code is to deselect text
                textView.setSelected(false);

                Toast.makeText(getApplicationContext(),"Link copied to clipboard.", Toast.LENGTH_SHORT).show();
                return true;
            }

        }));
        //Disable edit
        editText.setKeyListener(null);

    }
    public void edit(View m){
        final EditText editText = findViewById(R.id.editNotes);
        //Disable links click
        editText.setMovementMethod(ArrowKeyMovementMethod.getInstance());

        //Enable edit by setting key to old listener
        editText.setKeyListener(listener);

        // put cursor at the end of text
        editText.setSelection(editText.getText().length()-1); //without -1 it goes to a new line and I don't care why

        //open soft keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void save(View m){
        //get notes text
        EditText editText = findViewById(R.id.editNotes);
        String notes = editText.getText().toString();

        //Save notes to file
        Loading.writeToFile(notes, this, "notes.txt");

        //Press the back button
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }
    public void cancel(View m){
        //Press the back button
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

}