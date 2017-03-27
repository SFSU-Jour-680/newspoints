package newspoints.sfsu.com.newsp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.adapters.CustomListAdapter;
import newspoints.sfsu.com.newsp.ui.FilesListActivity;
import newspoints.sfsu.com.newsp.ui.IndexActivity;
import newspoints.sfsu.com.newsp.xmlop.ExportXML;
import newspoints.sfsu.com.newsp_data.dao.RecordingDetailsDB;
import newspoints.sfsu.com.newsp_data.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.util.BaseFragment;


/**
 * ContentMainFragment is a Fragment to display the List of all the News Activity in ListView
 */
public class ContentMainFragment extends BaseFragment {

    public static Context context;
    File projectStorageDir;
    File[] allprojectFiles;
    File[] folderFilesList;
    File selectedProjectFile;
    List<List<String>> listProjectDetails;
    TextView txtViewHelp;
    // ListView to display the News data in List
    ListView listMain;
    CustomListAdapter listAdapter;
    int searchTextLength = 0;
    String totalTimeValue;
    private IProjectSelectedListener mCallback;

    public ContentMainFragment() {
        // Required for FragmentTransaction
    }

    /**
     * This method is called to export and compress the Data in Zip folder on ListView item Long click
     *
     * @param inputFolderPath
     * @param outZipPath
     */
    private static void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content_main, container, false);
        listMain = (ListView) v.findViewById(android.R.id.list);
        txtViewHelp = (TextView) v.findViewById(R.id.txtView_help);


        final FloatingActionButton addProject = (FloatingActionButton) v.findViewById(R.id.fab_addProject);

        // get all the details
        listProjectDetails = ((IndexActivity) getActivity()).prepareListData();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (IProjectSelectedListener) activity;
            // assign the context to the activity
            context = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewsSelectedListener Interface");
        }
    }

    /**
     * This method will handle setting the Adapter to the ListView
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProjectConstants.dbClass = new RecordingDetailsDB(context);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("Click on ").append(" ");
        builder.setSpan(new ImageSpan(getActivity(), R.mipmap.ic_add_circle_black_24dp),
                builder.length() - 1, builder.length(), 0);
        builder.append(" to create new project!");

        txtViewHelp.setText(builder);

        // get the data, initialize the ListAdapter and inflate the List
        // IMP
        inflateListView();

        listMain.setLongClickable(true);

        try {
            // set the ItemClickListener for ListView item
            listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {

                        // get the project name
                        ProjectConstants.selectedProjectName = listProjectDetails.get(0).get(position);

                        String selectedProjectPath = Environment.getExternalStorageDirectory() + "/journalist/"
                                + ProjectConstants.selectedProjectName + "/";
                        ProjectConstants.selectedProjectPath = selectedProjectPath;
                        File selectedProjectFiles = new File(selectedProjectPath);
                        allprojectFiles = selectedProjectFiles.listFiles();

                        String selectedProjectCategory = allprojectFiles[0].getName();
                        ProjectConstants.selectedProjectCategory = selectedProjectCategory;
                        String selectedProjectCateogryPath = selectedProjectPath + selectedProjectCategory;

                        projectStorageDir = new File(selectedProjectCateogryPath);

                        ProjectConstants.selectedProjectCateogryPath = selectedProjectCateogryPath;

                        Intent i = new Intent(context, FilesListActivity.class);
                        i.putExtra("foldername", ProjectConstants.selectedProjectName);
                        startActivity(i);
                        ((Activity) context).finish();

                    } catch (ArrayIndexOutOfBoundsException e) {
                        listAdapter = new CustomListAdapter(context,
                                listProjectDetails.get(0), listProjectDetails.get(1), listProjectDetails.get(2));
                    } catch (NullPointerException e) {
                        listAdapter = new CustomListAdapter(context, listProjectDetails.get(0),
                                listProjectDetails.get(1), listProjectDetails.get(2));
                    }
                }
            });

            // set the LongClickListener for ListView item
            listMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        TextView totalTime = (TextView) view.findViewById(R.id.textView_listItem_time);
                        totalTimeValue = totalTime.getText().toString();
                        // get the project name
                        ProjectConstants.selectedProjectName = listProjectDetails.get(0).get(position);

                        String selectedProjectPath = Environment.getExternalStorageDirectory() + "/journalist/"
                                + ProjectConstants.selectedProjectName + "/";
                        ProjectConstants.selectedProjectPath = selectedProjectPath;

                        selectedProjectFile = new File(selectedProjectPath);
                        allprojectFiles = selectedProjectFile.listFiles();
                        String selectedProjectCategory = allprojectFiles[0].getName();
                        ProjectConstants.selectedProjectCategory = selectedProjectCategory;

                        String selectedProjectCateogryPath = selectedProjectPath + selectedProjectCategory;
                        projectStorageDir = new File(selectedProjectCateogryPath);
                        ProjectConstants.selectedProjectCateogryPath = selectedProjectCateogryPath;

                        folderFilesList = projectStorageDir.listFiles();

                        new AlertDialog.Builder(getActivity()).setTitle("Project Options").setMessage("Project Preferences")
                                .setPositiveButton("Export", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        ExportXML export = new ExportXML();
                                        export.createProjectXML(context, totalTimeValue);
                                        String fpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/journalist/"
                                                + ProjectConstants.selectedProjectName + "/"
                                                + ProjectConstants.selectedProjectCategory;
                                        String exportOutputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                                                + "Assests";

                                        // zip the folder
                                        zipFolder(fpath, exportOutputPath);

                                        File file = new File(exportOutputPath);
                                        System.out.println("###### " + file.getAbsolutePath());
                                        Uri uri = Uri.fromFile(file);
                                        Intent sendIntent = new Intent(Intent.ACTION_SEND);

                                        sendIntent.setType("*/zip");

                                        sendIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
                                        startActivity(Intent.createChooser(sendIntent, "Share Project - "
                                                + ProjectConstants.selectedProjectName));
                                    }
                                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // donothing
                                new AlertDialog.Builder(context).setTitle("Delete")
                                        .setMessage("Are you sure you want to delte this project")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                for (int i = 0; i < folderFilesList.length; i++) {
                                                    folderFilesList[i].delete();
                                                }

                                                // delete the project directory and path
                                                //boolean check1 = projectStorageDir.delete();
                                                projectStorageDir.delete();
                                                selectedProjectFile.delete();
                                                //boolean check2 = selectedProjectFile.delete();

                                                //Toast.makeText(context, check1 + ", " + check2, Toast.LENGTH_SHORT).show();

                                                // delete the project
                                                deleteProject(ProjectConstants.selectedProjectName);
                                                // refresh and inflate the ListView
                                                inflateListView();

                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    } catch (ArrayIndexOutOfBoundsException a) {
                    }
                    return true;
                }
            });

        } catch (NullPointerException e) {
            Log.d("=====>", "NULL in long click: " + e.getMessage());
        } catch (Exception ex) {
            Log.d("=====>", "Error in Long click listener: " + ex.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume the Fragment
        if (context != null) {
            inflateListView();
        }
    }

    // inflate ListView is used to inflate the ListView by retrieving all the Data from Database.
    private void inflateListView() {
        try {
            // get the new updated data from DB
            listProjectDetails = ((IndexActivity) getActivity()).prepareListData();

            // Toast.makeText(context, listProjectDetails.get(0).size() + "", Toast.LENGTH_LONG).show();
            // depending on whether data is present on ListView or not, set Visibility of TextView
            if (listProjectDetails.get(0).size() == 0) {
                txtViewHelp.setVisibility(View.VISIBLE);
                listMain.setVisibility(View.GONE);
            } else {
                // adjust the visibility of the Views
                listMain.setVisibility(View.VISIBLE);
                txtViewHelp.setVisibility(View.GONE);
                // pass the data to CustomListAdapter

                listAdapter = new CustomListAdapter(context, listProjectDetails.get(0), listProjectDetails.get(1), listProjectDetails.get(2));
                listAdapter.notifyDataSetChanged();
                listMain.setAdapter(listAdapter);
            }

        } catch (Exception e) {
            Log.d("=====>", "List inflator error: " + e.getMessage());
        }
    }

    // must be implemented in Fragment for Better performance
    private void deleteProject(String projectId) {
        ProjectConstants.dbClass.deleteProject(projectId);
        ProjectConstants.dbClass.deleteProjectRecordings(projectId);
    }


    // method to change the ListView content based on parseQuery
    public boolean parseQuery(String newText) {

        if (listProjectDetails != null) {
            List<String> projectNames = listProjectDetails.get(0);
            List<String> projectDates = listProjectDetails.get(1);
            List<String> projectImages = listProjectDetails.get(2);

            List<String> sProjectNames = new ArrayList<String>();
            List<String> sProjectImages = new ArrayList<String>();
            List<String> sProjectDates = new ArrayList<String>();
            String temp;


            if (newText.equals("") || newText.equals(null)) {
                // inflate the List using the default data in db
                inflateListView();

            } else {
                if (newText.length() < searchTextLength) {

                    // get the Details from DB
                    listProjectDetails = ((IndexActivity) getActivity()).prepareListData();
                    projectNames = listProjectDetails.get(0);
                    projectDates = listProjectDetails.get(1);
                    projectImages = listProjectDetails.get(2);

                }
                for (int j = 0; j < projectNames.size(); j++) {
                    String tempName = projectNames.get(j);
                    String tempImage = projectImages.get(j);
                    String tempDate = projectDates.get(j);
                    String compareString = tempName.toLowerCase();
                    if (compareString.contains(newText)) {
                        sProjectNames.add(tempName);
                        sProjectImages.add(tempImage);
                        sProjectDates.add(tempDate);
                    }
                }
                if (projectNames.size() > 0) {
                    listAdapter = new CustomListAdapter(context, projectNames, projectImages, projectDates);
                }

                listAdapter.notifyDataSetChanged();
                listMain.setAdapter(listAdapter);


            /*
             * Very important to set ProjectList=i, since after the search the
			 * list gets shorter and selecting upon any item in it will make the ListView set onclicklistener make
			 * ProjectConstants.selectedProjectName be different then what you are selecting
			 */

                listProjectDetails.set(0, sProjectNames);
                listProjectDetails.set(1, sProjectDates);
                listProjectDetails.set(2, sProjectImages);
            }
        }
        searchTextLength = newText.length();

        return false;
    }

    @Override
    public String toString() {
        return "ContentMainFragment";
    }


    // This interface implements the Callback Interface.
    public interface IProjectSelectedListener {
        /**
         * Called by ContentMainFragment when a list item is selected
         */
        public void onItemSelected(int position);
    }
}
