package com.qubaopen.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.qubaopen.R;
import com.qubaopen.daos.AddressDao;
import com.qubaopen.domain.AddressCs;
import com.qubaopen.domain.AddressDq;
import com.qubaopen.domain.AddressSf;

/**
 * Created by duel on 14-3-27.
 */
public class AddressDialog extends Dialog implements View.OnClickListener {

	private AddressSf sfObj;
	private AddressCs csObj;
	private AddressDq dqObj;

	private Context context;

	private boolean isValueGet;

	private Spinner spinnerAddressPickerSF;
	private Spinner spinnerAddressPickerCS;
	private Spinner spinnerAddressPickerDQ;
	private Button btnOk;
	private Button btnCancel;

	private ArrayAdapter<AddressSf> sfAdapter;
	private ArrayAdapter<AddressCs> csAdapter;
	private ArrayAdapter<AddressDq> dqAdapter;

	private AddressDao addressDao;

	private AddressSf emptySf;
	private AddressCs emptyCs;
	private AddressDq emptyDq;

	private OnSfSelectedListener mSfSelectedListener;

	private OnCsSelectedListener mCsSelectedListener;

	private OnDqSelectedListener mOnDqSelectedListener;

	public interface OnSfSelectedListener {
		public void onAddressSelected();
	}

	public interface OnCsSelectedListener {
		public void onAddressSelected();
	}

	public interface OnDqSelectedListener {
		public void onAddressSelected();

	}

