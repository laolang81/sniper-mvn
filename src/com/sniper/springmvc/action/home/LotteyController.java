package com.sniper.springmvc.action.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.model.Lottery;
import com.sniper.springmvc.mybatis.service.impl.LotteryService;
import com.sniper.springmvc.searchUtil.BaseSearch;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/lottey")
public class LotteyController extends HomeBaseController {

	@Resource
	LotteryService lotteryService;

	@RequestMapping("")
	public String dlt(BaseSearch search, Map<String, Object> map) {

		// 查询最后一期
		Map<String, Object> paramsList = new HashMap<>();

		paramsList.put("order", "num desc");
		paramsList.put("pageOffset", 0);
		paramsList.put("pageSize", 1);
		List<Lottery> lotteries = lotteryService.pageList(paramsList);
		Lottery lastLottery = new Lottery();
		if (lotteries.size() == 1) {
			lastLottery = lotteries.get(0);
		}
		map.put("lastLottery", lastLottery);
		if (ValidateUtil.isValid(search.getName())) {

			String[] a = search.getName().split("\\|");
			Lottery lottery = new Lottery();
			lottery.setId(FilesUtil.getUUIDName("", false));
			lottery.setNum(a[0]);
			lottery.setDate(a[1]);

			int length = a[2].length();
			String ab = a[2].trim();
			String[] bb = new String[5];
			for (int i = 0, j = 0; i < length;) {
				String aa = ab.substring(i, i + 2);
				bb[j] = aa;
				i += 2;
				j++;
			}
			Map<String, Object> params = new HashMap<>();
			params.put("date", a[2]);
			if (lotteryService.find("find", params) == null) {
				lottery.setType(LotteryService.DLT);
				lottery.setNumOne(Integer.valueOf(bb[0]).intValue());
				lottery.setNumTwo(Integer.valueOf(bb[1]).intValue());
				lottery.setNumThree(Integer.valueOf(bb[2]).intValue());
				lottery.setNumFour(Integer.valueOf(bb[3]).intValue());
				lottery.setNumFive(Integer.valueOf(bb[4]).intValue());
				String[] d = a[3].split(",");
				lottery.setNumSix(Integer.valueOf(d[0]).intValue());
				lottery.setNumSeven(Integer.valueOf(d[1]).intValue());
				lotteryService.insert(lottery);
				map.put("msg", "成功");
			} else {
				map.put("msg", "失败");
			}
		}
		return "home/lottey/dlt.ftl";
	}
}
