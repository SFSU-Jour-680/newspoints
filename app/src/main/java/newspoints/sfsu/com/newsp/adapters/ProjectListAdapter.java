package newspoints.sfsu.com.newsp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.util.ProjectConstants;
import newspoints.sfsu.com.newsp_data.entities.Project;
import newspoints.sfsu.com.newsp_lib.util.AppUtil;

/**
 * Adapter to display list of Projects into RecyclerView. The adapter specifies the row item using a CardView layout. The
 * ProjectViewHolder is used to apply smooth scrolling for large entries in RecyclerView
 * <p>
 * Created by Pavitra on 3/24/2016.
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private static final String TAG = "~!@#$ProjListAdptr";
    private List<Project> projectList;
    private Context mContext;

    public ProjectListAdapter(List<Project> projectList, Context mContext) {
        this.projectList = projectList;
        this.mContext = mContext;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_project_card, parent, false);
        return new ProjectViewHolder(itemRootView);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        try {
            assert holder != null;

            Project mProject = projectList.get(position);
            Log.i(TAG, mProject.toString());

            holder.txtView_projectName.setText(mProject.getName());
            String dateAndTime = AppUtil.getFullDateAndTime(mProject.getTimestamp());
            holder.txtView_dateTime.setText(dateAndTime);
            holder.txtView_location.setText("San Francisco");
            holder.txtView_audioCount.setText("14");
            holder.txtView_videoCount.setText("18");
            holder.txtView_category.setText(mProject.getCategory());

//            // display images
//            if (mProject.getImage_url() != null && !mProject.getImage_url().equals("")) {
//                Picasso.with(mContext).load(mProject.getImage_url()).into(holder.imageView_projectImage);
//            } else {
//                Picasso.with(mContext).load(R.drawable.placeholder_project_image_main).into(holder.imageView_projectImage);
//            }

            Picasso.with(mContext).load(R.drawable.placeholder_android_n).into(holder.imageView_projectImage);

            // display icon
            if (mProject.getCloudStorage() == ProjectConstants.CLOUD_GOOGLE_DRIVE) {
                Picasso.with(mContext).load(R.drawable.gdrive_product256).into(holder.imageView_driveStorageIcon);
            } else if (mProject.getCloudStorage() == ProjectConstants.CLOUD_DROPBOX) {
                Picasso.with(mContext).load(R.drawable.dropbox_android).into(holder.imageView_driveStorageIcon);
            } else {
                Picasso.with(mContext).load(R.drawable.placeholder_project_image_main).into(holder.imageView_projectImage);
            }


        } catch (NullPointerException ne) {
            Log.e(TAG, "onBindViewHolder: " + ne.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: " + e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    // filter methods for animation in RecyclerView upon SearchQuery change
    public void animateTo(List<Project> projects) {
        applyAndAnimateRemovals(projects);
        applyAndAnimateAdditions(projects);
        applyAndAnimateMovedItems(projects);
    }

    private void applyAndAnimateRemovals(List<Project> newProject) {
        for (int i = projectList.size() - 1; i >= 0; i--) {
            final Project mProject = projectList.get(i);
            if (!newProject.contains(mProject)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Project> newProjectList) {
        for (int i = 0, count = newProjectList.size(); i < count; i++) {
            final Project mProject = newProjectList.get(i);
            if (!projectList.contains(mProject)) {
                addItem(i, mProject);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Project> newProjectList) {
        for (int toPosition = newProjectList.size() - 1; toPosition >= 0; toPosition--) {
            final Project mProject = newProjectList.get(toPosition);
            final int fromPosition = projectList.indexOf(mProject);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Project mProject = projectList.remove(fromPosition);
        projectList.add(toPosition, mProject);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Project removeItem(int position) {
        final Project mProject = projectList.remove(position);
        notifyItemRemoved(position);
        return mProject;
    }

    public void addItem(int position, Project mProject) {
        projectList.add(position, mProject);
        notifyItemInserted(position);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewProject;
        private ImageView imageView_projectImage;
        private ImageView imageView_driveStorageIcon;
        private TextView txtView_projectName;
        private TextView txtView_location;
        private TextView txtView_audioCount;
        private TextView txtView_videoCount;
        private TextView txtView_dateTime;
        private TextView txtView_category;

        /**
         * Inflates the ProjectViewHolder with the item layout
         *
         * @param itemView
         */
        public ProjectViewHolder(View itemView) {
            super(itemView);

            cardViewProject = (CardView) itemView.findViewById(R.id.cardView_projlistItem_main);
            imageView_projectImage = (ImageView) itemView.findViewById(R.id.imageView_listItem_projectImage);
            imageView_driveStorageIcon = (ImageView) itemView.findViewById(R.id.imageView_listItem_cloudIcon);
            txtView_projectName = (TextView) itemView.findViewById(R.id.textView_listItem_header);
            txtView_dateTime = (TextView) itemView.findViewById(R.id.textView_listItem_dateAndTime);
            txtView_location = (TextView) itemView.findViewById(R.id.textView_listItem_location);
            txtView_audioCount = (TextView) itemView.findViewById(R.id.textView_listItem_audioCount);
            txtView_videoCount = (TextView) itemView.findViewById(R.id.textView_listItem_videoCount);
            txtView_category = (TextView) itemView.findViewById(R.id.textView_listItem_category);
        }
    }
}
