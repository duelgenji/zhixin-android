package com.qubaopen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qubaopen.R;

public class AnalysisCompletedFragment extends Fragment implements OnClickListener {
	private TextView analysisReport;
	private TextView btnReport;
	private AnalysisCharacterActivity analysisCharacterActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.analysisCharacterActivity = (AnalysisCharacterActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_analysis_completed, null);
		analysisReport = (TextView) view.findViewById(R.id.analysis_report);
//		analysisReport.setText("");
		btnReport = (TextView) view.findViewById(R.id.btn_report);
		btnReport.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.btn_report:
			Intent intent = new Intent(analysisCharacterActivity,SelectAddressActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;

		default:
			break;
		}
	}
}
