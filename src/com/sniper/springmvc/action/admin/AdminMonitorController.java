package com.sniper.springmvc.action.admin;

import java.lang.management.ThreadMXBean;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.java.Os;
import com.sniper.springmvc.java.OsCPU;
import com.sniper.springmvc.java.OsKeys;
import com.sniper.springmvc.java.OsNetwork;
import com.sniper.springmvc.thread.ThreadModel;
import com.sniper.springmvc.thread.ThreadMxBeanUtils;
import com.sniper.springmvc.utils.ThreadUtil;

/**
 * 系统检测
 * 
 * @author suzhen
 * 
 */
@Controller
@RequestMapping("${adminPath}/admin-monitor")
public class AdminMonitorController extends AdminBaseController {

	@RequestMapping("view")
	public String view(Map<String, Object> map) throws SigarException,
			UnknownHostException {
		Os os = new Os();

		Map<String, Object> sysInfos = os.initSystemInfo();
		map.put("osValues", sysInfos);
		map.put("keys", OsKeys.getKeys());
		return forward("/admin/admin-monitor/view.ftl");
	}

	@RequestMapping(value = "cpu", method = RequestMethod.GET)
	public String cpu(Map<String, Object> map) throws SigarException {
		Os os = new Os();
		List<Map<String, Object>> cpus = os.initCpu();
		map.put("cpus", cpus);

		return forward("/admin/admin-monitor/cpu.ftl");
	}

	@ResponseBody
	@RequestMapping(value = "cpu", method = RequestMethod.POST)
	public List<Map<String, Object>> cpuAjax() throws SigarException {

		Os os = new Os();
		List<Map<String, Object>> cpus = os.initCpu();

		List<Map<String, Object>> seriess = new ArrayList<>();
		int i = 1;
		for (Map<String, Object> map2 : cpus) {
			Map<String, Object> series = new HashMap<>();
			series.put("name", "CPU" + i);
			series.put("type", "bar");
			List<Integer> data = new ArrayList<>();
			OsCPU cpu = (OsCPU) map2.get("cpu");
			data.add(Double.valueOf(Double.valueOf(cpu.getUser()) * 100)
					.intValue());
			data.add(Double.valueOf(Double.valueOf(cpu.getSys()) * 100)
					.intValue());
			data.add(Double.valueOf(Double.valueOf(cpu.getWait()) * 100)
					.intValue());
			data.add(Double.valueOf(Double.valueOf(cpu.getNice()) * 100)
					.intValue());
			data.add(Double.valueOf(Double.valueOf(cpu.getIdle()) * 100)
					.intValue());
			data.add(Double.valueOf(Double.valueOf(cpu.getCombined()) * 100)
					.intValue());
			series.put("data", data);
			seriess.add(series);
			i++;
		}
		return seriess;
	}

	@RequestMapping("memory")
	public String memory() {
		return forward("/admin/admin-monitor/memory.ftl");
	}

	@RequestMapping(value = "network", method = RequestMethod.GET)
	public String network(Map<String, Object> map) throws SigarException {

		Os os = new Os();
		List<Map<String, Object>> networks = os.initNetwork();
		map.put("networks", networks);
		return forward("/admin/admin-monitor/network.ftl");
	}

	@ResponseBody
	@RequestMapping(value = "network", method = RequestMethod.POST)
	public List<Map<String, Long>> network() throws SigarException,
			InterruptedException {

		Os os = new Os();
		List<Map<String, Object>> networks = os.initNetwork();
		int size = networks.size();
		Long[] rxBytesStart = new Long[size];
		Long[] txBytesStart = new Long[size];

		Long[] rxBytesEnd = new Long[size];
		Long[] txBytesEnd = new Long[size];

		Long[] timesStart = new Long[size];
		Long[] timesEnd = new Long[size];

		List<Map<String, Long>> real = new ArrayList<>();
		// 保存两秒之间的数据
		for (int i = 0; i < size; i++) {
			timesStart[i] = System.currentTimeMillis();
			Map<String, Long> map = getBytes(i);
			rxBytesStart[i] = map.get("rx");
			txBytesStart[i] = map.get("tx");
			Thread.sleep(1000);
			Map<String, Long> map1 = getBytes(i);
			timesEnd[i] = System.currentTimeMillis();
			rxBytesEnd[i] = map1.get("rx");
			txBytesEnd[i] = map1.get("tx");
		}

		//
		for (int i = 0; i < size; i++) {
			Map<String, Long> speeds = new HashMap<>();
			// 先把bytes转成kb， 后面把毫秒转成秒
			long rxbps = ((rxBytesEnd[i] - rxBytesStart[i]) / 1024)
					/ ((timesEnd[i] - timesStart[i]) / 1000);
			long txbps = ((txBytesEnd[i] - txBytesStart[i]) / 1024)
					/ ((timesEnd[i] - timesStart[i]) / 1000);
			speeds.put("rx", rxbps);
			speeds.put("tx", txbps);
			real.add(speeds);
		}

		return real;
	}

	public Map<String, Long> getBytes(int i) throws SigarException {
		Os os = new Os();
		List<Map<String, Object>> networks = os.initNetwork();
		Map<String, Long> map = new HashMap<>();

		OsNetwork cpu = (OsNetwork) networks.get(i).get("network");
		long rxBytesStart = cpu.getRxBytes();
		map.put("rx", rxBytesStart);
		long txBytesStart = cpu.getTxBytes();
		map.put("tx", txBytesStart);

		return map;
	}

	@RequestMapping("harddisk")
	public String hardDisk(Map<String, Object> map) throws SigarException {

		Os os = new Os();
		List<Map<String, Object>> harddrisk = os.initHardDisk();
		map.put("harddrisk", harddrisk);
		return forward("/admin/admin-monitor/harddisk.ftl");
	}

	/**
	 * 线程管理
	 * 
	 * @return
	 */
	@RequestMapping("thread")
	@RequiresPermissions("admin:monitor:thread")
	public String thread(Map<String, Object> map) {

		ThreadMXBean threadMXBean = ThreadMxBeanUtils.getThreadMXBean();
		map.put("threadMXBean", threadMXBean);
		map.put("lists", ThreadUtil.listThread());
		Map<String, Map<String, String>> threadMap = new HashMap<>();
		List<Map<String, String>> listThreads = ThreadUtil.listThread();
		for (Map<String, String> map2 : listThreads) {
			threadMap.put(map2.get("id"), map2);
		}

		List<ThreadModel> models = ThreadMxBeanUtils.listThreads();
		Collections.sort(models, new ThreadSort());

		map.put("threadMap", threadMap);
		map.put("lists", models);
		return forward("/admin/admin-monitor/thread.ftl");
	}

	/**
	 * 排序
	 * 
	 * @author suzhen
	 * 
	 */
	public class ThreadSort implements Comparator<ThreadModel> {

		@Override
		public int compare(ThreadModel o1, ThreadModel o2) {
			if (o1.getCpuTime() > o2.getCpuTime()) {
				return -1;
			} else if (o1.getCpuTime() < o2.getCpuTime()) {
				return 1;
			}
			return 0;
		}
	}

	public static void main(String[] args) {
		long a = 53950000l;
		System.out.println(a / 1024l / 1024);
	}
}
