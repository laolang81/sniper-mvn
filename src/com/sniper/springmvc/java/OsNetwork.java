package com.sniper.springmvc.java;

import org.hyperic.sigar.NetInterfaceStat;

public class OsNetwork {
	// 接受的总数据包
	private long rxPackets;
	private long txPackets;
	// 接受的字节数
	private long rxBytes;
	private long txBytes;
	// 错误包
	private long rxError;
	private long txError;
	// 丢弃的包
	private long rxDropped;
	private long txDropped;
	private long speed;
	private long rxFrame;
	private long rxOverruns;
	private long txOverruns;
	private long txCarrier;

	private NetInterfaceStat interfaceStat;

	public OsNetwork(NetInterfaceStat interfaceStat) {
		this.interfaceStat = interfaceStat;
		this.init();
	}

	public OsNetwork() {

	}

	public void init() {
		interfaceStat.getRxBytes();
		this.rxPackets = interfaceStat.getRxPackets();
		this.txPackets = interfaceStat.getTxPackets();
		this.rxBytes = interfaceStat.getRxBytes();
		this.txBytes = interfaceStat.getTxBytes();
		this.rxError = interfaceStat.getRxErrors();
		this.txError = interfaceStat.getTxErrors();
		this.rxDropped = interfaceStat.getRxDropped();
		this.txDropped = interfaceStat.getTxDropped();
		
		this.txCarrier = interfaceStat.getTxCarrier();
		this.speed = interfaceStat.getSpeed();
		this.rxFrame = interfaceStat.getRxFrame();
		this.rxOverruns = interfaceStat.getRxOverruns();
		this.txOverruns = interfaceStat.getTxOverruns();

	}

	public long getRxPackets() {
		return rxPackets;
	}

	public void setRxPackets(long rxPackets) {
		this.rxPackets = rxPackets;
	}

	public long getTxPackets() {
		return txPackets;
	}

	public void setTxPackets(long txPackets) {
		this.txPackets = txPackets;
	}

	public long getRxBytes() {
		return rxBytes;
	}

	public void setRxBytes(long rxBytes) {
		this.rxBytes = rxBytes;
	}

	public long getTxBytes() {
		return txBytes;
	}

	public void setTxBytes(long txBytes) {
		this.txBytes = txBytes;
	}

	public long getRxError() {
		return rxError;
	}

	public void setRxError(long rxError) {
		this.rxError = rxError;
	}

	public long getTxError() {
		return txError;
	}

	public void setTxError(long txError) {
		this.txError = txError;
	}

	public long getRxDropped() {
		return rxDropped;
	}

	public void setRxDropped(long rxDropped) {
		this.rxDropped = rxDropped;
	}

	public long getTxDropped() {
		return txDropped;
	}

	public void setTxDropped(long txDropped) {
		this.txDropped = txDropped;
	}

	public NetInterfaceStat getInterfaceStat() {
		return interfaceStat;
	}

	public void setInterfaceStat(NetInterfaceStat interfaceStat) {
		this.interfaceStat = interfaceStat;
	}

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public long getRxFrame() {
		return rxFrame;
	}

	public void setRxFrame(long rxFrame) {
		this.rxFrame = rxFrame;
	}

	public long getRxOverruns() {
		return rxOverruns;
	}

	public void setRxOverruns(long rxOverruns) {
		this.rxOverruns = rxOverruns;
	}

	public long getTxOverruns() {
		return txOverruns;
	}

	public void setTxOverruns(long txOverruns) {
		this.txOverruns = txOverruns;
	}

	public long getTxCarrier() {
		return txCarrier;
	}

	public void setTxCarrier(long txCarrier) {
		this.txCarrier = txCarrier;
	}

}
