package rahul.nirmesh.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import rahul.nirmesh.newsapp.Interface.IconBetterIdeaService;
import rahul.nirmesh.newsapp.Interface.ItemClickListener;
import rahul.nirmesh.newsapp.ListNews;
import rahul.nirmesh.newsapp.R;
import rahul.nirmesh.newsapp.common.Common;
import rahul.nirmesh.newsapp.model.IconBetterIdea;
import rahul.nirmesh.newsapp.model.WebSite;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NIRMESH on 06-Jan-18.
 */

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListener itemClickListener;

    TextView source_name;
    CircleImageView source_image;

    public ListSourceViewHolder(View itemView) {
        super(itemView);

        source_name = itemView.findViewById(R.id.source_name);
        source_image = itemView.findViewById(R.id.source_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder> {

    private Context context;
    private WebSite webSite;
    private IconBetterIdeaService mService;

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;

        mService = Common.getIconService();
    }

    @Override
    public ListSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, parent, false);
        return new ListSourceViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(final ListSourceViewHolder holder, int position) {
        StringBuilder iconBetterAPI = new StringBuilder("https://icons.better-idea.org/allicons.json?url=");
        iconBetterAPI.append(webSite.getSources().get(position).getUrl());

        mService.getIconUrl(iconBetterAPI.toString()).enqueue(new Callback<IconBetterIdea>() {
            @Override
            public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response) {
                if (response.body() != null && response.body().getIcons() != null
                        && response.body().getIcons().size() > 0
                        && !TextUtils.isEmpty(response.body().getIcons().get(0).getUrl())) {
                    Picasso.with(context)
                            .load(response.body().getIcons().get(0).getUrl())
                            .into(holder.source_image);
                }
            }

            @Override
            public void onFailure(Call<IconBetterIdea> call, Throwable t) {

            }
        });

        holder.source_name.setText(webSite.getSources().get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source", webSite.getSources().get(position).getId());
                intent.putExtra("sortBy", webSite.getSources().get(position).getSortBysAvailable().get(0));
                intent.putExtra("sortBy", "top");
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return webSite.getSources().size();
    }
}
