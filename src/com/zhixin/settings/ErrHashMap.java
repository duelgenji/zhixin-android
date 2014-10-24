package com.zhixin.settings;

import java.util.HashMap;

public class ErrHashMap {
	private static final HashMap<String, String> errhashMap = new HashMap<String, String>();

	static {
		insertErrCode();
	}

	private static void insertErrCode() {
		errhashMap.put("err000", "用户未登录");
		errhashMap.put("err001", "用户不存在");
		errhashMap.put("err002", "密码错误");
		errhashMap.put("err003", "手机号码为空或格式不正确");
		errhashMap.put("err004", "密码格式不正确");
		errhashMap.put("err005", "邮箱格式不正确");
		errhashMap.put("err006", "该用户已注册");
		errhashMap.put("err007", "输入验证码有误");
		errhashMap.put("err008", "还未获取过短信验证码");
		errhashMap.put("err009", "验证时间过短");
		errhashMap.put("err010", "验证次数过多");
		errhashMap.put("err011", "短信验证码发送失败");
		errhashMap.put("err012", "验证码为空");
		errhashMap.put("err013", "请重新申请验证码");
		errhashMap.put("err014", "缺少信息");
		errhashMap.put("err015", "原密码格式不正确");
		errhashMap.put("err016", "原密码不匹配");
		errhashMap.put("err017", "亲，您的年龄段没有合适的测评哦！");

		errhashMap.put("err101", "修改信息失败");
		errhashMap.put("err102", "上传头像失败");
		// errhashMap.put("err103", "系统无记录");
		// errhashMap.put("err104", "检测到数据量异常,如有问题请联系官方人员");
		// errhashMap.put("err105", "输入内容不能为空");

		errhashMap.put("err200", "身份证绑定失败");
		errhashMap.put("err201", "每月绑定次数达到上线");
		errhashMap.put("err202", "身份证格式不正确");
		errhashMap.put("err203", "身份证已被人绑定");
		errhashMap.put("err204", "本月已成功绑定，无法修改");
		// errhashMap.put("err205", "不符合答该问卷的条件");
		// errhashMap.put("err206", "该问卷已经过期");
		// errhashMap.put("err207", "答题人数满了");
		//
		errhashMap.put("err301", "活动未上线或已结束");
		errhashMap.put("err302", "该活动已售完");
		errhashMap.put("err303", "兑奖次数已用完");
		errhashMap.put("err304", "当前金币不足");
		errhashMap.put("err305", "未找到该活动");
		errhashMap.put("err306", "地址信息不正确");
		errhashMap.put("err307", "没有该活动记录");
		errhashMap.put("err308", "状态位为空");
		// errhashMap.put("err313", "验证码错误");
		// errhashMap.put("err314", "查不到提现记录");
		// errhashMap.put("err315", "交易已经结束");
		// errhashMap.put("err316", "没有更多提现记录了");

		errhashMap.put("err400", "新增收获地址错误");
		errhashMap.put("err401", "收获地址过多");
		errhashMap.put("err402", "该地址不存在");
		errhashMap.put("err403", "该地址不属于您");
		// errhashMap.put("err405", "昵称不能超过14位（英文1位 中文2位）");
		// errhashMap.put("err410", "地址超过10个,不能继续添加");
		// errhashMap.put("err411", "地址不存在或者已经被删除");
		// errhashMap.put("err412", "该收货地址不属于当前用户");
		// errhashMap.put("err421", "身份证格式不正确");
		// errhashMap.put("err422", "身份证姓名不匹配");
		// errhashMap.put("err423", "每个月只能修改一次身份验证信息");
		// errhashMap.put("err424", "验证信息提交错误次数超过限制,如有问题请联系我们.");
		// errhashMap.put("err425", "该身份验证已经被使用,如非本人操作请联系官方人员.");
		// errhashMap.put("err426", "您已绑定此身份验证,请勿重复提交.");
		//
		//
		errhashMap.put("err500", "target参数错误");
		errhashMap.put("err501", "origin参数错误");
		errhashMap.put("err502", "remark为空");
		// errhashMap.put("err503", "目标不能是自己");
		// errhashMap.put("err504", "未找到对方的请求记录");
		// errhashMap.put("err505", "不是好友关系");
		// errhashMap.put("err506", "此人已经是您的宝宝");
		// errhashMap.put("err507", "此人不是您的宝宝");
		// errhashMap.put("err508", "宝宝被购买的时间间隔没到");
		// errhashMap.put("err509", "已到宝宝数量限制");
		// errhashMap.put("err510", "今天已经打赏过该宝宝");
		// errhashMap.put("err520", "没有更多");

		errhashMap.put("err601", "计算未能有结果");
		errhashMap.put("err602", "没有好友问卷结果");
		errhashMap.put("err600", "问卷提交有误");

		errhashMap.put("err700", "暂没有心理地图，请做题");
		errhashMap.put("err701", "该类型暂没有心理题图，请做题");
		// errhashMap.put("err702", "不能再参加该活动了");
		// errhashMap.put("err703", "还没参加过该活动");
		// errhashMap.put("err704", "兑换奖品库存不足");
		// errhashMap.put("err705", "兑换奖品超出限制");
		// errhashMap.put("err706", "没有找到记录");
		// errhashMap.put("err707", "没有奖品信息");
		errhashMap.put("errFFF", "没有可用的网络连接");
		errhashMap.put("err800", "心情参数不正确");

	}

	public static String getErrMessage(String errcode) {
		return errhashMap.get(errcode);

	}
}
