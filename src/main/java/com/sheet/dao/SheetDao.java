package com.sheet.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sheet.controller.SpreadSheet;

public class SheetDao {
	private static FirebaseApp mfirebaseApp = null;
	
	//Initialise the firebase database
	public static FirebaseApp  initializeFireBase(){
		if( mfirebaseApp != null)
		{
			return mfirebaseApp; 
		}
		FirebaseOptions mOptions=null;
		try {
			mOptions = new FirebaseOptions.Builder().setDatabaseUrl("https://sheet-f131b.firebaseio.com/").setServiceAccount(new FileInputStream("/home/bridgeit/sheet-35461dfea2bd.json")).build();
			mfirebaseApp = FirebaseApp.initializeApp(mOptions);
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return mfirebaseApp;
	}
	public static void saveDataToFireBase(String pKey, Map<String,String> pMap)
	
	{
		FirebaseApp firebaseApp = initializeFireBase();
		// As an admin, the app has access to read and write all data, regardless of Security Rules
		
		DatabaseReference lRef = FirebaseDatabase.getInstance( firebaseApp ).getReference("Engineer");
		DatabaseReference lUsersRef = lRef.child(pKey);
		lUsersRef.setValue(pMap);
		
		lUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot pDataSnapshot) {
		        Object document = pDataSnapshot.getValue();
		        System.out.println(document);
		    }
			@Override
			public void onCancelled(DatabaseError arg0) {
				System.err.println("Listener was cancelled");
				
			}
		});
		
	}
	
	public static Map<String, String> fetchDataFromFireBase(String pKey)
	{
		initializeFireBase();
		// As an admin, the app has access to read and write all data, regardless of Security Rules
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Engineer");
		DatabaseReference usersRef = ref.child(pKey);
		
		
		ValueEventListener postListener = new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot pDataSnapshot) {
		    	for (DataSnapshot child : pDataSnapshot.getChildren()) {
	                 for (DataSnapshot childd : child.getChildren()) {
	                	 System.out.println(childd
	                		);
	                    }
	                }
		    }
		    @Override
		    public void onCancelled(DatabaseError pDatabaseError) {
		        System.out.println("The read failed: " + pDatabaseError.getCode());
		    }
		};
		ref.addValueEventListener(postListener);
		
		return null;
		
	}
	
	 
	
}
