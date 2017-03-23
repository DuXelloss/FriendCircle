package razerdp.github.com.photoselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.LinkedHashMap;
import java.util.List;

import razerdp.github.com.baselibrary.manager.localphoto.LPException;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.ui.SwitchActivityTransitionUtil;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.base.BaseTitleBarActivity;
import razerdp.github.com.baseuilib.widget.common.TitleBar;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * 图片选择器
 */

// TODO: 2017/3/23 扫描相册
public class PhotoSelectActivity extends BaseTitleBarActivity {

    private ViewHolder vh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselect);
        init();
        LocalPhotoManager.INSTANCE.scanImgAsync(new LocalPhotoManager.OnScanListener() {
            @Override
            public void onStart() {
                KLog.i();

            }

            @Override
            public void onFinish() {
                LinkedHashMap<String, List<LocalPhotoManager.ImageInfo>> info = LocalPhotoManager.INSTANCE.getLocalImages();
                KLog.i();
            }

            @Override
            public void onError(LPException e) {
                KLog.e(e);

            }
        });
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    private void init() {
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        setTitle("所有照片");
        setTitleMode(TitleBar.MODE_BOTH);
        setTitleLeftText("返回");
        setTitleRightText("取消");
        setTitleRightIcon(0);
    }

    private void initData() {

    }

    private void initView() {
        vh = new ViewHolder(this);
        vh.mPhotoEdit.setOnClickListener(onPhotoEditClickListener);
        vh.mPhotoPreview.setOnClickListener(onPhotoPreviewClickListener);
        vh.mFinish.setOnClickListener(onFinishClickListener);
    }


    //=============================================================click event
    private View.OnClickListener onPhotoEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIHelper.ToastMessage("编辑功能估计要有很长一段时间之后才能去完善这哦");
        }
    };

    private View.OnClickListener onPhotoPreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIHelper.ToastMessage("预览");

        }
    };

    private View.OnClickListener onFinishClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UIHelper.ToastMessage("finish");
        }
    };


    @Override
    public void onTitleLeftClick() {

    }

    @Override
    public void onTitleRightClick() {
        finish();
    }


    @Override
    public void finish() {
        super.finish();
        SwitchActivityTransitionUtil.transitionVerticalOnFinish(this);
    }

    class ViewHolder {
        RecyclerView mPhotoContent;
        TextView mPhotoEdit;
        TextView mPhotoPreview;
        TextView mSelectCount;
        TextView mFinish;

        ScaleAnimation scaleAnimation;

        public ViewHolder(PhotoSelectActivity activity) {
            mPhotoContent = activity.findView(R.id.photo_content);
            mPhotoEdit = activity.findView(R.id.photo_edit);
            mPhotoPreview = activity.findView(R.id.photo_preview);
            mSelectCount = activity.findView(R.id.photo_select_count);
            mFinish = activity.findView(R.id.photo_select_finish);
            buildAnima();
            setPhotoSlectCount(0);
        }

        private void buildAnima() {
            if (scaleAnimation == null) {
                scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(600);
                scaleAnimation.setInterpolator(new BounceInterpolator());
            }
        }

        public void setPhotoSlectCount(int count) {
            if (count <= 0) {
                mPhotoEdit.setTextColor(UIHelper.getResourceColor(R.color.text_gray));
                mPhotoPreview.setTextColor(UIHelper.getResourceColor(R.color.text_gray));
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_transparent));
                mSelectCount.clearAnimation();
                mSelectCount.setVisibility(View.GONE);

                ViewUtil.setViewsEnableAndClickable(false, false, mPhotoEdit, mPhotoPreview, mFinish);
            } else {
                //如果选择的照片大于一张，是不允许编辑的(iOS版微信的交互如此设计)
                mPhotoEdit.setTextColor(count == 1 ? UIHelper.getResourceColor(R.color.text_black) : UIHelper.getResourceColor(R.color.text_gray));
                mPhotoPreview.setTextColor(UIHelper.getResourceColor(R.color.text_black));
                mFinish.setTextColor(UIHelper.getResourceColor(R.color.wechat_green_bg));
                mSelectCount.setVisibility(View.VISIBLE);
                mSelectCount.clearAnimation();
                mSelectCount.setText(String.valueOf(count));
                mSelectCount.startAnimation(scaleAnimation);
                ViewUtil.setViewsEnableAndClickable(count == 1, count == 1, mPhotoEdit);
                ViewUtil.setViewsEnableAndClickable(true, true, mPhotoPreview, mFinish);
            }

        }
    }
}
