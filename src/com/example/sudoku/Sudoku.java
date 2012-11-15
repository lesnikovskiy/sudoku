package com.example.sudoku;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Sudoku extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        
        View continueButton = this.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        
        View newButton = this.findViewById(R.id.new_game_button);
        newButton.setOnClickListener(this);
        
        View aboutButton = this.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        
        View exitButton = this.findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_sudoku, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
	    	case R.id.settings:
	    		startActivity(new Intent(this, Settings.class));
	    		return true;
    	}
    	
    	return false;
    }
    
    public void onClick(View v) {
    	switch(v.getId()) {
	    	case R.id.about_button:
	    		Intent i = new Intent(this, About.class);
	    		startActivity(i);
	    		break;
    	}
    }
}