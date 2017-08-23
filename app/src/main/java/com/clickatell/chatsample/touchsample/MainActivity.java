package com.clickatell.chatsample.touchsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.clickatell.chatsecure.androidsdk.ChatActivity;
import com.clickatell.chatsecure.androidsdk.data.model.ChatModel;
import com.clickatell.chatsecure.androidsdk.sdk2.PersistenceService;
import com.clickatell.chatsecure.androidsdk.sdk2.TouchSdk;
import com.clickatell.chatsecure.androidsdk.sdk2.api.ApiCall;
import com.clickatell.chatsecure.androidsdk.sdk2.api.Callback;
import com.clickatell.chatsecure.androidsdk.sdk2.model.Tenant;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRvTenants;
    private TenantsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btClearHistory).setOnClickListener(this);
        findViewById(R.id.btStartChat).setOnClickListener(this);
        mRvTenants = (RecyclerView) findViewById(R.id.rvTenants);
        mRvTenants.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TenantsAdapter(this);
        mRvTenants.setAdapter(mAdapter);
        fetchTenants();
    }

    private void fetchTenants() {
        ApiCall<List<Tenant>> apiCall = TouchSdk.service().apiService().tenants();
        apiCall.enqueue(new Callback<List<Tenant>>() {
            @Override
            public void onSuccess(@NonNull List<Tenant> tenants) {
                Log.d(TAG, "tenants " + Arrays.asList(tenants.toArray()));
                mAdapter.swapData(tenants);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "tenants call error", throwable);
            }
        });

        //To cancel request(e.g, when activity/fragment is no longer available)
        //apiCall.cancel();
    }

    private void startChatActivity(Tenant tenant) {
        ChatActivity.startActivity(this/*activity or fragment*/, tenant);
    }

    private void clearHistory() {
        PersistenceService persistenceService = TouchSdk.service().persistenceService();
        List<ChatModel> availableChats = persistenceService.availableChats();
        boolean isSuccessful = persistenceService.clearAllChats();
    }

    @Override
    public void onClick(View v) {
        Tenant tenant = mAdapter.getSelectedTenant();
        switch (v.getId()) {
            case R.id.btStartChat:
                if (tenant != null) {
                    startChatActivity(tenant);
                }
                break;
            case R.id.btClearHistory:
                clearHistory();
                break;
        }
    }
}
