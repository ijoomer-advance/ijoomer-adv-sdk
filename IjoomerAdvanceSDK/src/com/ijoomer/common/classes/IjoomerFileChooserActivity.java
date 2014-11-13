package com.ijoomer.common.classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This Class Contains All Method Related To IjoomerFileChooserActivity.
 * 
 * @author tasol
 * 
 */
public class IjoomerFileChooserActivity extends IjoomerSuperMaster {

	private LinearLayout lnrCreateFolder;
	private ListView lstFileChooser;
	private IjoomerButton btnSaveOrOpen;
	private IjoomerEditText edtFilePath;
	private IjoomerButton btnCreate;
	private IjoomerEditText edtFolderName;
	private ImageView btnMakeFolder;

	protected File mDirectory;
	protected ArrayList<File> mFiles;
	protected FilePickerListAdapter mAdapter;

	protected String[] acceptedFileExtensions;
	private String finalFilePath = "";
	protected boolean mShowHiddenFiles = false;
	private boolean IN_ISOPENFILE = false;

	/**
	 * The file path
	 */
	public final static String EXTRA_FILE_PATH = "file_path";
	
	/**
	 * Sets whether hidden files should be visible in the list or not
	 */
	public final static String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
	
	/**
	 * The allowed file extensions in an ArrayList of Strings
	 */
	public final static String EXTRA_ACCEPTED_FILE_EXTENSIONS = "accepted_file_extensions";
	
	/**
	 * The initial directory which will be used if no directory has been sent
	 * with the intent
	 */
	private static String DEFAULT_INITIAL_DIRECTORY = "/mnt/sdcard/download";

