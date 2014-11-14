package com.qubaopen.dialog;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.activity.SelfPrefaceActivity;
import com.qubaopen.adapter.SelfRetestListAdapter;
import com.qubaopen.database.DbManager;
import com.qubaopen.domain.SelfList;

/**
 * Created by duel on 14-3-27.
 */
public class RetestDialog extends Dialog {

	private Context context;
	private List<SelfList> list;

	private ListView retestList;
	private SelfRetestListAdapter adapter;

	public RetestDialog(Context context, List<SelfList> list) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.list = list;
		init();
	}

	public void init() {
		this.setContentView(R.layout.dialog_retest);
		this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);

		retestList = (ListView) this.findViewById(R.id.self_retest_list);
		adapter = new SelfRetestListAdapter(context);
		adapter.setList(list);
		retestList.setAdapter(adapter);
		retestList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Integer selfId;
				if (view instanceof TextView) {
					selfId = (Integer) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.self_retest_title);
					selfId = (Integer) tv.getTag();
				}

				Intent intent;
				intent = new Intent(context, SelfPrefaceActivity.class);
				intent.putExtra(SelfPrefaceActivity.INTENT_SELF_ID, selfId);
				intent.putExtra(SelfPrefaceActivity.INTENT_SELF_ISRETEST, true);
				context.startActivity(intent);
				dismiss();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void deleteCurrentQuesitonAnswerByselfId(int selfId) {

		String sql = "delete from self_user_question_answer where questionId="
				+ selfId;
		DbManager.getDatabase().exeCustomerSql(sql);
	}
}
