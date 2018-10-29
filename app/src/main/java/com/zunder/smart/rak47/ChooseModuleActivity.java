package com.zunder.smart.rak47;

import java.util.HashMap;
import java.util.Locale;

import com.zunder.smart.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ChooseModuleActivity extends Activity 
{	
	private Spinner _chooseModuleSpinner;
	private Button _chooseModuleButton;
	private final String[] module_select={"RAK473","RAK475","RAK476","RAK477"};
	private ArrayAdapter<String> module_adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvtivity_choose_module);
        
        _chooseModuleSpinner=(Spinner)findViewById(R.id.choosemodule_spinner);
        module_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,module_select);  	              
	    module_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  	              
	    _chooseModuleSpinner.setAdapter(module_adapter);
	    
        _chooseModuleButton=(Button)findViewById(R.id.choosemodule_button);
        _chooseModuleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = getSharedPreferences("module_type", MODE_PRIVATE).edit();
			    editor.putString("type", _chooseModuleSpinner.getSelectedItem().toString());
			    editor.commit();
			    Toast.makeText(ChooseModuleActivity.this, "You have choosed: "+_chooseModuleSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
				Intent intent=new Intent();
				intent.setClass(ChooseModuleActivity.this, MainTabActivity.class);
				startActivity(intent);
			}
		});
	}

}