	/**
	 * Override method
	 */
	
	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_file_chooser;
	}

	@Override
	public void initComponents() {

		DEFAULT_INITIAL_DIRECTORY = getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_DOWNLOAD_LOCATION, "");
		if (DEFAULT_INITIAL_DIRECTORY.length() <= 0) {
			DEFAULT_INITIAL_DIRECTORY = "/mnt/sdcard/download";
		}
		lstFileChooser = (ListView) findViewById(R.id.lstFileChooser);
		btnSaveOrOpen = (IjoomerButton) findViewById(R.id.btnSaveOrOpen);
		btnMakeFolder = (ImageView) findViewById(R.id.btnMakeFolder);
		edtFilePath = (IjoomerEditText) findViewById(R.id.edtFilePath);
		edtFilePath.setClickable(false);
		edtFilePath.setFocusableInTouchMode(false);
		edtFilePath.setFocusable(false);
		edtFolderName = (IjoomerEditText) findViewById(R.id.edtFolderName);
		lnrCreateFolder = (LinearLayout) findViewById(R.id.lnrCreateFolder);
		btnCreate = (IjoomerButton) findViewById(R.id.btnCreate);

		mDirectory = new File(DEFAULT_INITIAL_DIRECTORY);

		IN_ISOPENFILE = getIntent().getBooleanExtra("IN_ISOPENFILE", false);

		// Initialize the ArrayList
		mFiles = new ArrayList<File>();

		// Initialize the extensions array to allow any file extensions
		acceptedFileExtensions = new String[] {};

		// Get intent extras
		if (getIntent().hasExtra(EXTRA_FILE_PATH)) {
			mDirectory = new File(getIntent().getStringExtra(EXTRA_FILE_PATH));
		}
		if (getIntent().hasExtra(EXTRA_SHOW_HIDDEN_FILES)) {
			mShowHiddenFiles = getIntent().getBooleanExtra(EXTRA_SHOW_HIDDEN_FILES, false);
		}
		if (getIntent().hasExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS)) {
			ArrayList<String> collection = getIntent().getStringArrayListExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS);
			acceptedFileExtensions = (String[]) collection.toArray(new String[collection.size()]);
		}
		finalFilePath = mDirectory.getAbsolutePath();
	}

	@Override
	public void prepareViews() {
		// Set the ListAdapter
		mAdapter = new FilePickerListAdapter(this, mFiles);
		lstFileChooser.setAdapter(mAdapter);
		edtFilePath.setText(finalFilePath);
	}
	
	@Override
	protected void onResume() {
		refreshFilesList();
		super.onResume();
	}


	@Override
	public void setActionListeners() {
		btnSaveOrOpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (!IN_ISOPENFILE && mDirectory.canWrite()) {
					getSmartApplication().writeSharedPreferences(SP_DEFAULT_DOWNLOAD_LOCATION, mDirectory.getAbsolutePath());
					Intent extra = new Intent();
					finalFilePath = finalFilePath + "/";
					extra.putExtra("IN_PATH", finalFilePath);
					setResult(RESULT_OK, extra);
					finish();
				} else if (IN_ISOPENFILE && mDirectory.canRead()) {
					getSmartApplication().writeSharedPreferences(SP_DEFAULT_DOWNLOAD_LOCATION, mDirectory.getAbsolutePath());
					Intent extra = new Intent();
					extra.putExtra("IN_PATH", finalFilePath);
					setResult(RESULT_OK, extra);
					finish();
				} else {
					ting(getString(R.string.code705));
				}
			}
		});
		btnMakeFolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (mDirectory.canWrite()) {
					if (lnrCreateFolder.getVisibility() == View.GONE) {
						lnrCreateFolder.setVisibility(View.VISIBLE);
						edtFolderName.setText(null);
						edtFolderName.setError(null);
					} else {
						lnrCreateFolder.setVisibility(View.GONE);
					}
				} else {
					ting(getString(R.string.code705));
				}
			}
		});
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (edtFolderName.getText().toString().trim().length() <= 0) {
					edtFolderName.setError(getString(R.string.validation_value_required));
				} else {
					File wallpaperDirectory = new File(mDirectory.getAbsolutePath() + "/" + edtFolderName.getText().toString() + "/");
					wallpaperDirectory.mkdirs();
					refreshFilesList();
					lnrCreateFolder.setVisibility(View.GONE);
				}
			}
		});
		lstFileChooser.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				File newFile = (File) l.getItemAtPosition(position);

				if (newFile.isFile()) {
					if (IN_ISOPENFILE) {
						finalFilePath = newFile.getAbsolutePath();
						edtFilePath.setText(finalFilePath);
					}
				} else {
					mDirectory = newFile;
					finalFilePath = newFile.getAbsolutePath();
					edtFilePath.setText(finalFilePath);
					refreshFilesList();
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (lnrCreateFolder.getVisibility() == View.VISIBLE) {
			lnrCreateFolder.setVisibility(View.GONE);
			return;
		}
		if (mDirectory.getParentFile() != null) {

			if ((IN_ISOPENFILE && mDirectory.getParentFile().canRead()) || (!IN_ISOPENFILE && mDirectory.getParentFile().canWrite())) {
				mDirectory = mDirectory.getParentFile();
				finalFilePath = mDirectory.getAbsolutePath();
				edtFilePath.setText(finalFilePath);
				refreshFilesList();
				return;
			}
		}

		super.onBackPressed();
	}
	
	@Override
	public int setTabBarDividerResId() {
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		return 0;
	}

	@Override
	public String[] setTabItemNames() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt) {

	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	
	/**
	 * Class methods
	 */
	
	/**
	 * Updates the list view to the current directory
	 */
	private void refreshFilesList() {

		if (mDirectory.canWrite()) {
			btnMakeFolder.setVisibility(View.VISIBLE);
		} else {
			btnMakeFolder.setVisibility(View.GONE);
		}
		// Clear the files ArrayList
		mFiles.clear();

		// Set the extension file filter
		ExtensionFilenameFilter filter = new ExtensionFilenameFilter(acceptedFileExtensions);

		// Get the files in the directory
		File[] files = mDirectory.listFiles(filter);
		if (files != null && files.length > 0) {
			for (File f : files) {
				if (f.isHidden() && !mShowHiddenFiles) {
					// Don't add the file
					continue;
				}

				// Add the file the ArrayAdapter
				mFiles.add(f);
			}

			Collections.sort(mFiles, new FileComparator());
		}
		mAdapter.notifyDataSetChanged();
	}


	/**
	 * List adapter
	 */
	
	private class FilePickerListAdapter extends ArrayAdapter<File> {

		private List<File> mObjects;

		public FilePickerListAdapter(Context context, List<File> objects) {
			super(context, R.layout.ijoomer_filechooser_list_item, android.R.id.text1, objects);
			mObjects = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = null;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.ijoomer_filechooser_list_item, parent, false);
			} else {
				row = convertView;
			}

			File object = mObjects.get(position);

			ImageView imageView = (ImageView) row.findViewById(R.id.file_picker_image);
			IjoomerTextView IjoomerTextView = (IjoomerTextView) row.findViewById(R.id.file_picker_text);
			// Set single line
			IjoomerTextView.setSingleLine(true);

			IjoomerTextView.setText(object.getName());
			if (object.isFile()) {
				// Show the file icon
				imageView.setImageResource(R.drawable.file);
			} else {
				// Show the folder icon
				imageView.setImageResource(R.drawable.folder);
			}

			return row;
		}

	}

	/**
	 * Inner class 
	 */
	private class FileComparator implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			if (f1 == f2) {
				return 0;
			}
			if (f1.isDirectory() && f2.isFile()) {
				// Show directories above files
				return -1;
			}
			if (f1.isFile() && f2.isDirectory()) {
				// Show files below directories
				return 1;
			}
			// Sort the directories alphabetically
			return f1.getName().compareToIgnoreCase(f2.getName());
		}
	}

	private class ExtensionFilenameFilter implements FilenameFilter {
		private String[] mExtensions;

		public ExtensionFilenameFilter(String[] extensions) {
			super();
			mExtensions = extensions;
		}

		@Override
		public boolean accept(File dir, String filename) {
			if (new File(dir, filename).isDirectory()) {
				return true;
			}
			if (mExtensions != null && mExtensions.length > 0) {
				int size = mExtensions.length;
				for (int i = 0; i < size; i++) {
					if (filename.endsWith(mExtensions[i])) {
						return true;
					}
				}
				return false;
			}
			return true;
		}
	}

}
