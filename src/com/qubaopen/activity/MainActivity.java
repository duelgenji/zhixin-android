package com.qubaopen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import cn.sharesdk.framework.ShareSDK;

import com.qubaopen.R;
import com.qubaopen.customui.TabBarLayout;
import com.qubaopen.datasynservice.MainMenuService;
import com.qubaopen.dialog.InstructionDialog;
import com.qubaopen.service.UploadUserJpushAliasService;
import com.qubaopen.settings.MyApplication;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.LoadAddressUtil;
import com.qubaopen.utils.UpdateManager;

public class MainActivity extends FragmentActivity {
	private TabBarLayout zhijiTabBar;
	private TabBarLayout xinliMapTabBar;
	private TabBarLayout zhibiTabBar;
	private TabBarLayout messageTabBar;
	private TabBarLayout meTabBar;

	private MainMenuService service;

	private UpdateManager updateManager;

	private FragmentManager fm;

	private int currentCheckedBtnId;
	private String currentFragmentName;
	private View currentBtn;

	private MainActivity _this;

	private class FooterAction implements View.OnClickListener {

		@Override
		synchronized public void onClick(final View view) {

			TabBarLayout opView = (TabBarLayout) view;
			boolean checked = ((TabBarLayout) view).isChecked();
			if (!checked) {
				currentFragmentName = fm.getBackStackEntryAt(
						fm.getBackStackEntryCount() - 1).getName();
				currentCheckedBtnId = view.getId();
				currentBtn = view;
//				Log.i("current fragment name", currentFragmentName);

				Fragment rootFg = fm
						.findFragmentByTag(SettingValues.FRAGMENT_ROOT_FRAGMENT);
				FragmentTransaction transaction;

				_this.setAllFooterBtnEnableFalse();
				switch (opView.getId()) {
				case R.id.zhijiBtn:

					if (fm.getBackStackEntryCount() > 1) {
						fm.addOnBackStackChangedListener(changeBackstatckListener);
						fm.popBackStack();
					}

					break;
				case R.id.xinliMapBtn:

					if (!currentFragmentName
							.equals(SettingValues.FRAGMENT_QUDUIJIANG_FRAGMENT)) {
						if (fm.getBackStackEntryCount() > 1) {
							fm.addOnBackStackChangedListener(changeBackstatckListener);
							fm.popBackStack();
						} else {

							XinLiMapFragment xinliMapFragment = new XinLiMapFragment();

							transaction = fm.beginTransaction();

							transaction.add(R.id.contentFragment,
									xinliMapFragment,
									SettingValues.FRAGMENT_QUDUIJIANG_FRAGMENT);
							transaction
									.addToBackStack(SettingValues.FRAGMENT_QUDUIJIANG_FRAGMENT);

							transaction.hide(rootFg);

							fm.addOnBackStackChangedListener(fragmentAddedListener);
							transaction.commit();

							zhijiTabBar.setChecked(false);
							xinliMapTabBar.setChecked(true);
							messageTabBar.setChecked(false);
							zhibiTabBar.setChecked(false);
							meTabBar.setChecked(false);
						}

					}

					break;

				case R.id.messageBtn:

					if (!currentFragmentName
							.equals(SettingValues.FRAGMENT_MORE_FRAGMENT)) {

						if (fm.getBackStackEntryCount() > 1) {
							fm.addOnBackStackChangedListener(changeBackstatckListener);
							fm.popBackStack();
						} else {

							MessageFragment moreFragment = new MessageFragment();

							transaction = fm.beginTransaction();
							transaction.add(R.id.contentFragment, moreFragment,
									SettingValues.FRAGMENT_MORE_FRAGMENT);
							transaction
									.addToBackStack(SettingValues.FRAGMENT_MORE_FRAGMENT);

							transaction.hide(rootFg);

							fm.addOnBackStackChangedListener(fragmentAddedListener);

							transaction.commit();

							zhijiTabBar.setChecked(false);
							xinliMapTabBar.setChecked(false);
							messageTabBar.setChecked(true);
							zhibiTabBar.setChecked(false);
							meTabBar.setChecked(false);
						}

					}

					break;

				case R.id.zhibiBtn:

					if (!currentFragmentName
							.equals(SettingValues.FRAGMENT_QUSHEJIAO_FRAGMENT)) {
						if (fm.getBackStackEntryCount() > 1) {
							fm.addOnBackStackChangedListener(changeBackstatckListener);
							fm.popBackStack();
						} else {

							ZhibiFragment qushejiaoFragment = new ZhibiFragment();

							transaction = fm.beginTransaction();
							transaction.add(R.id.contentFragment,
									qushejiaoFragment,
									SettingValues.FRAGMENT_QUSHEJIAO_FRAGMENT);
							transaction
									.addToBackStack(SettingValues.FRAGMENT_QUSHEJIAO_FRAGMENT);

							transaction.hide(rootFg);

							fm.addOnBackStackChangedListener(fragmentAddedListener);

							transaction.commit();

							zhijiTabBar.setChecked(false);
							xinliMapTabBar.setChecked(false);
							messageTabBar.setChecked(false);
							zhibiTabBar.setChecked(true);
							meTabBar.setChecked(false);
						}

					}

					break;
				case R.id.meBtn:

					if (!currentFragmentName
							.equals(SettingValues.FRAGMENT_MORE_FRAGMENT)) {

						if (fm.getBackStackEntryCount() > 1) {
							fm.addOnBackStackChangedListener(changeBackstatckListener);
							fm.popBackStack();
						} else {

							MeFragment moreFragment = new MeFragment();

							transaction = fm.beginTransaction();
							transaction.add(R.id.contentFragment, moreFragment,
									SettingValues.FRAGMENT_MORE_FRAGMENT);
							transaction
									.addToBackStack(SettingValues.FRAGMENT_MORE_FRAGMENT);

							transaction.hide(rootFg);

							fm.addOnBackStackChangedListener(fragmentAddedListener);

							transaction.commit();

							zhijiTabBar.setChecked(false);
							xinliMapTabBar.setChecked(false);
							messageTabBar.setChecked(false);
							zhibiTabBar.setChecked(false);
							meTabBar.setChecked(true);

						}

					}

					break;
				}
			}

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		_this = this;
		service = new MainMenuService(MyApplication.getAppContext());
		new LoadDataTask().execute();

		// new loginAsyncTask().execute();
		startLogOnService();

		_this = this;
		fm = this.getSupportFragmentManager();
		fm.addOnBackStackChangedListener(initalFinishListener);

		MainmenuFragment newFragment = new MainmenuFragment();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.contentFragment, newFragment,
				SettingValues.FRAGMENT_ROOT_FRAGMENT);
		transaction.addToBackStack(SettingValues.FRAGMENT_ROOT_FRAGMENT);
		transaction.commit();

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				LoadAddressUtil.loadAddress();
				SharedPreferences sharedPref = MainActivity.this
						.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
								Context.MODE_PRIVATE);
				return sharedPref.getBoolean(SettingValues.INSTRUCTION_MAIN1,
						true);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					InstructionDialog qushouyeFirst = new InstructionDialog(
							MainActivity.this, SettingValues.INSTRUCTION_MAIN1);
					qushouyeFirst.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							new InstructionDialog(MainActivity.this,
									SettingValues.INSTRUCTION_MAIN2).show();
						}
					});

					qushouyeFirst.show();

				}
			}

		}.execute();

	}

	private class LoadDataTask extends AsyncTask<Object, Object, String> {

		@Override
		protected String doInBackground(Object[] params) {
			try {
				return service.newVersion();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String o) {
			if (o != null && !o.equals("") && !o.equals("isNew")) {
				updateManager = new UpdateManager(MainActivity.this);
				updateManager.checkUpdateInfo(o);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		setPushAliasSettings();
	}

	@Override
	public void onBackPressed() {
		if (isCurrentFragmentRootFragment()) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			ShareSDK.stopSDK(this);
		} else {
			zhijiTabBar.setChecked(true);
			xinliMapTabBar.setChecked(false);
			messageTabBar.setChecked(false);
			zhibiTabBar.setChecked(false);
			meTabBar.setChecked(false);
			fm.addOnBackStackChangedListener(fragmentAddedListener);
			super.onBackPressed();
		}

	}

	private boolean isCurrentFragmentRootFragment() {
		String currentFragmentName = fm.getBackStackEntryAt(
				fm.getBackStackEntryCount() - 1).getName();
		if (currentFragmentName.equals(SettingValues.FRAGMENT_ROOT_FRAGMENT)) {
			return true;
		} else {
			return false;
		}
	}

	private void startLogOnService() {
		Intent intent = new Intent(this, UploadUserJpushAliasService.class);
		startService(intent);
	}

	private void setPushAliasSettings() {
		// Intent intent = new Intent(this, SettingJpushAliasService.class);
		// startService(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// StatService.onPause(this);
	}

	private FragmentManager.OnBackStackChangedListener initalFinishListener = new OnBackStackChangedListener() {

		@Override
		public void onBackStackChanged() {
			FooterAction footerAction = new FooterAction();
			fm.removeOnBackStackChangedListener(this);

			zhijiTabBar = (TabBarLayout) _this.findViewById(R.id.zhijiBtn);
			zhijiTabBar.setChecked(true);
			zhijiTabBar.setOnClickListener(footerAction);

			xinliMapTabBar = (TabBarLayout) _this
					.findViewById(R.id.xinliMapBtn);
			xinliMapTabBar.setOnClickListener(footerAction);

			zhibiTabBar = (TabBarLayout) _this.findViewById(R.id.zhibiBtn);
			zhibiTabBar.setOnClickListener(footerAction);

			messageTabBar = (TabBarLayout) _this.findViewById(R.id.messageBtn);
			messageTabBar.setOnClickListener(footerAction);

			meTabBar = (TabBarLayout) _this.findViewById(R.id.meBtn);
			meTabBar.setOnClickListener(footerAction);

		}

	};

	private FragmentManager.OnBackStackChangedListener changeBackstatckListener = new OnBackStackChangedListener() {

		@Override
		public void onBackStackChanged() {
			Fragment rootFg = fm
					.findFragmentByTag(SettingValues.FRAGMENT_ROOT_FRAGMENT);
			FragmentTransaction transaction;
			switch (currentCheckedBtnId) {
			case R.id.zhijiBtn:
				fm.removeOnBackStackChangedListener(this);

				zhijiTabBar.setChecked(true);
				xinliMapTabBar.setChecked(false);
				messageTabBar.setChecked(false);
				zhibiTabBar.setChecked(false);
				meTabBar.setChecked(false);

				setAllFooterBtnEnableTrue();
				break;
			case R.id.xinliMapBtn:

				XinLiMapFragment xinliMapListFragment = new XinLiMapFragment();

				transaction = fm.beginTransaction();

				transaction.add(R.id.contentFragment, xinliMapListFragment,
						SettingValues.FRAGMENT_QUDUIJIANG_FRAGMENT);
				transaction
						.addToBackStack(SettingValues.FRAGMENT_QUDUIJIANG_FRAGMENT);

				transaction.hide(rootFg);
				fm.removeOnBackStackChangedListener(this);
				fm.addOnBackStackChangedListener(fragmentAddedListener);
				transaction.commit();

				zhijiTabBar.setChecked(false);
				xinliMapTabBar.setChecked(true);
				messageTabBar.setChecked(false);
				zhibiTabBar.setChecked(false);
				meTabBar.setChecked(false);

				break;

			case R.id.messageBtn:

				MessageFragment messageFragment = new MessageFragment();

				transaction = fm.beginTransaction();
				transaction.add(R.id.contentFragment, messageFragment,
						SettingValues.FRAGMENT_MORE_FRAGMENT);
				transaction
						.addToBackStack(SettingValues.FRAGMENT_MORE_FRAGMENT);

				transaction.hide(rootFg);

				fm.removeOnBackStackChangedListener(this);
				fm.addOnBackStackChangedListener(fragmentAddedListener);

				transaction.commit();

				zhijiTabBar.setChecked(false);
				xinliMapTabBar.setChecked(false);
				messageTabBar.setChecked(true);
				zhibiTabBar.setChecked(false);
				meTabBar.setChecked(false);

				break;

			case R.id.zhibiBtn:

				ZhibiFragment zhibiFragment = new ZhibiFragment();

				transaction = fm.beginTransaction();
				transaction.add(R.id.contentFragment, zhibiFragment,
						SettingValues.FRAGMENT_QUSHEJIAO_FRAGMENT);
				transaction
						.addToBackStack(SettingValues.FRAGMENT_QUSHEJIAO_FRAGMENT);

				transaction.hide(rootFg);

				fm.removeOnBackStackChangedListener(this);
				fm.addOnBackStackChangedListener(fragmentAddedListener);

				transaction.commit();

				zhijiTabBar.setChecked(false);
				xinliMapTabBar.setChecked(false);
				messageTabBar.setChecked(false);
				zhibiTabBar.setChecked(true);
				meTabBar.setChecked(false);

				break;

			case R.id.meBtn:

				MeFragment meFragment = new MeFragment();

				transaction = fm.beginTransaction();
				transaction.add(R.id.contentFragment, meFragment,
						SettingValues.FRAGMENT_MORE_FRAGMENT);
				transaction
						.addToBackStack(SettingValues.FRAGMENT_MORE_FRAGMENT);

				transaction.hide(rootFg);

				fm.removeOnBackStackChangedListener(this);
				fm.addOnBackStackChangedListener(fragmentAddedListener);

				transaction.commit();

				zhijiTabBar.setChecked(false);
				xinliMapTabBar.setChecked(false);
				messageTabBar.setChecked(false);
				zhibiTabBar.setChecked(false);
				meTabBar.setChecked(true);

				break;
			}
		}

	};

	private FragmentManager.OnBackStackChangedListener fragmentAddedListener = new OnBackStackChangedListener() {

		@Override
		public void onBackStackChanged() {

			fm.removeOnBackStackChangedListener(this);
			setAllFooterBtnEnableTrue();
		}

	};

	public void setAllFooterBtnEnableFalse() {
		zhijiTabBar.setEnabled(false);
		xinliMapTabBar.setEnabled(false);
		zhibiTabBar.setEnabled(false);
		messageTabBar.setEnabled(false);
		meTabBar.setEnabled(false);
	}

	public void setAllFooterBtnEnableTrue() {
		zhijiTabBar.setEnabled(true);
		xinliMapTabBar.setEnabled(true);
		zhibiTabBar.setEnabled(true);
		messageTabBar.setEnabled(true);
		meTabBar.setEnabled(true);
	}

}
