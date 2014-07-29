package com.zhixin.dialog;

import com.zhixin.R;
import com.zhixin.settings.SettingValues;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class InstructionDialog extends Dialog{
	private Context context;

	private ImageView instructionDialogMainImage;

	private String instructionCate;

	public InstructionDialog(Context context,String instructionCate) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		setContentView(R.layout.customui_instruction_dialog);

		this.context = context;

		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.01f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);

		instructionDialogMainImage = (ImageView) this
				.findViewById(R.id.instructionDialogMainImage);
		
		int drawableResource = 0;
		this.instructionCate = instructionCate;
		
		
		if (instructionCate.equals(SettingValues.INSTRUCTION_BAOBAO_MARKET)){
			drawableResource = R.drawable.instruction_page_baobao_market;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_DUIJIANG)){
			drawableResource = R.drawable.instruction_page_duijiang;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_FRIEND_ANSWER)){
			drawableResource = R.drawable.instruction_page_friend_answer;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_FRIEND_RECOMMEND)){			
			drawableResource = R.drawable.instruction_page_friend_recommend;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QU_FRIEND)){
			drawableResource = R.drawable.instruction_page_qu_friend;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QUCESHI_ANSWER)){
			drawableResource = R.drawable.instruction_page_quceshi_answer;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QUCESHI_LIST1)){
			drawableResource = R.drawable.instruction_page_quceshi_list;	
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QUCESHI_LIST2)){
			drawableResource = R.drawable.instruction_page_quceshi_list1;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QUHUATI)){
			drawableResource = R.drawable.instruction_page_quhuati;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QUSHEJIAO)){
			drawableResource = R.drawable.instruction_page_qushejiao;			
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QUSHOUYE1)){
			drawableResource = R.drawable.instruction_page_qushouye_firstin;
		}else if (instructionCate.equals(SettingValues.INSTRUCTION_QUSHOUYE2)){
			drawableResource = R.drawable.instruction_page_qushouye;
		}
		
		instructionDialogMainImage.setImageResource(drawableResource);
		
	
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.dismiss();
		if (instructionCate != null) {
			new Thread(instructionViewedAction).start();
		}
		return false;
	}

	private Runnable instructionViewedAction = new Runnable() {
		@Override
		public void run() {
			SharedPreferences sharedPref = context.getSharedPreferences(
					SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putBoolean(instructionCate, false);
			editor.commit();
		}
	};

}
