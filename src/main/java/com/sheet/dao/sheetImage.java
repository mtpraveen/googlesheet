/*package com.sheet.dao;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.util.Base64;

package com.dao;

import java.io.ByteArrayOutputStream;

import com.firebase.client.Firebase;

public class sheetImage {
	
	 private Firebase mRef;
	 private StorageReference storageRef;
	
	 private void storeImageToFirebase() {
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
		    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoUri.getPath(), options);
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		    byte[] bytes = baos.toByteArray();
		    String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

		    // we finally have our base64 string version of the image, save it.
		    firebase.child("pic").setValue(base64Image);
		    System.out.println("Stored image with length: " + bytes.length);
		}
}*/