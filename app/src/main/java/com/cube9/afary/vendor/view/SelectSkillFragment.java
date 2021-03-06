package com.cube9.afary.vendor.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cube9.afary.MainActivity;
import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.vendor.model.SkillServicesPojo;
import com.cube9.afary.vendor.presenter.IVenderSignUpPresenter;
import com.cube9.afary.vendor.presenter.IVendorDetailsPresenter;
import com.cube9.afary.vendor.presenter.SkillListAdapter;
import com.cube9.afary.vendor.presenter.VendorDetailsImpl;
import com.cube9.afary.vendor.presenter.VendorSignUpPresenter;
import com.cube9.afary.vendor.vendor_dashbord.VendorHomeActivity;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cube9.afary.vendor.view.SelectCategoryFragment.MainServiceId;
import static com.cube9.afary.vendor.view.SelectDocumentFragment.file_doc;
import static com.cube9.afary.vendor.view.TakePhotoFragment.file_profile;

public class SelectSkillFragment extends Fragment implements IVenderSignUp.ISelectSkill, IVenderDetailsView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    View v;
    @BindView(R.id.rv_select_skills)
    RecyclerView rv_select_skills;

    JSONArray array;
    String select_cat_ids = "";

    @BindView(R.id.btn_submit)
    Button btn_submit;

    IVendorDetailsPresenter iVendorDetailsPresenter;


    List<SkillServicesPojo> skillServicesPojoListAll = new ArrayList<>();

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

        v = inflater.inflate(R.layout.fragment_select_skill, container, false);
        ButterKnife.bind(this, v);
        array = new JSONArray();
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_select_skills.setLayoutManager(verticalLayoutManager);
        vendorSignUpPresenter = new VendorSignUpPresenter(getActivity(), this, new SkillServicesPojo());
        vendorSignUpPresenter.requestSjillServices();
        iVendorDetailsPresenter = new VendorDetailsImpl(this, getActivity());

        return v;

    }

    @OnClick(R.id.btn_submit)
    public void onNextClick() {

        select_cat_ids="";
        if (skillServicesPojoListAll.size() > 0) {
            for (int i = 0; i < skillServicesPojoListAll.size(); i++) {
                if (skillServicesPojoListAll.get(i).isSeleted()) {
                    array.put(skillServicesPojoListAll.get(i).isSeleted());
                    select_cat_ids = select_cat_ids + skillServicesPojoListAll.get(i).getService_id() + ",";
                    Toast.makeText(getActivity(), "" + select_cat_ids, Toast.LENGTH_SHORT).show();


                }

            }
        }
        if (!select_cat_ids.isEmpty()) {
            CustomUtils.ShowDialog(getActivity());
            iVendorDetailsPresenter.submitVendorDetails(MainServiceId, PrefManager.getInstance(getActivity()).getUserId(), select_cat_ids, file_profile, file_doc);
        }


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
    public void getSubcategory(final List<SkillServicesPojo> skillServicesPojoList) {
        skillServicesPojoListAll.addAll(skillServicesPojoList);
        SkillListAdapter adapter = new SkillListAdapter(skillServicesPojoList, getActivity(), new RecyclerviewItemClickListener() {
            @Override
            public void onItemClick(View v, int position, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(getActivity(), "" + skillServicesPojoList.get(position).getService_name(), Toast.LENGTH_SHORT).show();
                    skillServicesPojoList.get(position).setSeleted(true);

                } else {
                    Toast.makeText(getActivity(), "" + isChecked, Toast.LENGTH_SHORT).show();
                    skillServicesPojoList.get(position).setSeleted(false);
                }

            }
        });
        rv_select_skills.setAdapter(adapter);

    }

    @Override
    public void responseFailureSubCategory(Throwable t) {
        CustomUtils.showToast("Server Error", getActivity(), MDToast.TYPE_ERROR);
    }

    @Override
    public void showProgressBar() {
        CustomUtils.ShowDialog(getActivity());

    }

    @Override
    public void hideProgressBar() {
        CustomUtils.DismissDialog();
    }

    @Override
    public void completeVendorRegistration(int result) {

        if (result==1)
        {
            CustomUtils.DismissDialog();
            Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),VendorHomeActivity.class));
            getActivity().finish();

        }
        else
        {
            CustomUtils.DismissDialog();
            Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void failure(Throwable t) {

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
