package com.sniper.springmvc.action.admin;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.java.Os;
import com.sniper.springmvc.java.OsValues;

@Controller
@RequestMapping("${adminPath}/os")
public class AdminOsController {

	@ResponseBody
	@RequestMapping("javaMemory")
	public String javaMemory() {
		OsValues osValues = new OsValues();
		Os os = new Os(osValues);
		float reult = 0;
		os.initJavaMemory();
		DecimalFormat decimalFormat = new DecimalFormat("0");
		reult = (float) osValues.getJavaMemoryFree()
				/ osValues.getJavaMemoryTotal();
		reult = (1 - reult) * 100;
		return decimalFormat.format(reult);

	}

	@ResponseBody
	@RequestMapping("heapMemory")
	public String heapMemory() {
		OsValues osValues = new OsValues();
		Os os = new Os(osValues);
		float reult = 0;
		os.initHeapMemory();
		DecimalFormat decimalFormat = new DecimalFormat("0");
		reult = (float) osValues.getHeapMemoryUsageUsed()
				/ osValues.getHeapMemoryUsageMax();
		reult = (1 - reult) * 100;
		return decimalFormat.format(reult);

	}

	@ResponseBody
	@RequestMapping("sysMemory")
	public String sysMemory() throws SigarException {
		OsValues osValues = new OsValues();
		Os os = new Os(osValues);
		float reult = 0;
		os.initMemory();
		DecimalFormat decimalFormat = new DecimalFormat("0");
		reult = (float) osValues.getMemoryUsed() / osValues.getMemoryTotal();
		reult = reult * 100;
		return decimalFormat.format(reult);

	}

	@ResponseBody
	@RequestMapping("memory")
	public Map<String, String> memory() throws SigarException {

		Map<String, String> memery = new HashMap<>();
		OsValues osValues = new OsValues();
		Os os = new Os(osValues);
		float sysReult = 0;
		os.initMemory();
		DecimalFormat decimalFormat = new DecimalFormat("0");
		sysReult = (float) osValues.getMemoryUsed() / osValues.getMemoryTotal();
		sysReult = sysReult * 100;
		memery.put("sys", decimalFormat.format(sysReult));
		float heapReult = 0;
		os.initHeapMemory();
		heapReult = (float) osValues.getHeapMemoryUsageUsed()
				/ osValues.getHeapMemoryUsageMax();
		heapReult = (1 - heapReult) * 100;
		memery.put("heap", decimalFormat.format(heapReult));

		return memery;

	}

	public static void main(String[] args) {
		OsValues osValues = new OsValues();
		Os os = new Os(osValues);
		os.initRuntime();
		System.out.println(osValues.getJavaVersion());
	}
}