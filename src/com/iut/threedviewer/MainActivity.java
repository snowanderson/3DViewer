package com.iut.threedviewer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	

	// Progress Dialog
	private ProgressDialog pDialog;
	public static final int progress_bar_type = 0;

	// File url to download
	private static String file_url = "http://gts.sourceforge.net/samples/x_wing.gts.gz";


	/**
	 * Showing Dialog
	 * */

	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case progress_bar_type: // we set this to 0
	        pDialog = new ProgressDialog(this);
	        pDialog.setMessage("Downloading file. Please wait...");
	        pDialog.setIndeterminate(false);
	        pDialog.setMax(100);
	        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        pDialog.setCancelable(true);
	        pDialog.show();
	        return pDialog;
	    default:
	        return null;
	    }
	}

	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

	    /**
	     * Before starting background thread Show Progress Bar Dialog
	     * */
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        showDialog(progress_bar_type);
	    }

	    /**
	     * Downloading file in background thread
	     * */
	    @Override
	    protected String doInBackground(String... f_url) {
	        int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();

	            // this will be useful so that you can show a tipical 0-100%
	            // progress bar
	            int lenghtOfFile = conection.getContentLength();

	            // download the file
	            InputStream input = new BufferedInputStream(url.openStream(),
	                    8192);

	            // Output stream
	            String f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "x_wing.gts.gz").toString();
	            OutputStream output = new FileOutputStream(f);

	            byte data[] = new byte[1024];

	            long total = 0;

	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

	                // writing data to file
	                output.write(data, 0, count);
	            }

	            // flushing output
	            output.flush();

	            // closing streams
	            output.close();
	            input.close();

	        } catch (Exception e) {
	            Log.e("Error: ", e.getMessage());
	        }

	        return null;
	    }

	    /**
	     * Updating progress bar
	     * */
	    protected void onProgressUpdate(String... progress) {
	        // setting progress percentage
	        pDialog.setProgress(Integer.parseInt(progress[0]));
	    }

	    /**
	     * After completing background task Dismiss the progress dialog
	     * **/
	    @Override
	    protected void onPostExecute(String file_url) {
	        // dismiss the dialog after the file was downloaded
	        dismissDialog(progress_bar_type);

	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		TextView myTextView = (TextView) findViewById(R.id.textView1);
		TextView debug = (TextView) findViewById(R.id.textView2);
		File filesDir = getFilesDir();
		String[] a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();
		String b = a.toString();
		String filedir=filesDir.toString();
		myTextView.setText(b);

		try { 
		       // catches IOException below
		       final String TESTSTRING = new String("Hello Android");
	
		       /* We have to use the openFileOutput()-method
		       * the ActivityContext provides, to
		       * protect your file from others and
		       * This is done for security-reasons.
		       * We chose MODE_WORLD_READABLE, because
		       *  we have nothing to hide in our file */             

		FileOutputStream fOut = openFileOutput("x_wing.gts",
		                                                            MODE_WORLD_READABLE);
		       OutputStreamWriter osw = new OutputStreamWriter(fOut); 

		       // Write the string to the file
		       osw.write(TESTSTRING);

		       /* ensure that everything is
		        * really written out and close */
		       osw.flush();
		       osw.close();
		} catch (Exception ioe) 
	      {ioe.printStackTrace();} 
		

			File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "x_wing.gts");
			File g = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "x_wing.gts.gz");
			if(!f.isFile()) {
		
				// télécharger le fichier
			    new DownloadFileFromURL().execute(file_url);
		              		        
				// décompresser le fichier
				if(g.isFile()) {

				     try{
					
					final String INPUT_GZIP_FILE = g.toString();
				    final String OUTPUT_FILE = f.toString();
				    
				    byte[] buffer = new byte[1024];
					 
				 
				    	 GZIPInputStream gzis = 
				    		new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE));
				 
				    	 FileOutputStream out = 
				            new FileOutputStream(OUTPUT_FILE);
				 
				        int len;
				        while ((len = gzis.read(buffer)) > 0) {
				        	out.write(buffer, 0, len);
				        }
				 
				        gzis.close();
				    	out.close();
				 
				    	debug.setText("décompression réussie");
				 
				    }catch(Exception ex){
				       ex.printStackTrace(); 
				       debug.setText("echec décompression");
				    }
				}
			    
			    	
			}

			
		      try {
			Scanner input = new Scanner(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "x_wing.gts"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//debug.setText("le fichier n'existe pas");
			}
		}
	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
