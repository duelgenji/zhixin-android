package com.qubaopen.activity;

import com.qubaopen.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AnalysisNotCompletedFragment extends Fragment {
	private String designation;
	private Double percent;
	private TextView analysisPercent;
	private TextView analysisDesination;
	private TextView analysisContent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = this.getArguments(); 
		designation = bundle.getString("designation");
		percent = bundle.getDouble("percent");
		View view = inflater.inflate(R.layout.fragment_analysis_uncompleted, null);
		analysisPercent = (TextView) view.findViewById(R.id.ananlysis_percent);
		analysisPercent.setText(percent + "%");
		analysisDesination = (TextView) view.findViewById(R.id.ananlysis_designation);
		analysisDesination.setText(designation);
		analysisContent = (TextView) view.findViewById(R.id.analysis_content);
//		analysisContent.setText("");
		return view;
	}
}
