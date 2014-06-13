package com.internship.remindersfacebookapp.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity{
private LoginFragment loginFragment;
	/*
	 * Start of application lifecycle methods
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			loginFragment= new LoginFragment();
			getSupportFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, loginFragment)
					.commit();
		} else {
			// Or set the fragment from restored state info
			loginFragment= (LoginFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}
	/*
	 * End of application lifecycle methods
	 */

}