package com.cube9.afary.vendor.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cube9.afary.MainActivity;
import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.vendor.model.SkillServicesPojo;
import com.cube9.afary.vendor.presenter.SkillListAdapter;
import com.cube9.afary.vendor.presenter.VendorSignUpPresenter;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectSkillFragment extends Fragment implements IVenderSignUp.ISelectSkill {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    View v;
    @BindView(R.id.rv_select_skills)
    RecyclerView rv_select_skills;


    VendorSignUpPresenter vendorSignUpPresenter;
   public SelectSkillFragment() {

    }


    public static SelectSkillFragment newInstance(String param1, String param2) {
        SelectSkillFragment fragment = new SelectSkillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_select_skill, container, false);
        ButterKnife.bind(this,v);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_select_skills.setLayoutManager(verticalLayoutManager);
        vendorSignUpPresenter=new VendorSignUpPresenter(getActivity(),this,new SkillServicesPojo());
        vendorSignUpPresenter.requestSjillServices();
       return v;
   }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showProgressDailog() {

    }

    @Override
    public void hideProgressDailog() {

    }

    @Override
    public void getSubcategory(List<SkillServicesPojo> skillServicesPojoList) {
        SkillListAdapter adapter=new SkillListAdapter(skillServicesPojoList,getActivity());
        rv_select_skills.setAdapter(adapter);

    }

    @Override
    public void responseFailureSubCategory(Throwable t) {
        CustomUtils.showToast("Server Error",getActivity(),MDToast.TYPE_ERROR);
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
