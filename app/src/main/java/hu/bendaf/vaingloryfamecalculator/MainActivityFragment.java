package hu.bendaf.vaingloryfamecalculator;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ExpandableLayoutListener {

    private static final String PREF_ROLE = "role";
    private static final String PREF_HEAT = "heat";

    final int[] roleIds = {R.id.tv_role_initiate, R.id.tv_role_member, R.id.tv_role_veteran, R.id.tv_role_leader};
    final int[] heatIds = {R.id.tv_heat_0, R.id.tv_heat_10, R.id.tv_heat_25};
    final int[] fameTableIds = {R.id.tv_fame_table_21, R.id.tv_fame_table_22, R.id.tv_fame_table_11, R.id.tv_fame_table_12};
    final int[][][] fames = /*Initiate*/{{{1, 2, 7, 10},      /*10*/ {1, 2, 7, 11},     /*25*/ {2, 3, 9, 13}},
                               /*Member*/{{18, 25, 50, 75},   /*10*/ {20, 27, 55, 82},  /*25*/ {25, 33, 75, 101}},
                              /*Veteran*/{{25, 35, 75, 100},  /*10*/ {27, 37, 82, 110}, /*25*/ {33, 45, 101, 135}},
                              /*Leader*/ {{35, 45, 100, 125}, /*10*/ {37, 50, 110, 138},/*25*/ {44, 56, 125, 168}}};

    private int mSelectedRole;
    private int mSelectedHeat;

    ArrayList<TextView> mRoleList = new ArrayList<>();
    ArrayList<TextView> mHeatList = new ArrayList<>();
    TextView tvRole;
    TextView tvHeat;
    private ExpandableRelativeLayout expLayoutRole;
    private ExpandableRelativeLayout expLayoutHeat;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);

        mSelectedRole = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(PREF_ROLE, -1);
        mSelectedHeat = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(PREF_HEAT, -1);

        // Role
        expLayoutRole = (ExpandableRelativeLayout) root.findViewById(R.id.exp_layout_role);
        setUpViews(root, mRoleList, roleIds);
        tvRole = (TextView) root.findViewById(R.id.tv_role_title);
        for(int i = 0; i < mRoleList.size(); i++) {
            final int pos = i;
            mRoleList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedRole(pos);
                }
            });
        }
        expLayoutRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedRole(-1);
            }
        });
        expLayoutRole.setListener(this);

        // Heat
        expLayoutHeat = (ExpandableRelativeLayout) root.findViewById(R.id.exp_layout_heat);
        setUpViews(root, mHeatList, heatIds);
        tvHeat = (TextView) root.findViewById(R.id.tv_heat_title);
        for(int i = 0; i < mHeatList.size(); i++) {
            final int pos = i;
            mHeatList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedHeat(pos);
                }
            });
        }
        expLayoutHeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedHeat(-1);
            }
        });
        expLayoutHeat.setListener(this);
        refreshTable();
        setTexts();
        return root;
    }

    @Override
    public void onAnimationStart() {
    }

    @Override
    public void onAnimationEnd() {
    }

    @Override
    public void onOpened() {
    }

    @Override
    public void onClosed() {
    }

    @Override
    public void onPreOpen() {
        setTexts();
    }

    @Override
    public void onPreClose() {
        setTexts();
    }

    public void onFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            RelativeLayout.LayoutParams headerParams = ((RelativeLayout.LayoutParams) tvRole.getLayoutParams());
            int headerHeight = tvRole.getHeight() + headerParams.topMargin + headerParams.bottomMargin;
            expLayoutRole.setClosePosition(headerHeight);
            expLayoutHeat.setClosePosition(headerHeight);
            if(mSelectedRole != -1) expLayoutRole.collapse();
            if(mSelectedHeat != -1) expLayoutHeat.collapse();

        }
    }

    private void setTexts() {
        if(mSelectedRole == -1) {
            tvRole.setText(getText(R.string.title_text_role));
        } else {
            tvRole.setText(mRoleList.get(mSelectedRole).getText().toString());
        }
        if(mSelectedHeat == -1) {
            tvHeat.setText(R.string.title_text_heat);
        } else {
            tvHeat.setText(mHeatList.get(mSelectedHeat).getText().toString());
        }
    }

    private void setSelectedRole(int pos) {
        mSelectedRole = pos;
        if(pos == -1) {
            expLayoutRole.expand();
//            tvRole.setTextColor(getResources().getColor(R.color.textColorSecondary));
        } else {
            expLayoutRole.toggle();
//            tvRole.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        refreshTable();
        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(PREF_ROLE, mSelectedRole).commit();
    }

    private void setSelectedHeat(int pos) {
        mSelectedHeat = pos;
        if(pos == -1) {
            expLayoutHeat.expand();
        } else expLayoutHeat.toggle();
        refreshTable();
        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(PREF_HEAT, mSelectedHeat).commit();
    }

    private void refreshTable() {
        for(int i = 0; i < fameTableIds.length; i++) {
            if(mSelectedRole != -1 && mSelectedHeat != -1) {
                ((TextView) root.findViewById(fameTableIds[i])).setText(String.format(
                        Locale.getDefault(), "%d", fames[mSelectedRole][mSelectedHeat][i]));
            } else {
                ((TextView) root.findViewById(fameTableIds[i])).setText(getString(R.string.mark_question));
            }
        }
    }

    private <T extends View> void setUpViews(View root, ArrayList<T> roleList, @IdRes int[] roleIds) {
        for(int id : roleIds) {
            roleList.add((T) root.findViewById(id));
        }
    }
}
