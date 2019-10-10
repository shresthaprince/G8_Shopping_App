package au.edu.uts.ss1a.g8shoppingapp.ViewHolder;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import au.edu.uts.ss1a.g8shoppingapp.Interface.ItemClickListener;
import au.edu.uts.ss1a.g8shoppingapp.R;


public class BranchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView branchName;
    public ImageView imageView;

    public ItemClickListener itemClickListener;

    public BranchViewHolder(@NonNull View itemView) {
        super(itemView);

        branchName = (TextView) itemView.findViewById(R.id.branch_name);
        imageView = (ImageView) itemView.findViewById(R.id.branch_image);
    }


    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}


