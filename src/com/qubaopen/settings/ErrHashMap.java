package com.qubaopen.settings;

import java.util.HashMap;

public class ErrHashMap {
	private static final HashMap<String, String> errhashMap = new HashMap<String, String>();

	static {
		insertErrCode();
	}

	private static void insertErrCode() {
		errhashMap.put("err000", "用户未登录");
		errhashMap.put("err001", "亲，没有该用户呦！");
		errhashMap.put("err002", "亲，密码错误，可不能登录呦！");
		errhashMap.put("err003", "亲，您的手机号没有填对呢！");
		errhashMap.put("err004", "密码需要8位以上的字母或数字哦！");
		errhashMap.put("err005", "亲，您的邮箱没有填对呢！");
		errhashMap.put("err006", "亲，此号码已注册过咯！如有问题，麻烦联系我们微信客服哦！");
		errhashMap.put("err007", "您输的验证码不对哦！");
		errhashMap.put("err008", "您还没获取过验证码呦！");
		errhashMap.put("err009", "亲，请休息一会再验证呦！");
		errhashMap.put("err010", "亲，您让人家发了太多次验证码了啦");
		errhashMap.put("err011", "亲，短信被怪兽吃掉了~麻烦您联系我们微信客服吧~");
		errhashMap.put("err012", "亲，验证码都没填，我们还怎么愉快的玩耍呀~");
		errhashMap.put("err013", "亲，请重新申请验证码吧~");
		errhashMap.put("err014", "亲，您的信息有误，请检查有无遗漏哦");
		errhashMap.put("err015", "原密码格式不正确");
		errhashMap.put("err016", "原密码不匹配");
		errhashMap.put("err017", "亲，您的年龄段没有合适的测评哦！");

		errhashMap.put("err101", "亲，信息修改没成功，求您重新提交吧~");
		errhashMap.put("err102", "亲，您的头像把服务器帅呆了，请您再传一次吧~");
		// errhashMap.put("err103", "系统无记录");
		// errhashMap.put("err104", "检测到数据量异常,如有问题请联系官方人员");
		// errhashMap.put("err105", "输入内容不能为空");

		errhashMap.put("err200", "亲，信息修改没成功，求您重新提交吧~");
		errhashMap.put("err201", "亲，您已经达到本月的绑定次数咯~");
		errhashMap.put("err202", "亲，您的身份证不符合我国的格式呢。难道您是来自阿拉伯的壕？");
		errhashMap.put("err203", "亲，您的身份太热门，已被人抢注。请联系我们微信客服哦");
		errhashMap.put("err204", "亲，要修改绑定信息，只能下个月再来咯~");
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
		errhashMap.put("err401", "壕，您设的地址太多了呢~");
		errhashMap.put("err402", "亲，这个地址已经不存在啦。请您刷新页面呦~");
		errhashMap.put("err403", "亲，这可不是您的地址呦~刷新一下就好~");
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
		errhashMap.put("err500", "恭喜您，遇到了百年一遇的服务器出错，快去买彩票吧");
		errhashMap.put("err501", "恭喜您，遇到了百年一遇的服务器出错，快去买彩票吧");
		errhashMap.put("err502", "恭喜您，遇到了百年一遇的服务器出错，快去买彩票吧");
		// errhashMap.put("err503", "目标不能是自己");
		// errhashMap.put("err504", "未找到对方的请求记录");
		// errhashMap.put("err505", "不是好友关系");
		// errhashMap.put("err506", "此人已经是您的宝宝");
		// errhashMap.put("err507", "此人不是您的宝宝");
		// errhashMap.put("err508", "宝宝被购买的时间间隔没到");
		// errhashMap.put("err509", "已到宝宝数量限制");
		// errhashMap.put("err510", "今天已经打赏过该宝宝");
		// errhashMap.put("err520", "没有更多");

		errhashMap.put("err601", "亲，您真的认真做题了吗？面对您这样百年一遇的奇才，系统也给不出答案啦~");
		errhashMap.put("err602", "恭喜您，遇到了百年一遇的服务器出错，快去买彩票吧");
		errhashMap.put("err600", "恭喜您，遇到了百年一遇的服务器出错，快去买彩票吧");

		errhashMap.put("err700", "亲，您还没有分析图哦~快去做做测评吧");
		errhashMap.put("err701", "亲，您还没有该类型的分析图哦~快去做做测评吧");
		// errhashMap.put("err702", "不能再参加该活动了");
		// errhashMap.put("err703", "还没参加过该活动");
		// errhashMap.put("err704", "兑换奖品库存不足");
		// errhashMap.put("err705", "兑换奖品超出限制");
		// errhashMap.put("err706", "没有找到记录");
		// errhashMap.put("err707", "没有奖品信息");
		errhashMap.put("errFFF", "没有可用的网络连接");
		errhashMap.put("err800", "恭喜您，遇到了百年一遇的服务器出错，快去买彩票吧");

	}

	public static String getErrMessage(String errcode) {
		return errhashMap.get(errcode);

	}
}
