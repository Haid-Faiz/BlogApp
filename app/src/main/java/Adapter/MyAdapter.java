package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Data.ListData;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context context;
    List<ListData> list;
    public MyAdapter(Context context, List<ListData> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListData data = list.get(position);

        holder.title.setText(data.getTitle());
        holder.desc.setText(data.getDesc());

        String imageURL = data.getImageurl();
        // use picasso library to load images
        // Picasso.get().load(imageURL).into(holder.imageView);
         Glide.with(context).load(imageURL).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, desc;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.titleID);
            desc = (TextView) itemView.findViewById(R.id.descID);
             imageView = (ImageView) itemView.findViewById(R.id.imageViewID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // we can do further things here
                }
            });
        }
    }
}
