package com.qubaopen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.qubaopen.R;

/**
 * Created by duel on 14-3-26.
 */
public class SelectAddressAdapter extends CursorAdapter {

	private Context context;
	
	private int selectedId;
	
	public int getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(int selectedId) {
		this.selectedId = selectedId;
	}

	public SelectAddressAdapter(Context context, Cursor cursor, int selectedId) {
		super(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER);
		this.context = context;
		this.selectedId = selectedId;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.adapter_select_address_item,
				parent, false);
		return updatingContentInView(rowView, cursor);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		updatingContentInView(view, cursor);
	}

	@SuppressLint("ResourceAsColor")
	private View updatingContentInView(final View view, Cursor cursor) {

		TextView txtNameUserAddressItem = (TextView) view
				.findViewById(R.id.txt_user_address_item_name);
		txtNameUserAddressItem.setText(cursor.getString(cursor
				.getColumnIndex("name")) + "");

		TextView txtPhoneUserAddressItem = (TextView) view
				.findViewById(R.id.txt_user_address_item_phone);
		txtPhoneUserAddressItem.setText(cursor.getString(cursor
				.getColumnIndex("phone")) + "");

		TextView txtAddressUserAddressItem = (TextView) view
				.findViewById(R.id.txt_user_address_item_address);
		
		ToggleButton btnSelected = (ToggleButton) view
				.findViewById(R.id.btn_selected_address);
		final Integer Id = cursor.getInt(cursor.getColumnIndex("dzId"));
		if (selectedId == Id) {
			btnSelected.setChecked(true);
		}else {
			btnSelected.setChecked(false);
		}
		btnSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					setSelectedId(Id);
					notifyDataSetChanged();
				}
			}
		});

		

		String sfmc = cursor.getString(cursor.getColumnIndex("sfmc"));
		String csmc = cursor.getString(cursor.getColumnIndex("csmc"));
		String dqmc = cursor.getString(cursor.getColumnIndex("dqmc"));
		String address = cursor.getString(cursor.getColumnIndex("address"));

		String detail = "";
		if (sfmc != null) {
			detail += sfmc;
			if (csmc != null) {
				detail += "," + csmc;
				if (dqmc != null) {
					detail += "," + dqmc;
				}
			}
		}
		if (address != null) {
			detail = "";
			detail += address;
			if (detail.length() > 18) {
				detail = detail.substring(0, 18) + "...";
			}
		}

		txtAddressUserAddressItem.setText(detail);

		return view;

	}

}
