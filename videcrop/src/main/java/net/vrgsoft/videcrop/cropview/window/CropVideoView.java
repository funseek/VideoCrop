package net.vrgsoft.videcrop.cropview.window;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import net.vrgsoft.videcrop.R;
import net.vrgsoft.videcrop.cropview.window.edge.Edge;

public class CropVideoView extends FrameLayout {
    private final String TAG = "CropVideoView";
    private PlayerView mPlayerView;
    private CropView mCropView;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoRotationDegrees;
    private int mGuidelines = 1;
    private boolean mFixAspectRatio = false;
    private int mAspectRatioX = 1;
    private int mAspectRatioY = 1;

    public CropVideoView(Context context) {
        super(context);
        init(context);
    }

    public CropVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropVideoView, 0, 0);

        try {
            mGuidelines = ta.getInteger(R.styleable.CropVideoView_guidelines, 1);
            mFixAspectRatio = ta.getBoolean(R.styleable.CropVideoView_fixAspectRatio, false);
            mAspectRatioX = ta.getInteger(R.styleable.CropVideoView_aspectRatioX, 1);
            mAspectRatioY = ta.getInteger(R.styleable.CropVideoView_aspectRatioY, 1);
        } finally {
            ta.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_crop, this, true);
        mPlayerView = v.findViewById(R.id.playerView);
        mCropView = v.findViewById(R.id.cropView);
        mCropView.setInitialAttributeValues(mGuidelines, mFixAspectRatio, mAspectRatioX, mAspectRatioY);
    }

    protected void onSizeChanged(int newWidth, int newHeight, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged newWidth:" + newWidth + " newHeight:" + newHeight + "oldw:" + oldw + " oldh:" + oldh);
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (mVideoRotationDegrees == 90 || mVideoRotationDegrees == 270) {
            if (mVideoWidth >= mVideoHeight) {
                lp.width = (int) (newHeight * (1.0f * mVideoHeight / mVideoWidth));
                lp.height = newHeight;
            } else {
                lp.width = newWidth;
                lp.height = (int) (newWidth * (1.0f * mVideoWidth / mVideoHeight));
            }
        } else {
            if (mVideoWidth >= mVideoHeight) {
                lp.width = newWidth;
                lp.height = (int) (newWidth * (1.0f * mVideoHeight / mVideoWidth));
            } else {
                lp.width = (int) (newHeight * (1.0f * mVideoWidth / mVideoHeight));
                lp.height = newHeight;
            }
        }

        setLayoutParams(lp);
        Rect rect = new Rect(0, 0, lp.width, lp.height);
        mCropView.setBitmapRect(rect);
        mCropView.resetCropOverlayView();
    }

    public void setPlayer(SimpleExoPlayer player) {
        mPlayerView.setPlayer(player);
        mCropView.resetCropOverlayView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPlayerView.setPlayer(null);
    }

    public Rect getCropRect() {
        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();
        Rect result = new Rect();

        if (mVideoRotationDegrees == 90 || mVideoRotationDegrees == 270) {

            result.left = (int) (left * mVideoHeight / getWidth());
            result.right = (int) (right * mVideoHeight / getWidth());
            result.top = (int) (top * mVideoWidth / getHeight());
            result.bottom = (int) (bottom * mVideoWidth / getHeight());

            result.right = result.right - result.left;
            result.bottom = result.bottom - result.top;

        } else {
            result.left = (int) (left * mVideoWidth / getWidth());
            result.right = (int) (right * mVideoWidth / getWidth());
            result.top = (int) (top * mVideoHeight / getHeight());
            result.bottom = (int) (bottom * mVideoHeight / getHeight());

            result.right = result.right - result.left;
            result.bottom = result.bottom - result.top;
        }

        Log.d(TAG, "getCropRect mVideoRotationDegrees" + mVideoRotationDegrees);
        Log.d(TAG, "getCropRect mVideoWidth:" + mVideoWidth + " mVideoHeight:" + mVideoHeight + " getHeight:" + getHeight() + " getWidth:" + getWidth());
        Log.d(TAG, "getCropRect original left:" + left + " top:" + top + " right:" + right + " bottom" + bottom);
        Log.d(TAG, "getCropRect left:" + result.left + " top:" + result.top + " right:" + result.right + " bottom" + result.bottom);
        return result;
    }

    public void setFixedAspectRatio(boolean fixAspectRatio) {
        mCropView.setFixedAspectRatio(fixAspectRatio);
    }

    public void setAspectRatio(int aspectRatioX, int aspectRatioY) {
        mAspectRatioX = aspectRatioX;
        mAspectRatioY = aspectRatioY;
        mCropView.setAspectRatioX(this.mAspectRatioX);
        mCropView.setAspectRatioY(this.mAspectRatioY);
    }

    public void initBounds(int videoWidth, int videoHeight, int rotationDegrees) {
        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;
        mVideoRotationDegrees = rotationDegrees;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//
//    }
}
