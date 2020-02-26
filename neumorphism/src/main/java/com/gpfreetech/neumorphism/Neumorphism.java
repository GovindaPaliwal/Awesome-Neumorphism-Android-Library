package com.gpfreetech.neumorphism;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.ToggleButton;
import com.google.android.material.tabs.TabLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.CompoundButtonCompat;

import static android.view.Gravity.START;
public class Neumorphism {
    public static final int CIRCULAR_SHAPE = 1;
    private Context mContext;
    private Path mPath;
    private Paint mPaint;
    private ArrayMap<Integer,View> views = new ArrayMap<>();
    private ArrayMap<Integer,View> clipChildViews = new ArrayMap<>();
    private int elevation = 12;
    private int radius = 12;
    private int borders,
            parentColor,parentColorLight,parentColorDark,backgroundColor,controlColor;
    private Drawable backgroundDrawable;
    private boolean sharpEdges,withBorders,curvedSurface,circular;

    public Neumorphism(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }

    public Neumorphism setViews(View... view) {
        int i = 0;
        for(View mview : view){
            views.put(i++,mview);
        }
        return this;
    }

    /**
     * Make the image view child rounded corners, if any.
     * you can pass multiple child's to clip.
     * @param view
     * @return
     */
    public Neumorphism clipChildren(View... view) {
        int i = 0;
        for(View mview : view){
            clipChildViews.put(i++,mview);
        }
        return this;
    }

    /**
     *  the color of parent view background, can be any color
     * @param color
     * @return
     */
    public Neumorphism parentColor(int color) {
        parentColor = color;
        backgroundColor = color;
        return this;
    }

    /**
     *  the color of control components like checkbox, radio button icon color.
     * @param color
     * @return
     */
    public Neumorphism controlColor(int color) {
        controlColor = color;
        return this;
    }

    /**
     * pass color If you like to set custom color for the view surface different than parent's background.
     * @param color
     * @return
     */
    public Neumorphism backgroundColor(int color) {
        backgroundColor = color;
        return this;
    }

    /**
     * set a drawable background instead of solid color.
     * @param drawable
     * @return
     */
    public Neumorphism backgroundDrawable(Drawable drawable) {
        backgroundDrawable = drawable;
        return this;
    }

    /**
     * provide option to lib that you want circular shape for this button or Rect
     * @param shape
     * @return
     */
    public Neumorphism viewShape(int shape) {
        circular = shape == CIRCULAR_SHAPE;
        return this;
    }

    /**
     * value of borders for views
     * @param borders
     * @return
     */
    public Neumorphism withBorders(int borders) {
        withBorders = true;
        this.borders = borders;
        return this;
    }

    /**
     * value of corners radius for views
     * @param radius
     * @return
     */
    public Neumorphism withRoundedCorners(int radius) {
        this.radius = radius;
        return this;
    }

    /**
     * Call if you want to view as a surface seems curved.
     * makes the view surface seems curved with colour shades
     * @return
     */
    public Neumorphism withCurvedSurface() {
        curvedSurface = true;
        return this;
    }

    /**
     * You can adjust elevation by providing value in int
     * @param elevation
     * @return
     */
    public Neumorphism Elevation(int elevation) {
        this.elevation = elevation;
        return this;
    }

    /**
     * boolean determains if the view's Neumorphism shadow sharp:true or soft:false
     * @param isSharpEdges
     * @return
     */
    public Neumorphism sharpEdges(boolean isSharpEdges) {
        this.sharpEdges = isSharpEdges;
        return this;
    }

