package com.sarker.hellodoctor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sarker.hellodoctor.R;
import com.sarker.hellodoctor.model.SlideInfo;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

        private Context context;
        private ArrayList<SlideInfo> mSliderItems = new ArrayList<>();

        public SliderAdapter(Context context) {
            this.context = context;
        }

        public void renewItems(ArrayList<SlideInfo> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();

        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

            SlideInfo sliderItem = mSliderItems.get(position);

            final String key = sliderItem.getImageKey();
            final String url = sliderItem.getImageUrl();

            viewHolder.textViewDescription.setText(sliderItem.getDescription());
            viewHolder.textViewDescription.setTextSize(18);
            viewHolder.textViewDescription.setTextColor(Color.WHITE);

            Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.doctor_icon).into(viewHolder.imageViewBackground);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return mSliderItems.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {


            ImageView imageViewBackground;
            TextView textViewDescription;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            }
        }
}
