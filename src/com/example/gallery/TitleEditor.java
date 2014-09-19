package com.example.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TitleEditor extends Activity {

    private EditText mText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.title_editor);
        mText = (EditText) this.findViewById(R.id.title);
        mText.setText(getIntent().getExtras().getString("title"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        
    }

    public void onClickOk(View v) {
        Intent mIntent=new Intent(this, MainActivity.class);
        Bundle b=new Bundle();
        b.putString("newtitle", mText.getText().toString());
        mIntent.putExtras(b);
        setResult(RESULT_OK,mIntent);
        finish();
    }
}
