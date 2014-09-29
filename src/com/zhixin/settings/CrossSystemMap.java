package com.zhixin.settings;

import java.util.HashMap;

import android.R.integer;

import com.zhixin.R;

public class CrossSystemMap {
	private static final HashMap<String, Integer> crossSystemMap = new HashMap<String, Integer>();

	static {
		insertErrCode();
	}

	private static void insertErrCode() {
		crossSystemMap.put("r1", R.drawable.coordinate_system_01_r);
		crossSystemMap.put("r2", R.drawable.coordinate_system_02_r);
		crossSystemMap.put("r3", R.drawable.coordinate_system_03_r);
		crossSystemMap.put("r4", R.drawable.coordinate_system_04_r);
		crossSystemMap.put("r5", R.drawable.coordinate_system_05_r);
		crossSystemMap.put("r6", R.drawable.coordinate_system_06_r);
		crossSystemMap.put("r7", R.drawable.coordinate_system_07_r);
		crossSystemMap.put("r8", R.drawable.coordinate_system_08_r);
		
		crossSystemMap.put("r9", R.drawable.coordinate_system_09_r);
		crossSystemMap.put("r10", R.drawable.coordinate_system_10_r);
		crossSystemMap.put("r11", R.drawable.coordinate_system_11_r);
		crossSystemMap.put("r12", R.drawable.coordinate_system_12_r);
		crossSystemMap.put("r13", R.drawable.coordinate_system_13_r);
		crossSystemMap.put("r14", R.drawable.coordinate_system_14_r);
		crossSystemMap.put("r15", R.drawable.coordinate_system_15_r);
		crossSystemMap.put("r16", R.drawable.coordinate_system_16_r);
		
		crossSystemMap.put("r17", R.drawable.coordinate_system_17_r);
		crossSystemMap.put("r18", R.drawable.coordinate_system_18_r);
		crossSystemMap.put("r19", R.drawable.coordinate_system_19_r);
		crossSystemMap.put("r20", R.drawable.coordinate_system_20_r);
		crossSystemMap.put("r21", R.drawable.coordinate_system_21_r);
		crossSystemMap.put("r22", R.drawable.coordinate_system_22_r);
		crossSystemMap.put("r23", R.drawable.coordinate_system_23_r);
		crossSystemMap.put("r24", R.drawable.coordinate_system_24_r);
		
		crossSystemMap.put("r25", R.drawable.coordinate_system_25_r);
		crossSystemMap.put("r26", R.drawable.coordinate_system_26_r);
		crossSystemMap.put("r27", R.drawable.coordinate_system_27_r);
		crossSystemMap.put("r28", R.drawable.coordinate_system_28_r);
		crossSystemMap.put("r29", R.drawable.coordinate_system_29_r);
		crossSystemMap.put("r30", R.drawable.coordinate_system_30_r);
		crossSystemMap.put("r31", R.drawable.coordinate_system_31_r);
		crossSystemMap.put("r32", R.drawable.coordinate_system_32_r);
		
		

		crossSystemMap.put("y1", R.drawable.coordinate_system_01_y);
		crossSystemMap.put("y2", R.drawable.coordinate_system_02_y);
		crossSystemMap.put("y3", R.drawable.coordinate_system_03_y);
		crossSystemMap.put("y4", R.drawable.coordinate_system_04_y);
		crossSystemMap.put("y5", R.drawable.coordinate_system_05_y);
		crossSystemMap.put("y6", R.drawable.coordinate_system_06_y);
		crossSystemMap.put("y7", R.drawable.coordinate_system_07_y);
		crossSystemMap.put("y8", R.drawable.coordinate_system_08_y);
		
		crossSystemMap.put("y9", R.drawable.coordinate_system_09_y);
		crossSystemMap.put("y10", R.drawable.coordinate_system_10_y);
		crossSystemMap.put("y11", R.drawable.coordinate_system_11_y);
		crossSystemMap.put("y12", R.drawable.coordinate_system_12_y);
		crossSystemMap.put("y13", R.drawable.coordinate_system_13_y);
		crossSystemMap.put("y14", R.drawable.coordinate_system_14_y);
		crossSystemMap.put("y15", R.drawable.coordinate_system_15_y);
		crossSystemMap.put("y16", R.drawable.coordinate_system_16_y);
		
		crossSystemMap.put("y17", R.drawable.coordinate_system_17_y);
		crossSystemMap.put("y18", R.drawable.coordinate_system_18_y);
		crossSystemMap.put("y19", R.drawable.coordinate_system_19_y);
		crossSystemMap.put("y20", R.drawable.coordinate_system_20_y);
		crossSystemMap.put("y21", R.drawable.coordinate_system_21_y);
		crossSystemMap.put("y22", R.drawable.coordinate_system_22_y);
		crossSystemMap.put("y23", R.drawable.coordinate_system_23_y);
		crossSystemMap.put("y24", R.drawable.coordinate_system_24_y);
		
		crossSystemMap.put("y25", R.drawable.coordinate_system_25_y);
		crossSystemMap.put("y26", R.drawable.coordinate_system_26_y);
		crossSystemMap.put("y27", R.drawable.coordinate_system_27_y);
		crossSystemMap.put("y28", R.drawable.coordinate_system_28_y);
		crossSystemMap.put("y29", R.drawable.coordinate_system_29_y);
		crossSystemMap.put("y30", R.drawable.coordinate_system_30_y);
		crossSystemMap.put("y31", R.drawable.coordinate_system_31_y);
		crossSystemMap.put("y32", R.drawable.coordinate_system_32_y);


	}

	
	public static Integer getErrMessage(String color,int num){
		String errcode = null;
		int p = 0;
		if (num == 0) {
			p = 32;
		}else if (num == -1) {
			p = 31;
		}else if (num == 33) {
			p = 1;
		}else if (num == 34) {
			p = 2;
		}else {
			p = num;
		}
		errcode = color + p;
		return crossSystemMap.get(errcode);
		
	}
}
