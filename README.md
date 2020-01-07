# WEBPlayer
[![license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/webilisim/WEBPlayer/master/LICENSE)
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[ ![Download](https://api.bintray.com/packages/netwebilisim/WEBSlider/net.webilisim.webslider/images/download.svg?version=1.0.3) ](https://bintray.com/netwebilisim/WEBSlider/net.webilisim.webslider/1.0.3/link)

Advanced and easy android image slider implement custom adapter and animations.
 
 ## QuickStart

1. Implementation
```gradle
implementation 'net.webilisim.webslider:webslider:1.0.3'
```

2. Set WEBSlider in your layout
```xml
<androidx.cardview.widget.CardView
        app:cardCornerRadius="6dp"
        android:layout_margin="16dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

 <net.webilisim.webslider.WEBSliderView
        android:id="@+id/webSliderView"
        android:layout_width="match_parent"
        android:layout_height="185dp"
        app:sliderAnimationDuration="600"
        app:sliderAutoCycleDirection="back_and_forth"
        app:sliderAutoCycleEnabled="true"
        app:sliderCircularHandlerEnabled="true"
        app:sliderIndicatorAnimationDuration="600"
        app:sliderIndicatorGravity="center_horizontal|bottom"
        app:sliderIndicatorMargin="15dp"
        app:sliderIndicatorOrientation="horizontal"
        app:sliderIndicatorPadding="3dp"
        app:sliderIndicatorRadius="2dp"
        app:sliderIndicatorSelectedColor="#838383"
        app:sliderIndicatorUnselectedColor="#005E2D"
        app:sliderScrollTimeInSec="1"
        app:sliderStartAutoCycle="true" />
</androidx.cardview.widget.CardView>
```

3. Set your image to WEBSlider
```java
   WEBSliderView WEBSliderView = findViewById(R.id.webSliderView);
   WEBSliderView.setSliderAdapter(new SliderAdapterExample(this));
   WEBSliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
   WEBSliderView.setSliderTransformAnimation(WEBSliderAnimations.FADETRANSFORMATION);
   WEBSliderView.setAutoCycleDirection(net.webilisim.webslider.WEBSliderView.AUTO_CYCLE_DIRECTION_RIGHT);
   WEBSliderView.setIndicatorSelectedColor(Color.WHITE);
   WEBSliderView.setIndicatorUnselectedColor(Color.GRAY);
   WEBSliderView.setScrollTimeInSec(4);
   WEBSliderView.startAutoCycle();
```

4. Use custom adapter for WEBSlider
```java
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
```

5. Set adapter to WEBSlider
```java
    WEBSliderView.setSliderAdapter(new SliderAdapterExample(context));
```

# Licence

Copyright [2020] [We Bilişim Interactive]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
