package net.webilisim.websliderdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import net.webilisim.webslider.WEBSliderViewAdapter;

public class SliderAdapterExample extends WEBSliderViewAdapter<SliderAdapterExample.AdapterViewHolder> {

    private Context context;

    SliderAdapterExample(Context context) {
        this.context = context;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_webslider_adapter_layout, null);
        return new AdapterViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AdapterViewHolder viewHolder, int position) {
        viewHolder.textView.setText("Here is slider item " + position);

        switch (position) {
            case 0:
                Glide.with(context)
                        .load("https://wallpapers4screen.com/Uploads/19-1-2016/2658/thumb2-panorama-clouds-girl-the-sky-stone.jpg")
                        .into(viewHolder.imageView);
                break;
            case 1:
                Glide.with(context)
                        .load("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg")
                        .into(viewHolder.imageView);
                break;
            case 2:
                Glide.with(context)
                        .load("https://cdn.pixabay.com/photo/2018/01/12/10/19/fantasy-3077928__340.jpg")
                        .into(viewHolder.imageView);
                break;
            default:
                Glide.with(context)
                        .load("https://images.unsplash.com/photo-1538370965046-79c0d6907d47?ixlib=rb-1.2.1&w=1000&q=80")
                        .into(viewHolder.imageView);
                break;

        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    class AdapterViewHolder extends WEBSliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageView;
        TextView textView;

        AdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.webslider_image);
            textView = itemView.findViewById(R.id.webslider_textview);
            this.itemView = itemView;
        }
    }
}