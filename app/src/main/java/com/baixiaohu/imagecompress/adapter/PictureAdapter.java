package com.baixiaohu.imagecompress.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baixiaohu.imagecompress.R;
import com.baixiaohu.imagecompress.bean.ImageFileBean;
import com.baixiaohu.imagecompress.utils.GlideUtils;
import com.baixiaohu.imagecompress.utils.PairHelp;

import utils.UiUtils;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private List<ImageFileBean> mData;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public PictureAdapter(Context context, List<ImageFileBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_picture_view, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ImageFileBean imageFileBean = mData.get(position);
        holder.mTvSize.setText(imageFileBean.imageSize);

        if (imageFileBean.isImage) {
            PairHelp.setViewTransitionName(holder.mIvPicture);
            GlideUtils.showImage(mContext, imageFileBean.imageFile, holder.mIvPicture, R.drawable.selector_picture_image, R.drawable.selector_picture_image);
        } else {
             GlideUtils.showImage(mContext, R.drawable.selector_picture_image, holder.mIvPicture);
           // ContextCompat.getDrawable(mContext, R.drawable.selector_picture_image);
        }
        holder.mIvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    if (imageFileBean.isImage) {
                        onItemClickListener.onPictureItemClick(v, position);
                    } else {
                        onItemClickListener.onAddItemClick(v, position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvPicture;
        private TextView mTvSize;

        ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mIvPicture = itemView.findViewById(R.id.iv_picture);
            DisplayMetrics metrics = UiUtils.getContext().getResources().getDisplayMetrics();
            ViewGroup.LayoutParams layoutParams = mIvPicture.getLayoutParams();
            layoutParams.width = metrics.widthPixels  / 3;
            layoutParams.height = metrics.widthPixels  / 3;
            mIvPicture.setLayoutParams(layoutParams);
            mTvSize = itemView.findViewById(R.id.tv_size);
        }
    }

    public interface OnItemClickListener {
        void onAddItemClick(View view, int position);

        void onPictureItemClick(View view, int position);

    }
}
