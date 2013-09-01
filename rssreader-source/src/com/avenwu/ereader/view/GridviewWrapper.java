package com.avenwu.ereader.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.HashMap;

/**
 * Created by Aven on 13-9-1.
 */
public class GridviewWrapper {
    private GridView gv;
    private AdapterWrapper adapterWrapper;
    private HashMap<HViewType, View> mHFViewMap = new HashMap<HViewType, View>();

    private enum HViewType {
        HEADER, FOOTER, CONTENT
    }

    public GridviewWrapper(GridView gv) {
        this.gv = gv;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapterWrapper = new AdapterWrapper(adapter);
        gv.setAdapter(adapterWrapper);
    }

    public void addHeaderView(View header) {
        mHFViewMap.put(HViewType.HEADER, header);
    }

    public void addFooterView(View footer) {
        mHFViewMap.put(HViewType.FOOTER, footer);
    }

    public void setNumColumns(int columnNumber) {
        gv.setNumColumns(columnNumber);
    }

    public int getNumColumns() {
        return gv.getNumColumns();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        gv.setOnItemClickListener(new ItemClickListener(onItemClickListener));
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        private AdapterView.OnItemClickListener listener;

        public ItemClickListener(AdapterView.OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            HViewType clickedViewType = HViewType.CONTENT;
            if (mHFViewMap.isEmpty())
                listener.onItemClick(adapterView, view, i, l);
            else
                clickedViewType = (i == 0 && mHFViewMap.containsKey(HViewType.HEADER)) ? HViewType.HEADER : (i == adapterWrapper.getCount()-1 && mHFViewMap.containsKey(HViewType.FOOTER)) ? HViewType.FOOTER : HViewType.CONTENT;
            if (clickedViewType.equals(HViewType.HEADER)) {
                // header clicked
            } else if (clickedViewType.equals(HViewType.FOOTER)) {
                // footer clicked
            } else {
                if (mHFViewMap.containsKey(HViewType.HEADER)) {
                    i--;
                    l--;
                }
                listener.onItemClick(adapterView, view, i, l);
            }

        }
    }

    public void notifyDataSetChanged() {
        adapterWrapper.notifyDataSetChanged();
    }

    private class AdapterWrapper extends BaseAdapter {
        private BaseAdapter adapter;
        private HViewType viewType;

        public AdapterWrapper(BaseAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getCount() {
            return adapter.getCount() + mHFViewMap.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (mHFViewMap.isEmpty())
                return adapter.getView(i, view, viewGroup);
            else
                viewType = (i == 0 && mHFViewMap.containsKey(HViewType.HEADER)) ? HViewType.HEADER : (i == adapter.getCount() && mHFViewMap.containsKey(HViewType.FOOTER)) ? HViewType.FOOTER : HViewType.CONTENT;
            if (viewType.equals(HViewType.HEADER)) {
                view = mHFViewMap.get(HViewType.HEADER);
            } else if (viewType.equals(HViewType.FOOTER)) {
                view = mHFViewMap.get(HViewType.FOOTER);
            } else {
                if (mHFViewMap.containsKey(HViewType.HEADER))
                    i--;
                view = adapter.getView(i, view, viewGroup);
            }
            return view;
        }

        @Override
        public Object getItem(int i) {
            return adapter.getItem(i);
        }

        @Override
        public long getItemId(int i) {
            return adapter.getItemId(i);
        }
    }
}
