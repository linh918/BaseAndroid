package bkdev.android.base_mvp.utils.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import bkdev.android.base_mvp.R;


class EndlessRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_FOOTER = Integer.MAX_VALUE - 1;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mWrapped;
    private EndlessRecyclerView.ViewState mViewState;

    private RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeRemoved(final int positionStart, final int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(final int fromPosition, final int toPosition, final int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition, itemCount);
        }

        @Override
        public void onItemRangeInserted(final int positionStart, final int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(final int positionStart, final int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }
    };

    public EndlessRecyclerAdapter(final RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, final EndlessRecyclerView.ViewState state) {
        mWrapped = adapter;
        mWrapped.registerAdapterDataObserver(mAdapterDataObserver);
        mViewState = state;
    }

    public RecyclerView.Adapter getWrapped() {
        return mWrapped;
    }

    public void updateState() {
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mWrapped.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mWrapped.onDetachedFromRecyclerView(recyclerView);
        mWrapped.unregisterAdapterDataObserver(mAdapterDataObserver);
    }

    @Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mWrapped.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        mWrapped.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(final RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        mWrapped.onViewRecycled(holder);
    }

    @Override
    public void setHasStableIds(final boolean hasStableIds) {
        mWrapped.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(final int position) {
        if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            return position;
        } else {
            return mWrapped.getItemId(position);
        }
    }

    @Override
    public int getItemCount() {
        return mWrapped.getItemCount() + getFooterCount();
    }

    @Override
    public int getItemViewType(final int position) {
        if (position == mWrapped.getItemCount()) {
            return VIEW_TYPE_FOOTER;
        }
        return mWrapped.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            return createFooterViewHolder(parent);
        } else {
            return mWrapped.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int type = getItemViewType(position);
        if (type == VIEW_TYPE_FOOTER) {
            bindFooterViewHolder(holder);
        } else {
            mWrapped.onBindViewHolder(holder, position);
        }
    }


    private int getFooterCount() {
        return (mViewState.getState() == EndlessRecyclerView.STATE_HIDE) ? 0 : 1;
    }

    private RecyclerView.ViewHolder createFooterViewHolder(final ViewGroup parent) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item_recycler_footer, parent, false);
        return new SimpleViewHolder(view);
    }

    private void bindFooterViewHolder(final RecyclerView.ViewHolder holder) {
        SimpleViewHolder footer = (SimpleViewHolder) holder;
        switch (mViewState.getState()) {
            case EndlessRecyclerView.STATE_SHOW:
                footer.rlFooterRecycler.setVisibility(View.VISIBLE);
                break;

            case EndlessRecyclerView.STATE_HIDE:
                footer.rlFooterRecycler.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlFooterRecycler;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            rlFooterRecycler = itemView.findViewById(R.id.rlFooterRecycler);
        }
    }
}