    /**
     * You can final build your view's using build() method
     */
    public void build() {
        initColors();
        for(int i = 0;  i < views.size(); i++) {
            final View v = views.valueAt(i);
            v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    soften(v);
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        views.clear();
    }

    private void initColors() {
        double howDarkColor = howDark(parentColor);
        howDarkColor = howDarkColor<0.5?howDarkColor*2:howDarkColor;
        parentColorLight = lightColor(parentColor, 1.0-howDarkColor);
        parentColorDark = darkColor(parentColor, 0.3f);
    }

    double howDark(int color) {
        return ColorUtils.calculateLuminance(color);
    }

    private static int lightColor(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = light(red, fraction);
        green = light(green, fraction);
        blue = light(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    private static int darkColor(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = dark(red, fraction);
        green = dark(green, fraction);
        blue = dark(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int light(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    private static int dark(int color, double fraction) {
        return (int)Math.max(color - (color * fraction), 0);
    }

    private static int alphaColor(int color, int fraction) {
        return  Color.argb(fraction, Color.red(color), Color.green(color), Color.blue(color));
    }

    private void soften(View view) {
        if (view instanceof EditText) {
            softenEditText(view);
        } else if (view instanceof ToggleButton) {
            softenToggle(view);
        } else if (view instanceof Switch) {
            softenSwitch(view);
        } else if (view instanceof CheckBox) {
            softenCheckBox(view);
        } else if (view instanceof RadioButton) {
            softenRadioButton(view);
        } else if (view instanceof Button || view instanceof ImageButton) {
            softenButton(view);
        } else if (view instanceof SeekBar) {
            softenSeekBar(view);
        } else if (view instanceof ProgressBar) {
            softenProgress(view);
        } else if (view instanceof ImageView) {
            softenImage(view);
        } else if (view instanceof TabLayout) {
            softenTabLayout(view);
        } else {
            softenView(view);
        }
    }

    private void softenView(View view) {
        view.setPadding(view.getPaddingLeft()+elevation,
                view.getPaddingTop()+elevation,
                view.getPaddingRight()+elevation,
                view.getPaddingBottom()+elevation);
        if(clipChildViews!=null&&!clipChildViews.isEmpty()){
            for(int i = 0;  i < clipChildViews.size(); i++) {
                final View cv = clipChildViews.valueAt(i);
                clipChild(cv);
            }
        }
        view.setBackground(drawSoftMagic(view, false, false));
        clipChildViews.clear();
    }

    private void softenEditText(View view) {
        view.setPadding(view.getPaddingLeft()+elevation,
                view.getPaddingTop()+elevation,
                view.getPaddingRight()+elevation,
                view.getPaddingBottom()+elevation);
        view.setBackground(drawSoftMagic(view, true, false));
    }

    private void softenImage(View view) {
        view.setPadding(view.getPaddingLeft()+elevation,
                view.getPaddingTop()+elevation,
                view.getPaddingRight()+elevation,
                view.getPaddingBottom()+elevation);
        clipChild(view);
        view.setBackground(drawSoftMagic(view, true, false));
    }

    private void softenButton(View view) {
        if(!circular) {
            view.setPadding(view.getPaddingLeft() + elevation,
                    view.getPaddingTop() + elevation,
                    view.getPaddingRight() + elevation,
                    view.getPaddingBottom() + elevation);
        }
        view.setStateListAnimator(null);
        view.setElevation(0);
        StateListDrawable res = new StateListDrawable();
        res.setExitFadeDuration(300);
        res.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(parentColor));
        res.addState(new int[]{}, drawSoftMagic(view, false, false));
        view.setBackground(res);
    }

    private void softenCheckBox(View view) {
        CheckBox mCheckBox = (CheckBox)view;
        mCheckBox.setPadding(view.getPaddingLeft()+elevation,
                view.getPaddingTop()+elevation,
                view.getPaddingRight()+elevation,
                view.getPaddingBottom()+elevation);
        Drawable drawable = CompoundButtonCompat.getButtonDrawable(mCheckBox);
        if(drawable!=null) {
            view.setStateListAnimator(null);
            view.setElevation(0);
            StateListDrawable res = new StateListDrawable();
            res.setExitFadeDuration(300);
            res.addState(new int[]{android.R.attr.state_checked}, createNeumorphismIcon(drawable,false, true));
            res.addState(new int[]{}, createNeumorphismIcon(drawable,false, false));
            mCheckBox.setButtonDrawable(res);
        }else{
            mCheckBox.setBackground(drawSoftMagic(mCheckBox, false, false));
        }
    }

    private void softenRadioButton(View view) {
        RadioButton mRadioButton = (RadioButton)view;
        mRadioButton.setPadding(view.getPaddingLeft()+elevation,
                view.getPaddingTop()+elevation,
                view.getPaddingRight()+elevation,
                view.getPaddingBottom()+elevation);
        Drawable drawable = CompoundButtonCompat.getButtonDrawable(mRadioButton);
        if(drawable!=null) {
            StateListDrawable res = new StateListDrawable();
            res.setExitFadeDuration(300);
            res.addState(new int[]{android.R.attr.state_checked}, createNeumorphismIcon(drawable,true, true));
            res.addState(new int[]{}, createNeumorphismIcon(drawable,true, false));
            mRadioButton.setButtonDrawable(res);
        }else{
            mRadioButton.setBackground(drawSoftMagic(mRadioButton, false, false));
        }
    }

    private void softenToggle(View view) {
        ToggleButton mToggleButton = (ToggleButton)view;
        mToggleButton.setPadding(elevation,elevation,elevation,elevation);
        mToggleButton.setStateListAnimator(null);
        mToggleButton.setElevation(0);
        StateListDrawable res = new StateListDrawable();
        res.setExitFadeDuration(300);
        res.addState(new int[]{android.R.attr.state_checked}, drawSoftMagic(mToggleButton,true, false));
        res.addState(new int[]{}, drawSoftMagic(mToggleButton,false, false));
        mToggleButton.setBackground(res);
    }

    private void softenSwitch(View view) {
        Switch mSwitch = (Switch)view;
        mSwitch.setTrackDrawable(drawSoftMagic(mSwitch,true, false));
    }

    private void softenSeekBar(View view) {
        SeekBar mSeekBar = (SeekBar)view;
        mSeekBar.setPadding(0,0,elevation,0);
        mSeekBar.setElevation(0);
        mSeekBar.setSplitTrack(false);
        mSeekBar.setThumbOffset(12);
        mSeekBar.setBackground(drawSoftMagic(mSeekBar,true, false, mSeekBar.getHeight()/2));
        ClipDrawable mClipDrawable = new ClipDrawable(drawSoftMagic(mSeekBar,false, true, mSeekBar.getHeight()/2), START, ClipDrawable.HORIZONTAL);
        mClipDrawable.setLevel(mSeekBar.getProgress());
        mClipDrawable.invalidateSelf();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSeekBar.setProgress(50,true);
        }else{
            mSeekBar.setProgress(mSeekBar.getProgress());
        }
        mSeekBar.setProgressDrawable(mClipDrawable);
        mSeekBar.refreshDrawableState();
    }

    private void softenProgress(View view) {
        ProgressBar mProgressBar = (ProgressBar)view;
        if(!circular) {
            mProgressBar.setPadding(mProgressBar.getPaddingLeft() + elevation,
                    mProgressBar.getPaddingTop() + elevation,
                    mProgressBar.getPaddingRight() + elevation,
                    mProgressBar.getPaddingBottom() + elevation);
        }
        mProgressBar.setPadding(0,0,elevation,0);
        mProgressBar.setElevation(0);
        mProgressBar.setBackground(drawSoftMagic(mProgressBar,true, false));
    }

    private void softenTabLayout(View view) {
        view.setPadding(elevation,
                elevation,
                elevation,
                elevation);
        view.setBackground(drawSoftMagic(view, false, false));
    }

    private void clipChild(View cView) {
        cView.setClipToOutline(true);
        cView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(
                        new Rect(
                                0,
                                0,
                                view.getWidth(),
                                view.getHeight()),radius);
            }
        });
    }

    private BitmapDrawable drawSoftMagic(View view, boolean reverse, boolean mask) {
        return drawSoftMagic(view, reverse, mask, view.getWidth(), view.getHeight(), radius);
    }

    private BitmapDrawable drawSoftMagic(View view, boolean reverse, boolean mask, int radius) {
        return drawSoftMagic(view, reverse, mask, view.getWidth(), view.getHeight(), radius);
    }

    private BitmapDrawable drawSoftMagic(View view, boolean reverse, boolean mask, int width, int height, int radius) {

        int halfPathWidth = elevation;
        if(sharpEdges) {
            halfPathWidth = elevation/2;
        }
        int viewWidth = circular?height:width;
        int viewHeight = height;

        CornerPathEffect corEffect = new CornerPathEffect(radius);
        mPaint.setPathEffect(corEffect);

        Bitmap canvasBitmap = null;
        try {
            canvasBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            Canvas imageCanvas = new Canvas(canvasBitmap);

            mPath.rewind();
            mPaint.setMaskFilter(new BlurMaskFilter(elevation/2, BlurMaskFilter.Blur.NORMAL));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(elevation);

            if(reverse){
                mPaint.setColor(alphaColor(parentColorDark, mask?175:75));
            }else{
                mPaint.setColor(alphaColor(parentColorLight, mask?175:75));
            }
//            mPaint.setColor(Color.RED);

            if(circular){
                imageCanvas.drawArc(
                        new RectF(
                                halfPathWidth+elevation,
                                halfPathWidth+elevation,
                                viewWidth-elevation-halfPathWidth,
                                viewHeight-elevation-halfPathWidth),
                        135, 180, false, mPaint);
            }else{
                mPath.moveTo((radius/2), viewHeight-radius/2);
                mPath.lineTo(halfPathWidth, viewHeight-radius);
                mPath.lineTo(halfPathWidth, halfPathWidth+radius/2);
                mPath.lineTo(radius, halfPathWidth);
                mPath.lineTo(viewWidth-halfPathWidth-radius, halfPathWidth);
                mPath.lineTo(viewWidth-radius/2, radius/2);
                imageCanvas.drawPath(mPath, mPaint);
            }

            if(reverse){
                mPaint.setColor(alphaColor(parentColorLight, 75));
            }else{
                mPaint.setColor(alphaColor(parentColorDark, 75));
            }
//            mPaint.setColor(Color.BLUE);

            if(circular){
                imageCanvas.drawArc(
                        new RectF(
                                halfPathWidth+elevation,
                                halfPathWidth+elevation,
                                viewWidth-elevation-halfPathWidth,
                                viewHeight-elevation-halfPathWidth),
                        315, 180, false, mPaint);
            }else {
                mPath.rewind();
                mPath.moveTo((radius / 2), viewHeight - radius / 2);
                mPath.lineTo(radius, viewHeight - halfPathWidth);
                mPath.lineTo(viewWidth - radius, viewHeight - halfPathWidth);
                mPath.lineTo(viewWidth - radius / 2, viewHeight - radius / 2);
                mPath.lineTo(viewWidth - halfPathWidth, viewHeight - halfPathWidth - radius);
                mPath.lineTo(viewWidth - halfPathWidth, halfPathWidth + radius);
                mPath.lineTo(viewWidth - radius / 2, radius / 2);
                imageCanvas.drawPath(mPath, mPaint);
            }

            mPaint.setPathEffect(null);
            mPaint.setMaskFilter(null);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(parentColor);

            if(mask){
                mPaint.setColor(controlColor);
            }

            if(!curvedSurface||mask){
                if(circular){
                    imageCanvas.drawCircle(
                            viewWidth/2,viewHeight/2,
                            viewHeight/2-elevation*2, mPaint);
                }else{
                    if(backgroundDrawable!=null){
                        backgroundDrawable.setBounds(
                                new Rect(
                                        elevation,
                                        elevation,
                                        viewWidth-elevation,
                                        viewHeight-elevation));
                        backgroundDrawable.draw(imageCanvas);
                    }else{
                        imageCanvas.drawRoundRect(
                                new RectF(
                                        elevation,
                                        elevation,
                                        viewWidth-elevation,
                                        viewHeight-elevation),
                                radius,radius,
                                mPaint);
                    }
                }
            }else{
                GradientDrawable gd = new GradientDrawable(
                        reverse?GradientDrawable.Orientation.BR_TL:GradientDrawable.Orientation.TL_BR,
                        reverse?new int[]{alphaColor(parentColorLight,225), parentColor, alphaColor(parentColorDark,100)}
                                :new int[]{alphaColor(parentColorDark,100), parentColor, alphaColor(parentColorLight,200)}
                );
                if(circular){
                    gd.setGradientCenter(viewWidth+elevation/2,viewHeight+elevation/2);
                    gd.setShape(GradientDrawable.OVAL);
                    gd.setBounds(new Rect(
                            elevation*2,
                            elevation*2,
                            viewWidth-elevation*2,
                            viewHeight-elevation*2));
                }else{
                    gd.setCornerRadius(radius);
                    gd.setBounds(new Rect(
                            elevation,
                            elevation,
                            viewWidth-elevation,
                            viewHeight-elevation));
                }
                gd.draw(imageCanvas);
            }

            if(withBorders){
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(borders);
                mPaint.setColor(controlColor);
                if(circular){
                    imageCanvas.drawCircle(
                            viewWidth/2,viewHeight/2,
                            viewHeight/2-elevation*2, mPaint);
                }else{
                    imageCanvas.drawRoundRect(
                            new RectF(
                                    elevation,
                                    elevation,
                                    viewWidth-elevation,
                                    viewHeight-elevation),
                            radius,radius,
                            mPaint);
                }
            }

        }catch (Exception e) {
        }
        return  new BitmapDrawable(mContext.getResources(),canvasBitmap);
    }

    private Drawable createNeumorphismIcon(Drawable icon, boolean rounded, boolean reverse) {

        icon.setTintMode(PorterDuff.Mode.SRC_IN);
        icon.setTint(controlColor);

        int iconWidth = icon.getIntrinsicWidth();
        int iconHeight = icon.getIntrinsicHeight();
        GradientDrawable gd = new GradientDrawable(
                reverse?GradientDrawable.Orientation.BR_TL:GradientDrawable.Orientation.TL_BR,
                new int[]{alphaColor(parentColorLight,200), alphaColor(parentColorDark,100)}
        );
        if(rounded){
            gd.setGradientCenter(iconWidth+elevation/2,iconHeight+elevation/2);
            gd.setShape(GradientDrawable.OVAL);
            gd.setBounds(new Rect(
                    elevation*2,
                    elevation*2,
                    iconWidth-elevation*2,
                    iconHeight-elevation*2));
        }else {
            gd.setCornerRadius(radius);
            gd.setBounds(new Rect(
                    elevation,
                    elevation,
                    iconWidth - elevation,
                    iconHeight - elevation));
        }

        LayerDrawable layerDrawable;
        if(reverse){
            Drawable[] layers = {
                    gd,
                    icon
            };
            layerDrawable = new LayerDrawable(layers);
        }else{
            Drawable[] layers = {
                    gd,
                    icon
            };
            layerDrawable = new LayerDrawable(layers);
        }
        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable.setLayerInset(1, elevation/2, elevation/2, elevation/2, elevation/2);
        return layerDrawable;
    }

}
