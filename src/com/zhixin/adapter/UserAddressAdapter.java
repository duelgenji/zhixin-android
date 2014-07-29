package com.zhixin.adapter;

import com.zhixin.R;
import com.zhixin.activity.UserInfoAddressModifyActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by duel on 14-3-26.
 */
public class UserAddressAdapter  extends CursorAdapter {


    private Context context;
    private Cursor cursor;


    public UserAddressAdapter(Context context, Cursor cursor) {
        super(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER);
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

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
        //Log.i("add detail:",detail);
        if(address!=null){
//            if(!detail.equals("")){
//                detail+=",";
//            }
            detail="";
            detail+=address;
            if(detail.length()>18){
                detail=detail.substring(0,18)+"...";
            }
        }

        txtAddressUserAddressItem.setText(detail);

        if (isDefault != null) {
            if(isDefault.equals("1")){
                imgIsDefault.setVisibility(View.VISIBLE);
            }else{
                imgIsDefault.setVisibility(View.INVISIBLE);
            }
        }

        view.setOnClickListener(new UserAddressClickListener(iDzId));

        return view;

    }




    private class UserAddressClickListener implements View.OnClickListener {
        private int dzId;

        public UserAddressClickListener(int dzId) {
            this.dzId = dzId;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, UserInfoAddressModifyActivity.class);

            intent.putExtra(UserInfoAddressModifyActivity.INTENT_ADDRESS_ID,dzId);

            context.startActivity(intent);

        }
    }

}
