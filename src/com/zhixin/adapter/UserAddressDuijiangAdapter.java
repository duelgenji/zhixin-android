package com.zhixin.adapter;

import com.zhixin.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by duel on 14-3-26.
 */
public class UserAddressDuijiangAdapter extends CursorAdapter {


    private Context context;
    private Cursor cursor;
    private int checkedId;
    private ViewGroup parent;


    public UserAddressDuijiangAdapter(Context context, Cursor cursor) {
        super(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER);
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        this.parent=parent;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_user_address_item, parent,
                false);

        return updatingContentInView(rowView,cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        updatingContentInView(view,cursor);

    }
    private View updatingContentInView(final View view, Cursor cursor) {
        TextView txtNameUserAddressItem=(TextView) view.findViewById(R.id.txtNameUserAddressItem);
        txtNameUserAddressItem.setText(cursor.getString(cursor
                .getColumnIndex("name")) + "");
        TextView txtPhoneUserAddressItem=(TextView) view.findViewById(R.id.txtPhoneUserAddressItem);
        txtPhoneUserAddressItem.setText(cursor.getString(cursor
                .getColumnIndex("phone")) + "");

        TextView txtAddressUserAddressItem=(TextView) view.findViewById(R.id.txtAddressUserAddressItem);

        ImageView imgIsDefault=(ImageView) view.findViewById(R.id.imgIsDefault);

        Integer iDzId=cursor.getInt(cursor.getColumnIndex("dzId"));

        String sfmc=cursor.getString(cursor.getColumnIndex("sfmc"));
        String csmc=cursor.getString(cursor.getColumnIndex("csmc"));
        String dqmc=cursor.getString(cursor.getColumnIndex("dqmc"));
        String address=cursor.getString(cursor.getColumnIndex("address"));
        String isDefault=cursor.getString(cursor.getColumnIndex("isDefault"));
        String detail="";
        if(sfmc!=null){
            detail+=sfmc;
            if(csmc!=null){
                detail+=","+csmc;
                if(dqmc!=null){
                    detail+=","+dqmc;
                }
            }
        }
        if(address!=null){
            if(!detail.equals("")){
                detail+=",";
            }
            detail+=address;
        }

        txtAddressUserAddressItem.setText(detail);

        if (isDefault != null) {
            if(isDefault.equals("1")){
                imgIsDefault.setVisibility(View.VISIBLE);
                checkedId=iDzId;
            }else{
                imgIsDefault.setVisibility(View.INVISIBLE);
            }
        }

        view.setOnClickListener(new UserAddressClickListener(iDzId));

        return view;

    }



    public int getCheckedDzId(){
        return checkedId;
    }

    private class UserAddressClickListener implements View.OnClickListener {
        private int dzId;

        public UserAddressClickListener(int dzId) {
            this.dzId = dzId;
        }
        @Override
        public void onClick(View v) {

            for(int i=0;i<parent.getChildCount();i++){
                if(parent.getChildAt(i)!=null) {
                    parent.getChildAt(i).findViewById(R.id.imgIsDefault).setVisibility(View.INVISIBLE);
                }
            }
            v.findViewById(R.id.imgIsDefault).setVisibility(View.VISIBLE);
            checkedId=dzId;

        }
    }

}
