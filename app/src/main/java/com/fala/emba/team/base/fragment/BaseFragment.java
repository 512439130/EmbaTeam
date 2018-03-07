package com.fala.emba.team.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * 所有fragment的基类
 * Created by SoMustYY on 2017/7/4.
 */
public abstract class BaseFragment extends Fragment {
    protected LayoutInflater inflater;
    private ViewGroup container;
    private View contentView;
    private Context context;
    private Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();

    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        onCreateView(savedInstanceState);


        if (contentView == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return contentView;
    }

    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        contentView = null;
        container = null;
        inflater = null;
    }

    public Context getApplicationContext() {
        return context;
    }

    public void setContentView(int layoutResID) {
        setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View view) {
        contentView = view;
    }

    /**
     * 省去强转findviewById
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findView(int id) {
        if (contentView != null)
            return (T) contentView.findViewById(id);
        return null;
    }

    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
    }

}
