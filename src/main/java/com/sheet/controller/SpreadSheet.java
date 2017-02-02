package com.sheet.controller;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.sheet.dao.SheetDao;


public class SpreadSheet {
	
    /** Application name. */
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File( System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
   /* private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);*/
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws Exception 
     */
    public static Credential authorize() throws Exception {
        // Load client secrets.
        //InputStream in = Quickstart.class.getResourceAsStream("/client_secret.json");
        InputStream in = new FileInputStream("/home/bridgeit/client_secret.json");
        
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws Exception 
     */
    public static Sheets getSheetsService() throws Exception {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

	public static  String mKey;
	public static  String mURL;
                  
    public void getAndStore()
    {
    	try {
			getSheetData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws Exception {
    	getSheetData();
    }
    
    private static void getSheetData() throws Exception {
    	int lId_Col=0;
        // Build a new authorized API client service.
        Sheets service = getSheetsService();
        // Prints the names  of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        String lSpreadsheetId = "1CZPzCBwp3-SviyvmTvgsIcLotvAzuG_H5rFYzQ3aQFE";
        /*String range = "MyTestSheet!A1:AZ2000";*/
        String lRange = "Sheet1!A1:AZ2000";
       
        ValueRange response = service.spreadsheets().values().get(lSpreadsheetId, lRange).execute();
        List<List<Object>> lRows = response.getValues();
    	//System.out.println(rows.size());
        if ( lRows != null && lRows.size() > 0 ) {
        	//getting frst row
        	List<Object> lRowHeader =  lRows.get(0);
        	
        	for (int lRowNo = 1;  lRowNo < lRows.size(); lRowNo++) {
        		//System.out.println("----");
        		
        		//it returns particular row
        		List<Object> lRow =  lRows.get(lRowNo);
        		/*System.out.println(row.size());*/
        		
        		Map<String, String> map = new HashMap<String, String>();
        		//it returns column of that particular row
        		for (int lColNo = 0; lColNo < lRow.size(); lColNo++) {
        			map.put((String)lRowHeader.get(lColNo), (String)lRow.get( lColNo ));
        			/*System.out.println((String)row.get(colNo));*/
        			
        		}
        		
        		mURL=map.get("FaceBookURL");
        		System.out.println(mURL);
        		mKey=map.get(  lRowHeader.get(lId_Col) );
        		byte[] lImage = null;
        		Document doc = Jsoup.connect(mURL). ignoreHttpErrors(true).userAgent("Mozilla/5.0").timeout(5000).get();
            	
            	// fb profile url fetching
            	
            	 Element lLinks = doc.getElementsByClass("photoContainer").first();
            	 Element lImgTags = lLinks.getElementsByTag("img").get(0);
         	     String src = lImgTags.attr("src");
         	    System.out.println(src);
        		lImage = IOUtils.toByteArray(new URL(src));
        		String lImageFile = Base64.getEncoder().encodeToString(lImage);
        		map.put("image", lImageFile);
        		map.remove("EnggId");
        		SheetDao.saveDataToFireBase( mKey , map );
        		/*Map<String,String> oldValue = SheetDao.fetchData( map.get(rowHeader.get(Id_Col)) );
        	//	System.out.println(oldValue);
        		if( oldValue == null || oldValue.isEmpty()){
        			SheetDao.saveData( map.get(  rowHeader.get(Id_Col) ) , map );
        			SheetDao.saveData( map );
        			return;
        		}
        		
        		String strNewTime = map.get("Timestamp");
        	//	System.out.println(map.get("Timestamp"));
        		String stroldTime = oldValue.get("Timestamp");
        	//System.out.println(oldValue.get("Timestamp"));
        		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy MM:hh:ss");
        		Date old = sdf.parse(stroldTime);
        		Date newd = sdf.parse(strNewTime);
        		
        		if( newd.after(old))
        		{
        			SheetDao.saveData( map.get(  rowHeader.get(Id_Col) ) , map );
        			SheetDao.saveData( map );
        		}*/
        		
        	}
        	SheetDao.fetchDataFromFireBase(mKey);
        	
        	
        	
        	
        	//display the images in jframe in spreadsheet
        	/*Image image = null;
            try {
                URL url = new URL(mURL);
                image = ImageIO.read(url);
            } catch (IOException e) {
            	e.printStackTrace();
            }

            JFrame frame = new JFrame();
            frame.setSize(300, 300);
            JLabel label = new JLabel(new ImageIcon(image));
            frame.add(label);
            frame.setVisible(true);
    		*/
        	
        }
    }
    
}
