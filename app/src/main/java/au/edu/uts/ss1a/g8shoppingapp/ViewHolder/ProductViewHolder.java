package au.edu.uts.ss1a.g8shoppingapp.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import au.edu.uts.ss1a.g8shoppingapp.Interface.ItemClickListener;
import au.edu.uts.ss1a.g8shoppingapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    public TextView textProductName, textProductDesc, textProductPrice, textProductStock;
    public ImageView imageView;
    public ItemClickListener itemClickListener;


    public ProductViewHolder(View context) {
        super(context);

        imageView = (ImageView) context.findViewById(R.id.product_image);
        textProductName = (TextView) context.findViewById(R.id.product_name);
        textProductDesc = (TextView) context.findViewById(R.id.product_description);
        textProductPrice = (TextView) context.findViewById(R.id.product_price);
        textProductStock = (TextView) context.findViewById(R.id.product_stock);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
