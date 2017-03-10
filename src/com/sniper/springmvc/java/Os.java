package com.sniper.springmvc.java;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * 获取系统信息
 * 
 * @author suzhen
 * 
 */
public class Os {

	private OsValues osValue;
	private final static Sigar sigar = new Sigar();

	public Os() {
	}

	public Os(OsValues osValue) {
		this.osValue = osValue;
	}

	public void setOsValue(OsValues osValue) {
		this.osValue = osValue;
	}

	public OsValues getOsValue() {
		return osValue;
	}

	public List<Map<String, Object>> initNetwork() throws SigarException {
		String[] ifNames = sigar.getNetInterfaceList();
		List<Map<String, Object>> networks = new ArrayList<>();

		for (int i = 0; i < ifNames.length; i++) {
			String ifName = ifNames[i];
			NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ifName);
			if (NetFlags.LOOPBACK_ADDRESS.equals(ifConfig.getAddress())
					|| (ifConfig.getFlags() & NetFlags.IFF_LOOPBACK) != 0
					|| NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
				continue;
			}
			if ((ifConfig.getFlags() & 1L) <= 0L) {
				System.out.println("!IFF_UP...skipping getNetInterfaceStat");
				continue;
			}
			if (ifConfig.getAddress().equals(NetFlags.ANY_ADDR)) {
				continue;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("name", ifName);
			map.put("ip", ifConfig.getAddress());
			map.put("netmask", ifConfig.getNetmask());
			// 读取网络具体情况
			NetInterfaceStat interfaceStat = sigar.getNetInterfaceStat(ifName);
			OsNetwork osNetwork = new OsNetwork(interfaceStat);
			map.put("network", osNetwork);
			networks.add(map);
		}
		return networks;
	}

	public List<Map<String, Object>> initCpu() throws SigarException {
		CpuInfo cpuInfos[] = sigar.getCpuInfoList();
		CpuPerc cpuPerc[] = sigar.getCpuPercList();
		List<Map<String, Object>> cpus = new ArrayList<>();
		for (int i = 0; i < cpuInfos.length; i++) {
			CpuInfo cpuInfo = cpuInfos[i];
			Map<String, Object> map = new HashMap<>();
			map.put("id", i);
			map.put("MHz", cpuInfo.getMhz());
			map.put("vendor", cpuInfo.getVendor());
			map.put("model", cpuInfo.getVendor());
			map.put("cacheSize", cpuInfo.getCacheSize());
			OsCPU cpu = new OsCPU(cpuPerc[i]);
			map.put("cpu", cpu);
			cpus.add(map);
		}
		return cpus;
	}

	public List<Map<String, Object>> initHardDisk() throws SigarException {
		FileSystem fileSystems[] = sigar.getFileSystemList();
		List<Map<String, Object>> harddrisk = new ArrayList<>();
		for (int i = 0; i < fileSystems.length; i++) {
			Map<String, Object> map = new HashMap<>();
			FileSystem system = fileSystems[i];
			map.put("name", system.getDevName());
			map.put("dir", system.getDirName());
			map.put("flag", system.getFlags());
			map.put("systypename", system.getSysTypeName());
			map.put("typename", system.getTypeName());
			if (system.getType() == 2) {
				FileSystemUsage usage = sigar.getFileSystemUsage(system
						.getDirName());
				OsHardDisk disk = new OsHardDisk(usage);
				map.put("osHardDisk", disk);
			}

			harddrisk.add(map);
		}
		return harddrisk;
	}

	/**
	 * 获取heap堆内存使用情况
	 * 
	 * @return
	 */
	public void initHeapMemory() {
		MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
		MemoryUsage usage = memorymbean.getHeapMemoryUsage();
		osValue.setHeapMemoryUsageInit(usage.getInit());
		osValue.setHeapMemoryUsageMax(usage.getMax());
		osValue.setHeapMemoryUsageUsed(usage.getUsed());
	}

	/**
	 * 获取虚拟机内存
	 * 
	 * @return
	 */
	public void initJavaMemory() {
		Runtime runtime = Runtime.getRuntime();
		osValue.setJavaMemoryFree(runtime.freeMemory());
		osValue.setJavaMemoryMax(runtime.maxMemory());
		osValue.setJavaMemoryTotal(runtime.totalMemory());
	}

	public void initMemory() throws SigarException {

		Mem mem = sigar.getMem();
		osValue.setMemoryTotal(mem.getTotal());
		osValue.setMemoryFree(mem.getFree());
		osValue.setMemoryUsed(mem.getUsed());
	}

	public void initRuntime() {
		RuntimeMXBean rmb = ManagementFactory.getRuntimeMXBean();

		Map<String, String> map = rmb.getSystemProperties();

		osValue.setJavaVersion(map.get("java.version"));
		osValue.setVmName(rmb.getVmName());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(rmb.getStartTime());
		osValue.setVmStartTime(calendar.getTime());
		osValue.setVmUpTime(rmb.getUptime());
		osValue.setVmVersion(rmb.getVmVersion());

	}

	/**
	 * 系统基本信息介绍
	 * 
	 * @return
	 * @throws SigarException
	 * @throws UnknownHostException
	 */
	public Map<String, Object> initSystemInfo() throws SigarException,
			UnknownHostException {

		OperatingSystem os = OperatingSystem.getInstance();

		CpuInfo infos[] = sigar.getCpuInfoList();
		Map<String, Object> map = new HashMap<>();
		// 系统名称
		map.put("sysName", os.getVendorName());
		map.put("sysVersion", os.getVendorVersion());
		// 系统架构
		map.put("sysArch", os.getArch());
		// cpu数量
		map.put("cpuNum", infos.length);
		// 系统描述
		map.put("sysDescription", os.getDescription());

		InetAddress address = InetAddress.getLocalHost();
		map.put("ip", address.getHostAddress());

		Map<String, String> map1 = System.getenv();
		// 当前用户名
		map.put("username", map1.get("USERNAME"));
		// 电脑的名称
		map.put("pcName", map1.get("COMPUTERNAME"));
		// 电脑域
		map.put("pcUserdomain", map1.get("USERDOMAIN"));

		RuntimeMXBean rmb = ManagementFactory.getRuntimeMXBean();

		Map<String, String> javaMap = rmb.getSystemProperties();

		map.put("javaVersion", javaMap.get("java.version"));
		map.put("vmName", rmb.getVmName());
		map.put("vmVendor", rmb.getVmVendor());
		map.put("vmVersion", rmb.getVmVersion());

		Calendar calendar = Calendar.getInstance();
		map.put("time", calendar.getTime());
		calendar.setTimeInMillis(rmb.getStartTime());
		map.put("vmStartTime", calendar.getTime());
		map.put("vmUpTime", rmb.getUptime());

		return map;
	}

	public void initThread() {
		ThreadMXBean tm = ManagementFactory.getThreadMXBean();
		osValue.setThreadCount(tm.getThreadCount());
		osValue.setPeakThreadCount(tm.getPeakThreadCount());
		osValue.setDaemonThreadCount(tm.getDaemonThreadCount());
		osValue.setCurrentTheardCpuTime(tm.getCurrentThreadCpuTime());
		osValue.setCurrentTheardUserTime(tm.getCurrentThreadUserTime());

	}

	public void init() {

	}

	public static void main(String[] args) {
		long b = 5L;
		double c = 3l;
		double d = b / c;
		System.out.println(b / c);
		String a = String.format("%.0f %%", d);
		System.out.println(a);
	}
}
