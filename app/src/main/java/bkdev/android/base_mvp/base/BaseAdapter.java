package bkdev.android.base_mvp.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;



public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    public BaseAdapter(@NonNull Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    protected Resources getResources() {
        return mContext.getResources();
    }

    protected String getString(int resId) {
        return mContext.getString(resId);
    }

    protected String getString(int resId, Object... objects) {
        return mContext.getString(resId, objects);
    }

    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(@NonNull ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * Override onBindViewHolder to support OnItemClick and OnItemLongClick listener.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mItemClickListener != null) {
            RxView.clicks(viewHolder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(aVoid -> mItemClickListener.onItemClick(viewHolder.itemView, i));
        }
    }
}
