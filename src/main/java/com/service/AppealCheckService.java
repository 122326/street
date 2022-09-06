package com.service;



public interface AppealCheckService {

	/**
	 * 检测进行中的appeal是否过期，如果过期，则修改appeal的状态
	 */
	public void checkAppeal();
	
	/**
	 * 检测进行中的help是否过期，如果过期，则修改help的状态
	 */
	public void checkHelp();
}