	public AddressDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
	}

	public void init() {
		isValueGet = false;
		addressDao = new AddressDao();

		this.setContentView(R.layout.dialog_pick_address);
		this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);

		spinnerAddressPickerSF = (Spinner) this
				.findViewById(R.id.spinnerAddressPickerSF);
		spinnerAddressPickerCS = (Spinner) this
				.findViewById(R.id.spinnerAddressPickerCS);
		spinnerAddressPickerDQ = (Spinner) this
				.findViewById(R.id.spinnerAddressPickerDQ);

		spinnerAddressPickerCS.setEnabled(false);
		spinnerAddressPickerDQ.setEnabled(false);

		btnOk = (Button) this.findViewById(R.id.btnAddressPickerOK);
		btnCancel = (Button) this.findViewById(R.id.btnAddressPickerCancel);

		emptySf = new AddressSf();
		emptySf.setSfdm(null);
		emptySf.setMc("选择省份");

		emptyCs = new AddressCs();
		emptyCs.setSfdm(null);
		emptyCs.setCsdm(null);
		emptyCs.setMc("选择城市");

		emptyDq = new AddressDq();
		emptyDq.setCsdm(null);
		emptyDq.setDqdm(null);
		emptyDq.setMc("选择地区");

		initSf();

		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	private void initSf() {
		List<AddressSf> list = addressDao.getAddressSf();
		list.add(0, emptySf);
		sfAdapter = new ArrayAdapter<AddressSf>(context,
				R.layout.textview_address_picker, list);
		spinnerAddressPickerSF.setAdapter(sfAdapter);

		List<AddressCs> listnewcs = new ArrayList<AddressCs>();
		listnewcs.add(emptyCs);
		csAdapter = new ArrayAdapter<AddressCs>(context,
				R.layout.textview_address_picker, listnewcs);
		spinnerAddressPickerCS.setAdapter(csAdapter);

		List<AddressDq> listnewdq = new ArrayList<AddressDq>();
		listnewdq.add(emptyDq);
		dqAdapter = new ArrayAdapter<AddressDq>(context,
				R.layout.textview_address_picker, listnewdq);
		spinnerAddressPickerDQ.setAdapter(dqAdapter);

		spinnerAddressPickerSF
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> apaterView,
							View view, int position, long arg3) {
						AddressSf sf = sfAdapter.getItem(position);
						if (sf.getSfdm() != null) {
							List<AddressCs> csList = addressDao.getAddressCs(sf
									.getSfdm());
							csList.add(0, emptyCs);
							if (csAdapter == null) {
								csAdapter = new ArrayAdapter<AddressCs>(
										context,
										R.layout.textview_address_picker,
										csList);
								spinnerAddressPickerCS.setAdapter(csAdapter);
							} else {
								csAdapter.clear();
								for (AddressCs object : csList) {
									csAdapter.add(object);
								}

							}
							spinnerAddressPickerCS
									.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(
												AdapterView<?> arg0, View arg1,
												int position, long arg3) {
											AddressCs cs = csAdapter
													.getItem(position);
											if (cs.getCsdm() != null) {
												List<AddressDq> listDq = addressDao
														.getAddressDq(cs
																.getCsdm());
												listDq.add(0, emptyDq);

												if (dqAdapter == null) {
													dqAdapter = new ArrayAdapter<AddressDq>(
															context,
															R.layout.textview_address_picker,
															listDq);
													spinnerAddressPickerDQ
															.setAdapter(dqAdapter);
												} else {
													dqAdapter.clear();

													for (AddressDq object : listDq) {

														dqAdapter.add(object);
													}

												}
												spinnerAddressPickerDQ
														.setOnItemSelectedListener(new OnItemSelectedListener() {

															@Override
															public void onItemSelected(
																	AdapterView<?> arg0,
																	View arg1,
																	int position,
																	long arg3) {
																AddressDq dq = dqAdapter
																		.getItem(position);
																if (dq.getDqdm() != null) {
																	dqObj = dq;

																} else {
																	dqObj = null;

																}
																// mOnDqSelectedListener.onAddressSelected();
															}

															@Override
															public void onNothingSelected(
																	AdapterView<?> arg0) {

															}

														});
												if (mOnDqSelectedListener != null) {
													mOnDqSelectedListener
															.onAddressSelected();
												}
												spinnerAddressPickerDQ
														.setEnabled(true);

												csObj = cs;

											} else {
												csObj = null;
												if (dqAdapter != null) {
													dqAdapter.clear();
													dqAdapter.add(emptyDq);
													dqObj = null;
												}
												spinnerAddressPickerDQ
														.setEnabled(false);

											}
											// mCsSelectedListener.onAddressSelected();
										}

										@Override
										public void onNothingSelected(
												AdapterView<?> arg0) {

										}

									});
							if (mCsSelectedListener != null) {
								mCsSelectedListener.onAddressSelected();
							}
							sfObj = sf;
							spinnerAddressPickerCS.setEnabled(true);
						} else {
							if (csAdapter != null) {
								csAdapter.clear();
								csAdapter.add(emptyCs);
								csObj = null;
							}
							spinnerAddressPickerCS.setEnabled(false);
							if (dqAdapter != null) {
								dqAdapter.clear();
								dqAdapter.add(emptyDq);
								dqObj = null;
							}

							spinnerAddressPickerDQ.setEnabled(false);

							sfObj = null;
						}
						// mSfSelectedListener.onAddressSelected();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		if (mSfSelectedListener != null) {
			mSfSelectedListener.onAddressSelected();
		}

	}

	public void setDefault(String sf, String cs, String dq) {
		if (StringUtils.isNotEmpty(sf)) {
			spinnerAddressPickerSF.setSelection(getPositionFromSfAdapter(sf));
			if (StringUtils.isNotEmpty(cs)) {
				spinnerAddressPickerCS
						.setSelection(getPositionFromCsAdapter(cs));
				if (StringUtils.isNotEmpty(dq)) {
					spinnerAddressPickerDQ
							.setSelection(getPositionFromDqAdapter(dq));
				} else {
					spinnerAddressPickerDQ.setSelection(0);
				}
			} else {
				spinnerAddressPickerCS.setSelection(0);
			}
		} else {
			spinnerAddressPickerSF.setSelection(0);
		}
	}

	public void setDefaultSf(String sf) {
		if (StringUtils.isNotEmpty(sf)) {
			spinnerAddressPickerSF.setSelection(getPositionFromSfAdapter(sf));
		} else {
			spinnerAddressPickerSF.setSelection(0);

		}
	}

	public void setDefaultCs(String cs) {
		if (StringUtils.isNotEmpty(cs)) {
			spinnerAddressPickerCS.setSelection(getPositionFromCsAdapter(cs));
		} else {
			spinnerAddressPickerCS.setSelection(0);
		}
	}

	public void setDefaultDq(String dq) {
		if (StringUtils.isNotEmpty(dq)) {
			spinnerAddressPickerDQ.setSelection(getPositionFromDqAdapter(dq));
		} else {
			spinnerAddressPickerDQ.setSelection(0);
		}
	}

	private int getPositionFromSfAdapter(String dm) {
		AddressSf sf;
		for (int i = 0; i < sfAdapter.getCount(); i++) {
			sf = sfAdapter.getItem(i);
			if (sf.getSfdm() != null && sf.getSfdm().equals(dm)) {
				return i;
			}
		}
		return 0;
	}

	private int getPositionFromCsAdapter(String dm) {
		AddressCs cs;
		for (int i = 0; i < csAdapter.getCount(); i++) {
			cs = csAdapter.getItem(i);
			if (cs.getCsdm() != null && cs.getCsdm().equals(dm)) {
				return i;
			}
		}
		return 0;
	}

	private int getPositionFromDqAdapter(String dm) {
		AddressDq dq;
		for (int i = 0; i < dqAdapter.getCount(); i++) {
			dq = dqAdapter.getItem(i);
			if (dq.getDqdm() != null && dq.getDqdm().equals(dm)) {
				return i;
			}

		}

		return 0;
	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.btnAddressPickerOK:

			isValueGet = true;
			this.dismiss();

			break;
		case R.id.btnAddressPickerCancel:
			isValueGet = false;
			this.dismiss();
			break;
		default:
			break;
		}
		v.setEnabled(true);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		isValueGet = false;
	}

	public void setmSfSelectedListener(OnSfSelectedListener mSfSelectedListener) {
		this.mSfSelectedListener = mSfSelectedListener;
	}

	public void setmCsSelectedListener(OnCsSelectedListener mCsSelectedListener) {
		this.mCsSelectedListener = mCsSelectedListener;
	}

	public void setmOnDqSelectedListener(
			OnDqSelectedListener mOnDqSelectedListener) {
		this.mOnDqSelectedListener = mOnDqSelectedListener;
	}

	public AddressSf getSfObj() {
		return sfObj;
	}

	public AddressCs getCsObj() {
		return csObj;
	}

	public AddressDq getDqObj() {
		return dqObj;
	}

	public boolean isValueGet() {
		return isValueGet;
	}

}
