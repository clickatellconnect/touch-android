package com.clickatell.chatsample.touchsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clickatell.chatsecure.androidsdk.sdk2.model.Tenant;

import java.util.List;

/**
 * Created by Alexey Sidorenko on 24.07.2017.
 */

class TenantsAdapter extends RecyclerView.Adapter<TenantsAdapter.TenantViewHolder> {
    private final LayoutInflater mInflater;
    private List<Tenant> mTenants;
    private View mSelectedItem;

    public TenantsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TenantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TenantViewHolder(mInflater.inflate(R.layout.tenant_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TenantViewHolder holder, int position) {
        Tenant tenant = mTenants.get(position);
        holder.bind(tenant);
    }

    @Override
    public int getItemCount() {
        return mTenants == null ? 0 : mTenants.size();
    }

    public void swapData(List<Tenant> tenants) {
        mTenants = tenants;
        notifyDataSetChanged();
    }

    class TenantViewHolder extends RecyclerView.ViewHolder {

        private final TextView vTitle;

        public TenantViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            vTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedItem != null){
                        mSelectedItem.setSelected(false);
                    }
                    mSelectedItem = v;
                    mSelectedItem.setSelected(true);
                }
            });
        }

        public void bind(Tenant tenant) {
            vTitle.setText(tenant.getTenantOrgName()            );
            vTitle.setTag(tenant);
        }
    }

    public Tenant getSelectedTenant(){
        if (mSelectedItem != null){
            return (Tenant) mSelectedItem.getTag();
        } else {
            return null;
        }
    }
}
