package net.webilisim.webslider.ViewIndicator;

import androidx.annotation.Nullable;
import net.webilisim.webslider.ViewIndicator.animation.AnimationManager;
import net.webilisim.webslider.ViewIndicator.animation.controller.ValueController;
import net.webilisim.webslider.ViewIndicator.animation.data.Value;
import net.webilisim.webslider.ViewIndicator.draw.DrawManager;
import net.webilisim.webslider.ViewIndicator.draw.data.Indicator;

public class IndicatorManager implements ValueController.UpdateListener {

    private DrawManager drawManager;
    private AnimationManager animationManager;
    private Listener listener;

    interface Listener {
        void onIndicatorUpdated();
    }

    IndicatorManager(@Nullable Listener listener) {
        this.listener = listener;
        this.drawManager = new DrawManager();
        this.animationManager = new AnimationManager(drawManager.indicator(), this);
    }

    public AnimationManager animate() {
        return animationManager;
    }

    public Indicator indicator() {
        return drawManager.indicator();
    }

    public DrawManager drawer() {
        return drawManager;
    }

    @Override
    public void onValueUpdated(@Nullable Value value) {
        drawManager.updateValue(value);
        if (listener != null) {
            listener.onIndicatorUpdated();
        }
    }
}
