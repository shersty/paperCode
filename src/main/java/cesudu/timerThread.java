package cesudu;

import ExcelTest2.standard.entity.commonValue;

public class timerThread implements Runnable{

	
	timeCounter timer;
	public timerThread(timeCounter timer){
		this.timer = timer;
	}
	
	@Override
	public void run() {
//		Thread.currentThread().setDaemon(true);	//设置为后台进程 不影响main主进程
		try {
			Thread.sleep(commonValue.maxRunTime);		//睡眠我规定的最大时间小时以后把对象的值设为false代表超时
			timer.setNowRun(false);						//	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
