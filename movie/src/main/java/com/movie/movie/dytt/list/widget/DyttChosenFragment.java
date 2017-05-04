package com.movie.movie.dytt.list.widget;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.framework.base.BaseFragment;
import com.framework.utils.UIUtils;
import com.movie.R;
import com.movie.movie.dytt.list.model.DyttChosenModel;
import com.movie.movie.dytt.list.presenter.DyttChosenPresenter;
import com.movie.movie.dytt.list.presenter.DyttChosenPresenterImpl;
import com.movie.movie.dytt.list.view.DyttChosenView;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 2017/3/24
 */

public class DyttChosenFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, DyttChosenView {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DyttChosenPresenter presenter;
    private DyttChosenAdapter adapter;

    public static DyttChosenFragment newInstance() {
        return new DyttChosenFragment();
    }

    @Override
    protected void initById() {
        swipeRefreshLayout = getView(R.id.refresh_layout);
        recyclerView = getView(R.id.recyclerView);
    }

    @Override
    protected void initActivityCreated() {
        if (!isPrepared || !isVisible || isLoad) {
            return;
        }
        initRecyclerView();
        presenter = new DyttChosenPresenterImpl(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(this::onRefresh);
        setLoad();
    }

    private void initRecyclerView() {
        adapter = new DyttChosenAdapter(new ArrayList<>());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dytt_chosen;
    }

    @Override
    public void onRefresh() {
        presenter.netWorkRequest();
    }

    @Override
    public void netWorkSuccess(List<DyttChosenModel> data) {
        adapter.removeAll();
        adapter.addAll(data);
    }

    @Override
    public void netWorkError() {
        if (getActivity() != null) {
            UIUtils.INSTANCE.snackBar(getActivity().findViewById(R.id.coordinatorLayout), getString(R.string.network_error));
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }
}
